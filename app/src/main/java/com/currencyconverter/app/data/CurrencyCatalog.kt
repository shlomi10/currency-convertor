package com.currencyconverter.app.data

data class CurrencyInfo(
    val code: String,
    val nameHe: String,
    val nameEn: String,
    val flag: String,
    val symbol: String
) {
    fun displayName(language: String): String = if (language == "en") nameEn else nameHe
}

object CurrencyCatalog {
    val all: List<CurrencyInfo> = listOf(
        CurrencyInfo("ILS", "שקל חדש", "Israeli Shekel", "🇮🇱", "₪"),
        CurrencyInfo("USD", "דולר אמריקאי", "US Dollar", "🇺🇸", "$"),
        CurrencyInfo("EUR", "אירו", "Euro", "🇪🇺", "€"),
        CurrencyInfo("GBP", "לירה שטרלינג", "British Pound", "🇬🇧", "£"),
        CurrencyInfo("JPY", "ין יפני", "Japanese Yen", "🇯🇵", "¥"),
        CurrencyInfo("CHF", "פרנק שוויצרי", "Swiss Franc", "🇨🇭", "CHF"),
        CurrencyInfo("CAD", "דולר קנדי", "Canadian Dollar", "🇨🇦", "C$"),
        CurrencyInfo("AUD", "דולר אוסטרלי", "Australian Dollar", "🇦🇺", "A$"),
        CurrencyInfo("CNY", "יואן סיני", "Chinese Yuan", "🇨🇳", "¥"),
        CurrencyInfo("HKD", "דולר הונג קונג", "Hong Kong Dollar", "🇭🇰", "HK$"),
        CurrencyInfo("SGD", "דולר סינגפורי", "Singapore Dollar", "🇸🇬", "S$"),
        CurrencyInfo("NZD", "דולר ניו־זילנדי", "New Zealand Dollar", "🇳🇿", "NZ$"),
        CurrencyInfo("INR", "רופי הודי", "Indian Rupee", "🇮🇳", "₹"),
        CurrencyInfo("KRW", "וון דרום קוריאני", "South Korean Won", "🇰🇷", "₩"),
        CurrencyInfo("TRY", "לירה טורקית", "Turkish Lira", "🇹🇷", "₺"),
        CurrencyInfo("BRL", "ריאל ברזילאי", "Brazilian Real", "🇧🇷", "R$"),
        CurrencyInfo("MXN", "פסו מקסיקני", "Mexican Peso", "🇲🇽", "MX$"),
        CurrencyInfo("ZAR", "ראנד דרום אפריקאי", "South African Rand", "🇿🇦", "R"),
        CurrencyInfo("SEK", "כתר שוודי", "Swedish Krona", "🇸🇪", "kr"),
        CurrencyInfo("NOK", "כתר נורווגי", "Norwegian Krone", "🇳🇴", "kr"),
        CurrencyInfo("DKK", "כתר דני", "Danish Krone", "🇩🇰", "kr"),
        CurrencyInfo("PLN", "זלוטי פולני", "Polish Zloty", "🇵🇱", "zł"),
        CurrencyInfo("CZK", "כתר צ'כי", "Czech Koruna", "🇨🇿", "Kč"),
        CurrencyInfo("HUF", "פורינט הונגרי", "Hungarian Forint", "🇭🇺", "Ft"),
        CurrencyInfo("RON", "לאו רומני", "Romanian Leu", "🇷🇴", "lei"),
        CurrencyInfo("BGN", "לב בולגרי", "Bulgarian Lev", "🇧🇬", "лв"),
        CurrencyInfo("ISK", "כתר איסלנדי", "Icelandic Krona", "🇮🇸", "kr"),
        CurrencyInfo("IDR", "רופיה אינדונזית", "Indonesian Rupiah", "🇮🇩", "Rp"),
        CurrencyInfo("MYR", "רינגיט מלזי", "Malaysian Ringgit", "🇲🇾", "RM"),
        CurrencyInfo("PHP", "פסו פיליפיני", "Philippine Peso", "🇵🇭", "₱"),
        CurrencyInfo("THB", "בהט תאילנדי", "Thai Baht", "🇹🇭", "฿")
    )

    private val byCode = all.associateBy { it.code }

    fun get(code: String): CurrencyInfo {
        return byCode[code.uppercase()]
            ?: CurrencyInfo(code.uppercase(), code.uppercase(), code.uppercase(), "💱", code.uppercase())
    }
}
