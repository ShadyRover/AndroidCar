package ru.sshex.androidcartest.common.di

import dagger.Component
import ru.sshex.androidcartest.di.CarPlaygroundComponent
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationContextModule::class])
interface AppComponent {
	fun currenciesBuilder(): CarPlaygroundComponent.Builder
}