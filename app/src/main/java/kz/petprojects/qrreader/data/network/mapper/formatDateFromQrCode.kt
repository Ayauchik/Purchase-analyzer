package kz.petprojects.qrreader.data.network.mapper

import java.text.SimpleDateFormat
import java.util.*

fun formatDateFromQrCode(qrDate: String): String? {
    val inputFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(qrDate)
    return date?.let { outputFormat.format(it) }
}
