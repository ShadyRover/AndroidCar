package ru.sshex.androidcartest.presentation.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.sshex.androidcartest.R
import ru.sshex.androidcartest.common.di.Injector
import ru.sshex.androidcartest.presentation.mvp.CarPlaygroundPresenter

class CarPlayGroundActivity : MvpAppCompatActivity() {
	companion object {
		@LayoutRes
		const val LAYOUT = R.layout.activity_main
	}

	@InjectPresenter
	lateinit var carPlaygroundPresenter: CarPlaygroundPresenter

	@ProvidePresenter
	fun provideCurrenciesPresenter() = Injector.carPlaygroundComponent.carPlaygroundPresenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(LAYOUT)
	}
}
