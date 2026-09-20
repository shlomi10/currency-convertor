package com.currencyconverter.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.currencyconverter.app.MainActivity

private val Teal = Color(0xFF0D7377)
private val Cream = Color(0xFFF6F3EA)
private val Mint = Color(0xFF4ECDC4)

class SmallRatesWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = WidgetData.load(context)
        provideContent { RatesWidgetUi(context, data, maxRows = 1, compact = true) }
    }
}

class MediumRatesWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = WidgetData.load(context)
        provideContent { RatesWidgetUi(context, data, maxRows = 3, compact = false) }
    }
}

class LargeRatesWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = WidgetData.load(context)
        provideContent { RatesWidgetUi(context, data, maxRows = 8, compact = false) }
    }
}

class SmallRatesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = SmallRatesWidget()
}

class MediumRatesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = MediumRatesWidget()
}

class LargeRatesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = LargeRatesWidget()
}

object RatesWidgets {
    suspend fun updateAll(context: Context) {
        SmallRatesWidget().updateAll(context)
        MediumRatesWidget().updateAll(context)
        LargeRatesWidget().updateAll(context)
    }
}

@Composable
private fun RatesWidgetUi(context: Context, data: WidgetSnapshot, maxRows: Int, compact: Boolean) {
    val rows = data.rows.take(maxRows)
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Teal)
            .clickable(actionStartActivity(Intent(context, MainActivity::class.java)))
            .padding(if (compact) 8.dp else 12.dp)
    ) {
            Text(
                text = data.title,
                style = TextStyle(
                    color = ColorProvider(Cream),
                    fontWeight = FontWeight.Bold,
                    fontSize = if (compact) 14.sp else 16.sp
                ),
                maxLines = 1
            )
            if (rows.isEmpty()) {
                Spacer(GlanceModifier.height(6.dp))
                Text(
                    text = data.empty,
                    style = TextStyle(color = ColorProvider(Mint), fontSize = 12.sp),
                    maxLines = 2
                )
            } else {
                rows.forEach { row ->
                    Spacer(GlanceModifier.height(if (compact) 4.dp else 6.dp))
                    Text(
                        text = "${row.flag} ${row.code}  ${row.value}",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            fontSize = if (compact) 13.sp else 15.sp,
                            textAlign = TextAlign.Start
                        ),
                        maxLines = 1
                    )
                }
            }
        }
}
