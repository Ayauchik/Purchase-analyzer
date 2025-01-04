package kz.petprojects.qrreader.data.network.mapper

import android.net.Uri

fun extractParametersFromUrl(url: String): Map<String, String> {
    val uri = Uri.parse(url)
    val i = uri.getQueryParameter("i") ?: ""
    val f = uri.getQueryParameter("f") ?: ""
    val t = uri.getQueryParameter("t") ?: ""

    val ticketDate = t.substring(0, 4) + "-" + t.substring(4, 6) + "-" + t.substring(6, 8)
    return mapOf(
        "ticketNumber" to i,
        "registrationNumber" to f,
        "ticketDate" to ticketDate
    )
}
