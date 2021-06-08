package com.brahian.mercadolibreapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.brahian.mercadolibreapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

abstract class BaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val archComponentRule = InstantTaskExecutorRule()

}