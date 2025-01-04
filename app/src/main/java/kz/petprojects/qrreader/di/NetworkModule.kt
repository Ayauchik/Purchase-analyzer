package kz.petprojects.qrreader.di

import kz.petprojects.qrreader.data.network.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    factory<Converter.Factory> { GsonConverterFactory.create() }
    factory { HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    }


    factory<OkHttpClient> {
        OkHttpClient.Builder()
            //.addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://cabinet.kofd.kz/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }
}


