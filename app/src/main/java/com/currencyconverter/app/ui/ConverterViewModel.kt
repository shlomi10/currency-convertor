package com.currencyconverter.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.currencyconverter.app.data.CurrencyCatalog
import com.currencyconverter.app.data.PrefsStore
import com.currencyconverter.app.data.RatesRepository
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
    val nameHe: String,
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
    val pickerOpen: Boolean = false,
    val pickerQuery: String = ""
) {
    val amount: Double?
        get() = parseAmount(amountInput)

    val rows: List<ConversionRow>
        get() {
            val value = amount ?: return emptyList()
            return selectedTargets
                .filter { it != baseCurrency }
                .mapNotNull { code ->
                    val rate = rates[code] ?: return@mapNotNull null
                    val info = CurrencyCatalog.get(code)
                    ConversionRow(
                        code = info.code,
                        nameHe = info.nameHe,
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
    private val prefs: PrefsStore
) : ViewModel() {

    private val _state = MutableStateFlow(
        ConverterUiState(
            amountInput = prefs.loadAmount(),
            baseCurrency = prefs.loadBase().takeIf { code -> CurrencyCatalog.all.any { it.code == code } } ?: "ILS",
            selectedTargets = prefs.loadTargets()
                .filter { code -> CurrencyCatalog.all.any { it.code == code } }
                .toSet()
                .ifEmpty { setOf("USD", "EUR", "GBP") }
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

    fun setPickerOpen(open: Boolean) {
        _state.update { it.copy(pickerOpen = open, pickerQuery = if (open) it.pickerQuery else "") }
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
        _state.update { it.copy(isRefreshing = true, error = null) }
        try {
            val snapshot = repository.fetchRates(base)
            if (base != _state.value.baseCurrency) return
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isRefreshing = false,
                    error = e.message ?: "לא ניתן לעדכן שערים כרגע"
                )
            }
        }
    }

    private fun persist() {
        val current = _state.value
        prefs.save(current.amountInput, current.baseCurrency, current.selectedTargets)
    }
}

class ConverterViewModelFactory(
    private val repository: RatesRepository,
    private val prefs: PrefsStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConverterViewModel(repository, prefs) as T
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
