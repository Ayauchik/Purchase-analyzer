package kz.petprojects.qrreader.di

import kz.petprojects.qrreader.ui.qr.TicketViewModel
import org.koin.dsl.module


val viewModelModule = module {
    single { TicketViewModel(get()) }
}