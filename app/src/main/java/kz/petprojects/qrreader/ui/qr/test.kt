package kz.petprojects.qrreader.ui.qr
// Step 1: Process Price function that converts price to Double
fun processPrice(price: String): Double {
    // Remove any spaces, commas, and replace the comma with a dot
    val cleanedPrice = price.replace(Regex("[^0-9,]"), "").replace(",", ".")
    return cleanedPrice.toDoubleOrNull() ?: 0.0 // Return 0.0 if invalid
}

// Step 2: Update `extractReceiptMapFromText` to process prices
fun extractReceiptMapFromText(receiptText: String): Map<String, Double> {
    val receiptMap = mutableMapOf<String, Double>()
    val regex = Regex("""^(.*)=(.*)$""") // Matches and splits at `=`
    val shtRegex = Regex("""\(шт\).*""") // Matches `(шт)` and everything after it
    val shtRegex2 = Regex("""\( шт\).*""")
    val shtRegex4 = Regex("""\(  шт\).*""")
    val shtRegex3 = Regex("""\(шт \).*""")

    // Split the combined text into lines and process each line
    receiptText.split("\n").forEach { line ->
        val trimmedLine = line.trim()
        val match = regex.find(trimmedLine)
        if (match != null) {
            var (beforeEquals, afterEquals) = match.destructured
            // Remove `(шт)` and everything after it
            beforeEquals = beforeEquals.replace(shtRegex, "").trim()
            beforeEquals = beforeEquals.replace(shtRegex2, "").trim()
            beforeEquals = beforeEquals.replace(shtRegex3, "").trim()
            beforeEquals = beforeEquals.replace(shtRegex4, "").trim()

            // Process price using the `processPrice` function
            val processedPrice = processPrice(afterEquals.trim())
            receiptMap[beforeEquals] = processedPrice
        }
    }
    return receiptMap
}

// Step 3: Combine Receipt Text Function
fun extractReceiptText(receipt: List<String>, startIndex: Int, endIndex: Int): String {
    return receipt.subList(startIndex, endIndex)
        .joinToString(" ") { line ->
            // Trim trailing spaces before joining
            val trimmedLine = line.trimEnd()
            if (trimmedLine.contains("=")) "$trimmedLine\n" else trimmedLine
        }
        .split("\n") // Split by newlines to process each segment individually
        .joinToString("\n") { line ->
            line.replace(Regex("\\s{2,}"), " ") // Replace multiple spaces with one in each segment
        }
}

fun main() {
    val receipt = listOf(
        "***********************************************",
        "Блокнот А5 в линию KANCDOM 48стр 365 Planner 2  ",
        "35D2533 (шт)                                    ",
        "1 (Штука) x 1 200,00₸                = 1 200,00₸",
        "Ежедневник в точку «Чего тебе», А5, 64 листа (  ",
        "шт)                                             ",
        "1 (Штука) x 600,00₸                    = 600,00₸",
        "Блокнот А5 60л в линию, на гребне Star DSA5-CF  ",
        "XQ040 (шт)                                      ",
        "1 (Штука) x 1 200,00₸                = 1 200,00₸",
        "Стикер \"Weekly Plan\" 50л, 100*140мм  (шт)       ",
        "1 (Штука) x 1 300,00₸                = 1 300,00₸",
        "Ручка шариковая 0.7мм GIGIS UNIMAX G-LOTUS, си  ",
        "няя (шт)                                        ",
        "1 (Штука) x 185,00₸                    = 185,00₸",
        "Ручка шариковая 0.7мм GIGIS UNIMAX G-BILLION,   ",
        "silver clip, синяя (шт)                         ",
        "1 (Штука) x 440,00₸                    = 440,00₸",
        "------------------------------------------------"
    )

    // Define the start and end indexes
    val startIndex = receipt.indexOfFirst { it.contains("***********************************************") } + 1
    val endIndex = receipt.indexOfFirst { it.contains("------------------------------------------------") }

    // Step 1: Combine the receipt text
    val combinedText = extractReceiptText(receipt, startIndex, endIndex)
    println("Combined Text:\n$combinedText\n")

    // Step 2: Create a map from the combined text
    val receiptMap = extractReceiptMapFromText(combinedText)
    receiptMap.forEach { (key, value) ->
        println("Before `=`: $key")
        println("Processed Price: $value\n")
    }
}
