package kz.petprojects.qrreader.ui


import android.app.Application
import kz.petprojects.qrreader.di.mapperModule
import kz.petprojects.qrreader.di.networkModule
import kz.petprojects.qrreader.di.repositoryModule
import kz.petprojects.qrreader.di.useCaseModule
import kz.petprojects.qrreader.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class CustomApplication : Application() {
    private val modulesToUse = listOf(
        networkModule,
        repositoryModule,
        viewModelModule,
        useCaseModule,
        mapperModule
    )

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@CustomApplication)
            parametersOf("https://cabinet.kofd.kz/")
            modules(modulesToUse)
        }
    }
}