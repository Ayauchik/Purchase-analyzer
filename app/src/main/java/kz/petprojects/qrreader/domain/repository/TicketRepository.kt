package kz.petprojects.qrreader.domain.repository

import kz.petprojects.qrreader.domain.models.TicketData

interface TicketRepository {
    suspend fun fetchTicketData(
        registrationNumber: String,
        ticketNumber: String,
        ticketDate: String
    ): TicketData
}