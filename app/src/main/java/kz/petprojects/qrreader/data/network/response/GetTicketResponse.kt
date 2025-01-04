package kz.petprojects.qrreader.data.network.response

data class GetTicketResponse(
    val `data`: Data,
    val error: String? = null
)