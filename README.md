<p align="center">
  <img src="docs/app-icon.svg" alt="Currency Converter" width="120" height="120" />
</p>

<h1 align="center">Currency Converter</h1>

<p align="center"><strong>Created by Shlomi</strong></p>

<p align="center">
  <a href="https://github.com/shlomi10/currency-convertor/releases/latest">
    <img src="https://img.shields.io/github/v/release/shlomi10/currency-convertor?style=for-the-badge&logo=github&logoColor=white&color=0D7377" alt="Latest release" />
  </a>
  <a href="https://github.com/shlomi10/currency-convertor/actions/workflows/build-apk.yml">
    <img src="https://img.shields.io/github/actions/workflow/status/shlomi10/currency-convertor/build-apk.yml?style=for-the-badge&logo=githubactions&logoColor=white&label=APK%20Build" alt="APK Build" />
  </a>
  <a href="https://github.com/shlomi10">
    <img src="https://img.shields.io/badge/Author-Shlomi-14919B?style=for-the-badge&logo=github&logoColor=white" alt="Author Shlomi" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Glance%20Widgets-0D7377?style=flat-square&logo=android&logoColor=white" alt="Glance Widgets" />
  <img src="https://img.shields.io/badge/Language-Hebrew%20%7C%20English-14919B?style=flat-square&logo=googletranslate&logoColor=white" alt="Hebrew and English" />
  <img src="https://img.shields.io/badge/UI-RTL%20%2B%20LTR-E4A11B?style=flat-square&logo=materialdesign&logoColor=white" alt="RTL and LTR" />
  <img src="https://img.shields.io/badge/Min%20SDK-26-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Min SDK 26" />
  <img src="https://img.shields.io/badge/Rates-exchangerate.dev-0D7377?style=flat-square&logo=cashapp&logoColor=white" alt="exchangerate.dev" />
</p>

<p align="center">
  Convert one amount into many currencies at once, with live rates, Hebrew and English, and home-screen widgets.
</p>

---

# English

Type an amount, pick the currencies you care about, and see every conversion on one screen.

The app is **not** on Google Play. Every push to `main` builds a real APK and publishes it as a GitHub Release.

## Features

- Convert one amount into several selected currencies at the same time
- Live rates from [exchangerate.dev](https://exchangerate.dev/)
- Hebrew (RTL) and English (LTR) interface — tap **EN** / **עב** in the top bar
- Side menu to manage currencies, plus a **Manage currencies** button at the bottom
- Refresh on open, then every minute while the screen is open
- Remembers amount, base currency, selected currencies, and language
- Home-screen widgets in small, medium, and large sizes
- Version number and **Created by Shlomi** at the bottom of the app
- Light and dark theme follow the system

## Install

1. Open **[Releases](https://github.com/shlomi10/currency-convertor/releases/latest)**
2. Download the APK (`currency-convertor-v…apk`)
3. Copy it to your phone and install (allow unknown sources)

On the device the launcher name is **ממיר מטבעות**. Inside the app, tap **EN** / **עב** to switch the interface.

## Home-screen widgets

Long-press the home screen → **Widgets** → **ממיר מטבעות**.

| Size | What it shows |
| --- | --- |
| Small | The amount and 1 converted currency |
| Medium | The amount and up to 3 currencies |
| Large | The amount and up to 8 currencies |

Tap a widget to open the app. Widgets use the amount and currencies you last chose.

## Currencies

About 31 currencies, including ILS, USD, EUR, GBP, JPY, CHF, CAD, AUD, CNY, and more.

Tap a currency flag in the menu to change the **base** currency. Check the boxes to choose what to convert into.

## Rates

Data comes from [exchangerate.dev](https://exchangerate.dev/). Major currencies update during market hours. Some less-liquid currencies may use a daily reference rate.

Rates are indicative only, not for settlement.

## Development

Kotlin + Jetpack Compose + Glance widgets.

Local requirements: Android Studio, JDK 17, Android SDK 35.

```bash
./gradlew assembleDebug
```

APK output:

`app/build/outputs/apk/debug/app-debug.apk`

## Author

**Shlomi** designed, wrote, and maintains this app — UI, rates, widgets, Hebrew/English, and the GitHub APK releases.

GitHub: [shlomi10](https://github.com/shlomi10)

---

# עברית

מקלידים סכום, בוחרים מטבעות, ורואים את כל ההמרות במסך אחד.

האפליקציה **לא** ב־Google Play. בכל דחיפה ל־`main` נבנה APK אמיתי ומפורסם ב־GitHub Releases.

## תכונות

- המרת סכום אחד למספר מטבעות יעד בבת אחת
- שערי מטבע חיים מ־[exchangerate.dev](https://exchangerate.dev/)
- ממשק עברית (RTL) ואנגלית (LTR) — לחיצה על **EN** / **עב** בסרגל העליון
- תפריט צד לניהול מטבעות, וגם כפתור **נהל מטבעות** בתחתית
- רענון בכניסה, ואז כל דקה כל עוד המסך פתוח
- שומר סכום, מטבע מקור, מטבעות שנבחרו ושפת ממשק
- ווידג'טים למסך הבית בשלושה גדלים: קטן, בינוני וגדול
- מספר גרסה ו־**Created by Shlomi** בתחתית האפליקציה
- מצב בהיר וכהה לפי המערכת

## התקנה

1. פתחו **[Releases](https://github.com/shlomi10/currency-convertor/releases/latest)**
2. הורידו את ה־APK (`currency-convertor-v…apk`)
3. העבירו לטלפון והתקינו (לאפשר מקורות לא מוכרים)

במכשיר שם האפליקציה הוא **ממיר מטבעות**. בתוך האפליקציה לוחצים על **EN** / **עב** כדי להחליף שפה.

## ווידג'טים למסך הבית

לחיצה ארוכה על מסך הבית → **ווידג'טים** → **ממיר מטבעות**.

<div dir="rtl" align="right">

| גודל | מה מוצג |
| ---: | ---: |
| קטן | הסכום ומטבע מומר אחד |
| בינוני | הסכום ועד 3 מטבעות |
| גדול | הסכום ועד 8 מטבעות |

</div>

לחיצה על הווידג'ט פותחת את האפליקציה. הווידג'ט משתמש בסכום ובמטבעות שבחרתם לאחרונה.

## מטבעות

כ־31 מטבעות, כולל שקל, דולר, אירו, ליש"ט, ין, פרנק שוויצרי ועוד.

לחיצה על דגל בתפריט מחליפה את **מטבע המקור**. סימון בתיבות בוחר לאלו מטבעות להמיר.

## שערים

הנתונים מגיעים מ־[exchangerate.dev](https://exchangerate.dev/). מטבעות מרכזיים מתעדכנים תוך־יום. חלק מהמטבעות הפחות נזילים עשויים להישען על שער ייחוס יומי.

השערים לידיעה בלבד, לא לסליקה.

## פיתוח

Kotlin + Jetpack Compose + Glance widgets.

דרישות מקומיות: Android Studio, JDK 17, Android SDK 35.

```bash
./gradlew assembleDebug
```

פלט ה־APK:

`app/build/outputs/apk/debug/app-debug.apk`

## יוצר

<p dir="rtl" align="right"><strong>Shlomi</strong> תכנן, כתב ומתחזק את האפליקציה — ממשק, שערים, ווידג'טים, עברית/אנגלית, ופרסום ה־APK ב־GitHub.</p>

GitHub: [shlomi10](https://github.com/shlomi10)
