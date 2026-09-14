package com.currencyconverter.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.currencyconverter.app.data.CurrencyCatalog
import com.currencyconverter.app.data.CurrencyInfo
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val texts = state.texts
    val colors = MaterialTheme.colorScheme
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.onForeground()
                Lifecycle.Event.ON_STOP -> viewModel.onBackground()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.onBackground()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(texts.appTitle, fontWeight = FontWeight.Bold)
                        Text(
                            text = statusText(state),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = viewModel::toggleLanguage) {
                        Icon(
                            Icons.Outlined.Language,
                            contentDescription = texts.languageToggle,
                            tint = colors.onPrimary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(texts.languageToggle, color = colors.onPrimary)
                    }
                    if (state.isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(22.dp),
                            strokeWidth = 2.dp,
                            color = colors.onPrimary
                        )
                    } else {
                        IconButton(onClick = viewModel::refreshNow) {
                            Icon(Icons.Outlined.Refresh, contentDescription = texts.refresh)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.primary,
                    titleContentColor = colors.onPrimary,
                    actionIconContentColor = colors.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding)
        ) {
            AnimatedVisibility(visible = state.isRefreshing && state.rates.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    AmountCard(
                        state = state,
                        onAmountChange = viewModel::onAmountChange,
                        onBaseClick = { viewModel.setPickerOpen(true) }
                    )
                }

                item {
                    TargetsCard(
                        state = state,
                        onAddClick = { viewModel.setPickerOpen(true) },
                        onToggle = viewModel::toggleTarget
                    )
                }

                if (state.isRefreshing && state.rates.isEmpty()) {
                    item {
                        HintCard(texts.loadingRates)
                    }
                }

                if (state.error != null && state.rates.isEmpty()) {
                    item {
                        ErrorCard(
                            message = state.error.orEmpty(),
                            retryLabel = texts.retry,
                            onRetry = viewModel::refreshNow
                        )
                    }
                }

                if (state.amount == null) {
                    item {
                        HintCard(texts.enterAmount)
                    }
                } else if (state.selectedTargets.none { it != state.baseCurrency }) {
                    item {
                        HintCard(texts.pickOneCurrency)
                    }
                }

                items(state.rows, key = { it.code }) { row ->
                    ConversionCard(
                        row = row,
                        baseCode = state.baseCurrency,
                        texts = texts,
                        localeTag = state.localeTag,
                        onRemove = { viewModel.removeTarget(row.code) },
                        onSetBase = { viewModel.onBaseChange(row.code) }
                    )
                }

                item {
                    Text(
                        text = texts.attribution,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }

                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }

    if (state.pickerOpen) {
        CurrencyPickerSheet(
            state = state,
            onDismiss = { viewModel.setPickerOpen(false) },
            onQuery = viewModel::onPickerQuery,
            onToggleTarget = viewModel::toggleTarget,
            onSelectBase = viewModel::onBaseChange
        )
    }
}

@Composable
private fun AmountCard(
    state: ConverterUiState,
    onAmountChange: (String) -> Unit,
    onBaseClick: () -> Unit
) {
    val texts = state.texts
    val base = CurrencyCatalog.get(state.baseCurrency)
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(texts.amountTitle, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.amountInput,
                    onValueChange = onAmountChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    placeholder = { Text("0") }
                )
                Spacer(Modifier.width(12.dp))
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onBaseClick),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(base.flag, fontSize = 22.sp)
                        Text(base.code, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${base.flag} ${base.displayName(state.localeTag)} · ${texts.changeBaseHint}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TargetsCard(
    state: ConverterUiState,
    onAddClick: () -> Unit,
    onToggle: (String) -> Unit
) {
    val texts = state.texts
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(texts.convertTo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = onAddClick) {
                    Icon(Icons.Outlined.Add, contentDescription = texts.addCurrencies)
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.selectedTargets.filter { it != state.baseCurrency }.forEach { code ->
                    val info = CurrencyCatalog.get(code)
                    FilterChip(
                        selected = true,
                        onClick = { onToggle(code) },
                        label = { Text("${info.flag} ${info.code}") },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = texts.remove,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
                FilterChip(
                    selected = false,
                    onClick = onAddClick,
                    label = { Text(texts.add) },
                    leadingIcon = { Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
            }
        }
    }
}

@Composable
private fun ConversionCard(
    row: ConversionRow,
    baseCode: String,
    texts: UiText,
    localeTag: String,
    onRemove: () -> Unit,
    onSetBase: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(row.flag, fontSize = 28.sp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(row.name, style = MaterialTheme.typography.titleMedium)
                    Text(row.code, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = onSetBase) {
                    Icon(Icons.Outlined.SwapHoriz, contentDescription = texts.setAsBase)
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Outlined.Close, contentDescription = texts.remove)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = formatMoney(row.converted, row.code, localeTag),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 30.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "1 $baseCode = ${formatRate(row.rate, localeTag)} ${row.code} · ${sourceLabel(row.source, texts)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HintCard(text: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(text, modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun ErrorCard(message: String, retryLabel: String, onRetry: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(message, color = MaterialTheme.colorScheme.onErrorContainer)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text(retryLabel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyPickerSheet(
    state: ConverterUiState,
    onDismiss: () -> Unit,
    onQuery: (String) -> Unit,
    onToggleTarget: (String) -> Unit,
    onSelectBase: (String) -> Unit
) {
    val texts = state.texts
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val query = state.pickerQuery.trim()
    val filtered = CurrencyCatalog.all.filter { info ->
        query.isEmpty() ||
            info.code.contains(query, ignoreCase = true) ||
            info.nameHe.contains(query) ||
            info.nameEn.contains(query, ignoreCase = true)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Text(texts.pickerTitle, style = MaterialTheme.typography.titleLarge)
            Text(
                texts.pickerHint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = state.pickerQuery,
                onValueChange = onQuery,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text(texts.searchPlaceholder) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) }
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.height(460.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filtered, key = { it.code }) { info ->
                    CurrencyPickerRow(
                        info = info,
                        isBase = info.code == state.baseCurrency,
                        selected = info.code in state.selectedTargets,
                        localeTag = state.localeTag,
                        baseLabel = texts.baseCurrency,
                        onToggle = { onToggleTarget(info.code) },
                        onSelectBase = { onSelectBase(info.code) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyPickerRow(
    info: CurrencyInfo,
    isBase: Boolean,
    selected: Boolean,
    localeTag: String,
    baseLabel: String,
    onToggle: () -> Unit,
    onSelectBase: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onToggle)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onSelectBase),
            contentAlignment = Alignment.Center
        ) {
            Text(info.flag, fontSize = 22.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(info.displayName(localeTag), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium)
            Text(
                text = if (isBase) "${info.code} · $baseLabel" else info.code,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Checkbox(
            checked = selected || isBase,
            onCheckedChange = { if (!isBase) onToggle() },
            enabled = !isBase
        )
    }
}

private fun statusText(state: ConverterUiState): String {
    val texts = state.texts
    val updated = state.lastUpdatedMillis ?: return texts.loadingStatus
    val time = android.text.format.DateFormat.format("HH:mm:ss", Date(updated))
    if (state.error != null) {
        return "${texts.updateFailed}$time"
    }
    val freshness = selectedFreshness(state)
    val session = when (state.marketSession) {
        "weekend" -> texts.weekend
        "interbank_closed" -> texts.marketClosed
        else -> ""
    }
    return "${texts.updatedAt}$time · $freshness$session${texts.everyMinute}"
}

private fun selectedFreshness(state: ConverterUiState): String {
    val sources = state.selectedTargets
        .filter { it != state.baseCurrency }
        .mapNotNull { state.rateSources[it] }
    if (sources.isEmpty()) return state.texts.liveRate
    return if (sources.all { it == "live" }) state.texts.liveRate else state.texts.mixedRate
}

private fun sourceLabel(source: String?, texts: UiText): String {
    return when (source) {
        "live" -> texts.sourceLive
        "ecb_daily" -> texts.sourceEcb
        "fred_daily" -> texts.sourceFred
        else -> texts.sourceLive
    }
}

private fun formatMoney(amount: Double, code: String, localeTag: String): String {
    val locale = if (localeTag == "en") Locale.US else Locale("he", "IL")
    return try {
        val format = NumberFormat.getCurrencyInstance(locale)
        format.currency = Currency.getInstance(code)
        format.maximumFractionDigits = if (amount < 1) 4 else 2
        format.minimumFractionDigits = if (amount < 1) 2 else 2
        format.format(amount)
    } catch (_: Exception) {
        val number = NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = if (amount < 1) 4 else 2
            minimumFractionDigits = 2
        }
        "${number.format(amount)} $code"
    }
}

private fun formatRate(rate: Double, localeTag: String): String {
    val locale = if (localeTag == "en") Locale.US else Locale("he", "IL")
    val format = NumberFormat.getNumberInstance(locale)
    format.maximumFractionDigits = if (rate < 1) 6 else 4
    format.minimumFractionDigits = 2
    return format.format(rate)
}
