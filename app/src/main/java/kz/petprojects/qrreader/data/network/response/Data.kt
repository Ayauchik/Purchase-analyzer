package kz.petprojects.qrreader.data.network.response

data class Data(
    val found: Int? = 0,
    val ticket: List<Ticket?>? = emptyList(),
    val ticketUrl: String? = ""
)