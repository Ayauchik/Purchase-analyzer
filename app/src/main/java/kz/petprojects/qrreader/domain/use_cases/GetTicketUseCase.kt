package kz.petprojects.qrreader.domain.use_cases

import kz.petprojects.qrreader.domain.models.TicketData

interface GetTicketUseCase {
    suspend fun getTicket(
        registrationNumber: String,
        ticketNumber: String,
        ticketDate: String
    ): TicketData
}