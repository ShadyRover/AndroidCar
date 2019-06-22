package ru.sshex.androidcartest.common.di

import android.app.Application
import ru.sshex.androidcartest.di.CarPlaygroundComponent

object Injector {

	private var appComponent: AppComponent? = null

	fun initAppComponent(app: Application) {
		appComponent = DaggerAppComponent.builder()
			.applicationContextModule(ApplicationContextModule(app))
			.build()
	}

	val carPlaygroundComponent: CarPlaygroundComponent by lazy {
		if (appComponent == null) {
			throw IllegalStateException("Must initialize AppComponent")
		}
		appComponent!!.currenciesBuilder().build()
	}
}


