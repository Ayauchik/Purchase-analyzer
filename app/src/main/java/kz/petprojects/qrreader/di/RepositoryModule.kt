package kz.petprojects.qrreader.di

import kz.petprojects.qrreader.data.reporsitory.TicketRepositoryImpl
import kz.petprojects.qrreader.domain.repository.TicketRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<TicketRepository> { TicketRepositoryImpl(get(), get()) }
}