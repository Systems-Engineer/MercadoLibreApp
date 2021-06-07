package com.brahian.mercadolibreapp.test

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.brahian.mercadolibreapp.di.BaseUrlModule
import com.brahian.mercadolibreapp.dispatcher.DispatcherRule
import com.brahian.mercadolibreapp.dispatcher.MockServerDispatcher
import com.brahian.mercadolibreapp.screen.MainScreen
import com.brahian.mercadolibreapp.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@UninstallModules(BaseUrlModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  private lateinit var mockServer : MockWebServer

  private lateinit var scenario: ActivityScenario<MainActivity>

  @Module
  @InstallIn(SingletonComponent::class)
  class FakeBaseUrlModule {

    @Provides
    @Singleton
    fun provideUrl(): String = "http://localhost:8080/"
  }

  private fun composeMockServer() : MockWebServer {
    return MockWebServer().apply {
      dispatcher = MockServerDispatcher().RequestDispatcher(listOf(
        DispatcherRule(
          "/sites/MCO/search?q=Tecnologia",
          200,
          "results.json"
        )
      ))
    }
  }

  @Before
  fun setUp() {
    mockServer = composeMockServer()
    mockServer.start(8080)
  }

  @After
  fun tearDown() {
    mockServer.shutdown()
  }

  @Test
  fun testMainActivityLoads() {
    scenario = ActivityScenario.launch(MainActivity::class.java)
    MainScreen.waitForScreen()
  }
}
