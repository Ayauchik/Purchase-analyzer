package kz.petprojects.qrreader.data.reporsitory

import android.util.Log
import kz.petprojects.qrreader.data.network.api.ApiService
import kz.petprojects.qrreader.data.network.mapper.TicketMapper
import kz.petprojects.qrreader.domain.models.TicketData
import kz.petprojects.qrreader.domain.repository.TicketRepository

class TicketRepositoryImpl(
    private val apiService: ApiService,
    private val ticketMapper: TicketMapper
) : TicketRepository {

    override suspend fun fetchTicketData(
        registrationNumber: String,
        ticketNumber: String,
        ticketDate: String
    ): TicketData {

        val response = apiService.getTicketData(registrationNumber, ticketNumber, ticketDate)
        Log.e("TicketRepository", response.data.found.toString())
        return ticketMapper.fromRemoteToDomain(response)

    }
}
