package ru.sshex.androidcartest

import android.app.Application
import ru.sshex.androidcartest.common.di.Injector

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		Injector.initAppComponent(this)
	}

}