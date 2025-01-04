package kz.petprojects.qrreader.di

import kz.petprojects.qrreader.data.use_cases.GetTicketUseCaseImpl
import kz.petprojects.qrreader.domain.use_cases.GetTicketUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetTicketUseCase> { GetTicketUseCaseImpl(get()) }
}