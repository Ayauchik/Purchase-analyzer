package kz.petprojects.qrreader.data.network.mapper

import kz.petprojects.qrreader.data.network.response.Data
import kz.petprojects.qrreader.data.network.response.GetTicketResponse
import kz.petprojects.qrreader.data.network.response.Ticket
import kz.petprojects.qrreader.domain.models.TicketData
import kz.petprojects.qrreader.domain.models.TicketItemModel
import kz.petprojects.qrreader.domain.models.TicketModel

class TicketMapper {
    fun fromRemoteToDomain(ticketResponse: GetTicketResponse): TicketData {

        return TicketData(
            data = dataMapper(ticketResponse.data),
            error = ticketResponse.error.toString(),
        )
    }

    private fun dataMapper(data: Data): TicketModel {
        return TicketModel(
            found = data.found,
            ticket = data.ticket?.map {ticketItemMapper(it!!)},
            ticketUrl = data.ticketUrl
        )
    }

    private fun ticketItemMapper(ticket: Ticket): TicketItemModel{
        return TicketItemModel(
            text = ticket.text,
            style = ticket.style
        )
    }
}