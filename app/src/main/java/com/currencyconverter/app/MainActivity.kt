package com.currencyconverter.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.currencyconverter.app.data.PrefsStore
import com.currencyconverter.app.data.RatesRepository
import com.currencyconverter.app.ui.AppLanguage
import com.currencyconverter.app.ui.ConverterScreen
import com.currencyconverter.app.ui.ConverterViewModel
import com.currencyconverter.app.ui.ConverterViewModelFactory
import com.currencyconverter.app.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = ConverterViewModelFactory(
            RatesRepository(),
            PrefsStore(applicationContext),
            applicationContext
        )
        setContent {
            CurrencyConverterTheme {
                val vm: ConverterViewModel = viewModel(factory = factory)
                val state by vm.state.collectAsStateWithLifecycle()
                val direction = if (state.language == AppLanguage.EN) {
                    LayoutDirection.Ltr
                } else {
                    LayoutDirection.Rtl
                }
                CompositionLocalProvider(LocalLayoutDirection provides direction) {
                    ConverterScreen(vm)
                }
            }
        }
    }
}
