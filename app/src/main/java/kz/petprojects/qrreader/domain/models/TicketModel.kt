package kz.petprojects.qrreader.domain.models

data class TicketModel(
    val found: Int? = null,
    val ticket: List<TicketItemModel>? = null,
    val ticketUrl: String? = null
)