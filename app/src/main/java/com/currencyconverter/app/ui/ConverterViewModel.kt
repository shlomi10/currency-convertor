package com.currencyconverter.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.currencyconverter.app.data.CurrencyCatalog
import com.currencyconverter.app.data.PrefsStore
import com.currencyconverter.app.data.RatesRepository
import com.currencyconverter.app.widget.RatesWidgets
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ConversionRow(
    val code: String,
    val name: String,
    val flag: String,
    val symbol: String,
    val converted: Double,
    val rate: Double,
    val source: String?
)

data class ConverterUiState(
    val amountInput: String = "100",
    val baseCurrency: String = "ILS",
    val selectedTargets: Set<String> = setOf("USD", "EUR", "GBP"),
    val rates: Map<String, Double> = emptyMap(),
    val rateSources: Map<String, String> = emptyMap(),
    val marketSession: String? = null,
    val lastUpdatedMillis: Long? = null,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val pickerQuery: String = "",
    val language: AppLanguage = AppLanguage.HE
) {
    val texts: UiText get() = language.texts()
    val localeTag: String get() = if (language == AppLanguage.EN) "en" else "he"

    val amount: Double?
        get() = parseAmount(amountInput)

    val rows: List<ConversionRow>
        get() {
            val value = amount ?: return emptyList()
            val lang = localeTag
            return selectedTargets
                .filter { it != baseCurrency }
                .mapNotNull { code ->
                    val rate = rates[code] ?: return@mapNotNull null
                    val info = CurrencyCatalog.get(code)
                    ConversionRow(
                        code = info.code,
                        name = info.displayName(lang),
                        flag = info.flag,
                        symbol = info.symbol,
                        converted = value * rate,
                        rate = rate,
                        source = rateSources[code]
                    )
                }
                .sortedBy { it.code }
        }
}

class ConverterViewModel(
    private val repository: RatesRepository,
    private val prefs: PrefsStore,
    private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(
        ConverterUiState(
            amountInput = prefs.loadAmount(),
            baseCurrency = prefs.loadBase().takeIf { code -> CurrencyCatalog.all.any { it.code == code } } ?: "ILS",
            selectedTargets = prefs.loadTargets()
                .filter { code -> CurrencyCatalog.all.any { it.code == code } }
                .toSet()
                .ifEmpty { setOf("USD", "EUR", "GBP") },
            language = if (prefs.loadLanguage() == "en") AppLanguage.EN else AppLanguage.HE,
            rates = prefs.loadRates(),
            lastUpdatedMillis = prefs.loadRatesAt()
        )
    )
    val state: StateFlow<ConverterUiState> = _state

    private var refreshJob: Job? = null

    fun onAmountChange(value: String) {
        val filtered = value.filter { it.isDigit() || it == '.' || it == ',' }
        _state.update { it.copy(amountInput = filtered) }
        persist()
    }

    fun onBaseChange(code: String) {
        val selected = (_state.value.selectedTargets + _state.value.baseCurrency - code).toSet()
        _state.update {
            it.copy(
                baseCurrency = code,
                selectedTargets = selected.ifEmpty { setOf("USD", "EUR") }
            )
        }
        persist()
        refreshNow()
    }

    fun toggleTarget(code: String) {
        if (code == _state.value.baseCurrency) return
        _state.update { current ->
            val next = current.selectedTargets.toMutableSet()
            if (!next.add(code)) {
                next.remove(code)
            }
            current.copy(selectedTargets = next)
        }
        persist()
    }

    fun removeTarget(code: String) {
        _state.update { it.copy(selectedTargets = it.selectedTargets - code) }
        persist()
    }

    fun toggleLanguage() {
        _state.update {
            it.copy(language = if (it.language == AppLanguage.HE) AppLanguage.EN else AppLanguage.HE)
        }
        persist()
    }

    fun onPickerQuery(query: String) {
        _state.update { it.copy(pickerQuery = query) }
    }

    fun onForeground() {
        if (refreshJob?.isActive == true) return
        startAutoRefresh()
    }

    fun onBackground() {
        refreshJob?.cancel()
        refreshJob = null
    }

    fun refreshNow() {
        refreshJob?.cancel()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        refreshJob = viewModelScope.launch {
            while (isActive) {
                loadRates()
                delay(60_000)
            }
        }
    }

    private suspend fun loadRates() {
        val base = _state.value.baseCurrency
        val texts = _state.value.texts
        _state.update { it.copy(isRefreshing = true, error = null) }
        try {
            val snapshot = repository.fetchRates(base)
            if (base != _state.value.baseCurrency) return
            prefs.saveRates(snapshot.rates, snapshot.fetchedAtMillis)
            _state.update {
                it.copy(
                    rates = snapshot.rates,
                    rateSources = snapshot.sources,
                    marketSession = snapshot.marketSession,
                    lastUpdatedMillis = snapshot.fetchedAtMillis,
                    isRefreshing = false,
                    error = null
                )
            }
            RatesWidgets.updateAll(appContext)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val message = e.message.orEmpty()
            val error = if (message.startsWith("network")) {
                val code = message.substringAfter(":", "")
                if (code.isBlank()) texts.networkError else "${texts.networkError} ($code)"
            } else {
                texts.genericError
            }
            _state.update {
                it.copy(
                    isRefreshing = false,
                    error = error
                )
            }
        }
    }

    private fun persist() {
        val current = _state.value
        prefs.save(
            current.amountInput,
            current.baseCurrency,
            current.selectedTargets,
            if (current.language == AppLanguage.EN) "en" else "he"
        )
        viewModelScope.launch {
            RatesWidgets.updateAll(appContext)
        }
    }
}

class ConverterViewModelFactory(
    private val repository: RatesRepository,
    private val prefs: PrefsStore,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConverterViewModel(repository, prefs, appContext) as T
    }
}

fun parseAmount(raw: String): Double? {
    val s = raw.trim().replace(" ", "")
    if (s.isEmpty()) return null
    val normalized = when {
        s.contains(',') && s.contains('.') -> s.replace(",", "")
        s.contains(',') -> s.replace(',', '.')
        else -> s
    }
    return normalized.toDoubleOrNull()
}
