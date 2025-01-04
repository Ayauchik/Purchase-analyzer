package kz.petprojects.qrreader.data.use_cases

import android.util.Log
import kz.petprojects.qrreader.domain.models.TicketData
import kz.petprojects.qrreader.domain.repository.TicketRepository
import kz.petprojects.qrreader.domain.use_cases.GetTicketUseCase

class GetTicketUseCaseImpl(
    private val ticketRepository: TicketRepository
) : GetTicketUseCase {
    override suspend fun getTicket(
        registrationNumber: String,
        ticketNumber: String,
        ticketDate: String
    ): TicketData {

        val data = ticketRepository.fetchTicketData(registrationNumber, ticketNumber, ticketDate)
        Log.e("UseCase", data.data?.found.toString())
        return data
    }
}