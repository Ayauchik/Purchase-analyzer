package kz.petprojects.qrreader.ui.qr

fun main() {
    val receiptLines = listOf(
        "***********************************************",
        "DILLIGEN Super econom Кассеты 30шт ПРОМО Стано",
        "к",
        "1 (Штука) x 2 990,00₸                = 2 990,00₸",
        "DR KANG МАСКА ТКАНЕВАЯ ДЛЯ ЛИЦА AHA/BHA/PHA (A",
        "NT",
        "1 (Штука) x 230,00₸                    = 230,00₸",
        "DR KANG МАСКА ТКАНЕВАЯ ДЛЯ ЛИЦА HYALURONIC ACI",
        "D",
        "2 (Штука) x 230,00₸                    = 460,00₸",
        "Вода мицеллярная Garnier Skin Nаturаls Витамин",
        "С",
        "1 (Штука) x 3 025,00₸                = 3 025,00₸",
        "ПАКЕТ COSMART ПОЛИЭТИЛЕНОВЫЙ БОЛЬШОЙ",
        "1 (Штука) x 45,00₸                      = 45,00₸",
        "------------------------------------------------"
    )

    val startIndex = receiptLines.indexOfFirst { it.contains("***********************************************") } + 1
    val endIndex = receiptLines.indexOfFirst { it.contains("------------------------------------------------") }


    val combinedText = extractReceiptTextTest(receiptLines, startIndex, endIndex)
 //   println(combinedText)

    val result = processReceiptText(combinedText)
    println(result)
//    val string = "2 (Штука) x 230, 90₸ = 230, 00₸"
//    println(extractPriceFromCalculationLine(string))

}


fun extractReceiptTextTest(receipt: List<String>, startIndex: Int, endIndex: Int): String {
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

fun processReceiptText(receiptText: String): List<Pair<String, String>> {
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




fun extractPriceFromCalculationLine(line: String): String {
    val regex = Regex("""(\d+)\s*\(.*?\)\s*x\s*([\d\s,]+₸)\s*=""")
    val match = regex.find(line)
    val price = match?.groupValues?.get(2)?.replace("\\s".toRegex(), "") ?: ""
    return if (price.isNotEmpty()) price.dropLast(1) else price
}




data class Product(
    val name: String,
    val price: String
)