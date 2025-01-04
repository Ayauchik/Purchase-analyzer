package kz.petprojects.qrreader.data.network.api

import kz.petprojects.qrreader.data.network.response.GetTicketResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/tickets")
    suspend fun getTicketData(
        @Query("registrationNumber") registrationNumber: String,
        @Query("ticketNumber") ticketNumber: String,
        @Query("ticketDate") ticketDate: String
    ): GetTicketResponse
}