package ru.sshex.androidcartest.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CarPlaygroundPresenter @Inject constructor() : MvpPresenter<CarPlaygroundView>() {
	fun onSeekBarSpeedChange(progress: Int) {
		val speed = 6000 - (progress+1) * 500
		viewState.setSpeed(speed.toLong())
		viewState.stopCar()
	}
}