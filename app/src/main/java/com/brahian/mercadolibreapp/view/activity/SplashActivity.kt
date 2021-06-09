package com.brahian.mercadolibreapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for app splash
 */
class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MainActivity.start(this)
    finish()
  }
}
