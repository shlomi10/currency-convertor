package com.currencyconverter.app.widget

import android.content.Context
import com.currencyconverter.app.data.CurrencyCatalog
import com.currencyconverter.app.data.PrefsStore
import com.currencyconverter.app.ui.parseAmount
import java.text.NumberFormat
import java.util.Locale

data class WidgetRow(
    val code: String,
    val flag: String,
    val value: String
)

data class WidgetSnapshot(
    val title: String,
    val rows: List<WidgetRow>,
    val empty: String
)

object WidgetData {
    fun load(context: Context): WidgetSnapshot {
        val prefs = PrefsStore(context)
        val language = prefs.loadLanguage()
        val locale = if (language == "en") Locale.US else Locale("he", "IL")
        val amountInput = prefs.loadAmount()
        val amount = parseAmount(amountInput)
        val base = prefs.loadBase()
        val baseInfo = CurrencyCatalog.get(base)
        val rates = prefs.loadRates()
        val title = if (amount == null) {
            baseInfo.code
        } else {
            "${formatNumber(amount, locale)} ${baseInfo.flag} ${baseInfo.code}"
        }
        val empty = if (language == "en") "Open the app to load rates" else "פתחו את האפליקציה לטעינת שערים"
        val rows = if (amount == null) {
            emptyList()
        } else {
            prefs.loadTargets()
                .filter { it != base }
                .mapNotNull { code ->
                    val rate = rates[code] ?: return@mapNotNull null
                    val info = CurrencyCatalog.get(code)
                    WidgetRow(
                        code = info.code,
                        flag = info.flag,
                        value = formatNumber(amount * rate, locale)
                    )
                }
                .sortedBy { it.code }
        }
        return WidgetSnapshot(title = title, rows = rows, empty = empty)
    }

    private fun formatNumber(value: Double, locale: Locale): String {
        val format = NumberFormat.getNumberInstance(locale)
        format.maximumFractionDigits = if (value < 1) 4 else 2
        format.minimumFractionDigits = 2
        return format.format(value)
    }
}
