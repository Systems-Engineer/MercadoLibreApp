package com.brahian.mercadolibreapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MainActivity.start(this)
    finish()
  }
}