package com.currencyconverter.app.data

data class CurrencyInfo(
    val code: String,
    val nameHe: String,
    val flag: String,
    val symbol: String
)

object CurrencyCatalog {
    val all: List<CurrencyInfo> = listOf(
        CurrencyInfo("ILS", "שקל חדש", "🇮🇱", "₪"),
        CurrencyInfo("USD", "דולר אמריקאי", "🇺🇸", "$"),
        CurrencyInfo("EUR", "אירו", "🇪🇺", "€"),
        CurrencyInfo("GBP", "לירה שטרלינג", "🇬🇧", "£"),
        CurrencyInfo("JPY", "ין יפני", "🇯🇵", "¥"),
        CurrencyInfo("CHF", "פרנק שוויצרי", "🇨🇭", "CHF"),
        CurrencyInfo("CAD", "דולר קנדי", "🇨🇦", "C$"),
        CurrencyInfo("AUD", "דולר אוסטרלי", "🇦🇺", "A$"),
        CurrencyInfo("CNY", "יואן סיני", "🇨🇳", "¥"),
        CurrencyInfo("HKD", "דולר הונג קונג", "🇭🇰", "HK$"),
        CurrencyInfo("SGD", "דולר סינגפורי", "🇸🇬", "S$"),
        CurrencyInfo("NZD", "דולר ניו־זילנדי", "🇳🇿", "NZ$"),
        CurrencyInfo("INR", "רופי הודי", "🇮🇳", "₹"),
        CurrencyInfo("KRW", "וון דרום קוריאני", "🇰🇷", "₩"),
        CurrencyInfo("TRY", "לירה טורקית", "🇹🇷", "₺"),
        CurrencyInfo("BRL", "ריאל ברזילאי", "🇧🇷", "R$"),
        CurrencyInfo("MXN", "פסו מקסיקני", "🇲🇽", "MX$"),
        CurrencyInfo("ZAR", "ראנד דרום אפריקאי", "🇿🇦", "R"),
        CurrencyInfo("SEK", "כתר שוודי", "🇸🇪", "kr"),
        CurrencyInfo("NOK", "כתר נורווגי", "🇳🇴", "kr"),
        CurrencyInfo("DKK", "כתר דני", "🇩🇰", "kr"),
        CurrencyInfo("PLN", "זלוטי פולני", "🇵🇱", "zł"),
        CurrencyInfo("CZK", "כתר צ'כי", "🇨🇿", "Kč"),
        CurrencyInfo("HUF", "פורינט הונגרי", "🇭🇺", "Ft"),
        CurrencyInfo("RON", "לאו רומני", "🇷🇴", "lei"),
        CurrencyInfo("BGN", "לב בולגרי", "🇧🇬", "лв"),
        CurrencyInfo("ISK", "כתר איסלנדי", "🇮🇸", "kr"),
        CurrencyInfo("IDR", "רופיה אינדונזית", "🇮🇩", "Rp"),
        CurrencyInfo("MYR", "רינגיט מלזי", "🇲🇾", "RM"),
        CurrencyInfo("PHP", "פסו פיליפיני", "🇵🇭", "₱"),
        CurrencyInfo("THB", "בהט תאילנדי", "🇹🇭", "฿")
    )

    private val byCode = all.associateBy { it.code }

    fun get(code: String): CurrencyInfo {
        return byCode[code.uppercase()]
            ?: CurrencyInfo(code.uppercase(), code.uppercase(), "💱", code.uppercase())
    }
}
