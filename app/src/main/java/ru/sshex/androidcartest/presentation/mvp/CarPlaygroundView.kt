package ru.sshex.androidcartest.presentation.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CarPlaygroundView : MvpView {
	fun setSpeed(speed: Long)
	fun stopCar()
}