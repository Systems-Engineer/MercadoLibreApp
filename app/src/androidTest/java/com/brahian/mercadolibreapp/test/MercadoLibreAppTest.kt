package com.brahian.mercadolibreapp.test

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.brahian.mercadolibreapp.R
import com.brahian.mercadolibreapp.di.module.BaseUrlModule
import com.brahian.mercadolibreapp.dispatcher.DispatcherRule
import com.brahian.mercadolibreapp.dispatcher.MockServerDispatcher
import com.brahian.mercadolibreapp.screen.DetailScreen
import com.brahian.mercadolibreapp.screen.MainScreen
import com.brahian.mercadolibreapp.view.activity.MainActivity
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

/**
 * Instrumentation tests for both MainActivity and DetailActivity
 */
@UninstallModules(BaseUrlModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MercadoLibreAppTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  private lateinit var mockServer : MockWebServer

  private lateinit var scenario: ActivityScenario<MainActivity>

  private lateinit var context: Context

  @Module
  @InstallIn(SingletonComponent::class)
  class FakeBaseUrlModule {

    @Provides
    @Singleton
    fun provideUrl(): String = "http://localhost:8080/"
  }

  /**
   * mocking the endpoint responses
   */
  private fun setDispatcher(dispatcherTag : DispatcherTag) {
    mockServer.dispatcher =
      when (dispatcherTag) {
        DispatcherTag.MAIN_SUCCESS -> {
          MockServerDispatcher().RequestDispatcher(listOf(
            DispatcherRule(
              "/sites/MCO/search?q=Tecnologia",
              200,
              "tecnologia_results.json"
            ),
            DispatcherRule(
              "/sites/MCO/search?q=gatos",
              200,
              "gatos_results.json"
            ),
            DispatcherRule(
              "/users/556503088",
              200,
              "seller.json"
            )
          ))
        }
        DispatcherTag.MAIN_FAIL -> {
          MockServerDispatcher().ErrorDispatcher()
        }
        DispatcherTag.DETAIL_FAIL -> {
          MockServerDispatcher().RequestDispatcher(listOf(
            DispatcherRule(
              "/sites/MCO/search?q=Tecnologia",
              200,
              "tecnologia_results.json"
            ),
            DispatcherRule(
              "/users/556503088",
              400,
              "seller.json"
            )
          ))
        }
      }
  }

  @Before
  fun setUp() {
    mockServer = MockWebServer()
    mockServer.start(8080)
    scenario = ActivityScenario.launch(MainActivity::class.java)
    context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @After
  fun tearDown() {
    mockServer.shutdown()
  }

  @Test
  fun testMainActivityLoads() {
    setDispatcher(DispatcherTag.MAIN_SUCCESS)
    MainScreen.waitForScreen()
  }

  @Test
  fun testMainActivityLoadsAndSearchIsSuccessful() {
    setDispatcher(DispatcherTag.MAIN_SUCCESS)
    MainScreen.waitForScreen()
    MainScreen.searchForProduct("gatos")
    MainScreen.waitForScreen()
  }

  @Test
  fun testDetailActivityLoads() {
    setDispatcher(DispatcherTag.MAIN_SUCCESS)
    MainScreen.waitForScreen()
    MainScreen.tapOnProduct(0)
    DetailScreen.waitForScreen()
    DetailScreen.verifyDetails("New | 1 sold", "Parlante LG Xboom Go Pl7 Portátil Con Bluetooth Negra ", "Free Shipping | Accepts MercadoPago")
    DetailScreen.verifySellerLoads("HOMESOLUTIONSBQ", "gold".capitalize())
  }

  @Test
  fun testMainActivityFailsToLoad() {
    setDispatcher(DispatcherTag.MAIN_FAIL)
    MainScreen.waitForScreenToFail(context.getString(R.string.error_message))
  }

  @Test
  fun testDetailActivityFailsToLoadSellerInfo() {
    setDispatcher(DispatcherTag.DETAIL_FAIL)
    MainScreen.waitForScreen()
    MainScreen.tapOnProduct(0)
    DetailScreen.waitForScreen()
    DetailScreen.verifyDetails("New | 1 sold", "Parlante LG Xboom Go Pl7 Portátil Con Bluetooth Negra ", "Free Shipping | Accepts MercadoPago")
    DetailScreen.verifySellerDidNotLoad()
  }

}

enum class DispatcherTag {
  MAIN_SUCCESS,
  MAIN_FAIL,
  DETAIL_FAIL
}
