package ru.sshex.androidcartest.di

import dagger.Subcomponent
import ru.sshex.androidcartest.presentation.mvp.CarPlaygroundPresenter

@CarPlaygroundScope
@Subcomponent(modules = [CarPlaygroundModule::class])
interface CarPlaygroundComponent {

	@Subcomponent.Builder
	interface Builder {
		fun build(): CarPlaygroundComponent
	}

	val carPlaygroundPresenter: CarPlaygroundPresenter

}
