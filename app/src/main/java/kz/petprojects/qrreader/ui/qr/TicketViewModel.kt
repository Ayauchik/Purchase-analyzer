package kz.petprojects.qrreader.ui.qr

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.petprojects.qrreader.domain.models.TicketData
import kz.petprojects.qrreader.domain.use_cases.GetTicketUseCase

class TicketViewModel(
    private val getTicketUseCase: GetTicketUseCase
) : ViewModel() {

    private val _ticketState = MutableStateFlow(TicketState())
    val ticketState: StateFlow<TicketState> = _ticketState.asStateFlow()

//    init {
//        fetchData()
//    }

    fun fetchData(registrationNumber: String, ticketNumber: String, ticketDate: String){
        viewModelScope.launch {
            try {
                val response = getTicketUseCase.getTicket(registrationNumber, ticketNumber, ticketDate)
                Log.e("TicketViewModel response", response.data?.found.toString())

                if (response.data == null) {
                    Log.e("TicketViewModel", "Response data is null")
                    return@launch
                }

                _ticketState.value = mapToTicketState(response)!!
                Log.e("TicketViewModel state", _ticketState.value.ticketUrl.toString())

            } catch (e:Exception) {
                Log.e("TicketViewModel", e.message.toString())
            }
        }
    }

    private fun mapToTicketState(ticketData: TicketData): TicketState? {
        val ticketItems = ticketData.data?.ticket ?: return null

        val where = ticketItems.firstOrNull()?.text?.trim()
        val whenLine = ticketItems.find { it.text!!.contains("УАҚЫТЫ/ВРЕМЯ", ignoreCase = true) }?.text
        val `when` = whenLine?.substringAfter("УАҚЫТЫ/ВРЕМЯ:")?.trim()


        val startIndex = ticketItems.indexOfFirst { it.text!!.contains("***********************************************") }
        val endIndex = ticketItems.indexOfFirst { it.text!!.contains("------------------------------------------------") }

        val receiptLines = ticketItems.subList(startIndex+1, endIndex).mapNotNull { it.text }
        val combinedText = extractReceiptText(receiptLines, 0, receiptLines.size)
        val receiptMap = processReceiptText(combinedText)

        val products = receiptMap.map { (name, price) ->
            val priceValue = processPrice(price)
            ItemState(name, priceValue)
        }

        val totalLine = ticketItems.find { it.text!!.contains("БАРЛЫҒЫ/ИТОГО", ignoreCase = true) }?.text
        Log.e("total line", totalLine.toString())

        val totalPrice = extractTotalPrice(totalLine!!)
        val ticketUrl = ticketData.data.ticketUrl

        return TicketState(
            where = where,
            `when` = `when`,
            products = products,
            totalPrice = totalPrice,
            ticketUrl = ticketUrl
        )
    }


    private fun extractReceiptText(receipt: List<String>, startIndex: Int, endIndex: Int): String {
        val result = receipt.subList(startIndex, endIndex)
            .joinToString("") { line ->
                // Trim trailing spaces before joining
                val trimmedLine = line.trimEnd()
                // Add space after commas if not already followed by one
                val formattedLine = trimmedLine.replace(Regex(",(?!\\s)"), ", ")
                if (formattedLine.contains("₸")) "\n$formattedLine\n" else formattedLine
            }
            .split("\n") // Split by newlines to process each segment individually
            .joinToString("\n") { line ->
                line.replace(Regex("\\s{2,}"), " ") // Replace multiple spaces with one in each segment
            }

        return result
    }

    private fun extractPriceFromCalculationLine(line: String): String {
        val regex = Regex("""(\d+)\s*\(.*?\)\s*x\s*([\d\s,]+₸)\s*=""")
        val match = regex.find(line)
        val price = match?.groupValues?.get(2)?.replace("\\s".toRegex(), "") ?: ""
        return if (price.isNotEmpty()) price.dropLast(1) else price
    }

    private fun processReceiptText(receiptText: String): List<Pair<String, String>> {
        val lines = receiptText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        val result = mutableListOf<Pair<String, String>>()

        for (i in lines.indices step 2) {
            val name = lines[i] // Odd-indexed line (0-based index, i.e., name)
            if (i + 1 < lines.size) { // Ensure there is a next line for the price
                val priceLine = lines[i + 1] // Even-indexed line (calculation line)
                val price = extractPriceFromCalculationLine(priceLine) // Extract price using helper function
                result.add(Pair(name, price))
            }
        }
        return result
    }

    private fun processPrice(price: String): Double {
        // Remove any spaces, commas, and replace the comma with a dot
        val cleanedPrice = price.replace(Regex("[^0-9,]"), "").replace(",", ".")
        return cleanedPrice.toDoubleOrNull() ?: 0.0 // Return 0.0 if invalid
    }

    private fun extractTotalPrice(receiptText: String): Double {
        val regex = Regex("""БАРЛЫҒЫ/ИТОГО:\s*(\d[\d\s,]*\d)₸""") // Matches the pattern after "БАРЛЫҒЫ/ИТОГО:"
        val match = regex.find(receiptText)

        return if (match != null) {
            // Extract the price part and apply processPrice
            val priceString = match.groupValues[1]
            processPrice(priceString) // Apply processPrice to extract the number as Double
        } else {
            0.0 // Return 0.0 if no match is found
        }
    }

}

    data class TicketState(
        val where: String? = null,
        val `when`: String? = null,
        val products: List<ItemState>? = null,
        val totalPrice: Double? = null,
        val ticketUrl: String? = null
    )

    data class ItemState(
        val name: String? = null,
        val price: Double? = null
    )