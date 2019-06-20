package ru.sshex.androidcartest.presentation.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.sshex.androidcartest.R

class CarPlayGroundActivity : MvpAppCompatActivity() {
	companion object {
		@LayoutRes
		const val LAYOUT = R.layout.activity_main
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(LAYOUT)
	}
}
