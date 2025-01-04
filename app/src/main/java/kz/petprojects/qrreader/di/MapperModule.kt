package kz.petprojects.qrreader.di

import kz.petprojects.qrreader.data.network.mapper.TicketMapper
import org.koin.dsl.module


val mapperModule = module{
    factory { TicketMapper() }
}