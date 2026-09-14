# Currency Converter / ממיר מטבעות

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/exchangerate.dev-0D7377?style=for-the-badge&logo=cashapp&logoColor=white" alt="exchangerate.dev" />
</p>

<p align="center">
  <img src="https://img.shields.io/github/actions/workflow/status/shlomi10/currency-convertor/build-apk.yml?style=flat-square&label=APK%20Build&logo=githubactions&logoColor=white" alt="APK Build" />
  <img src="https://img.shields.io/badge/Language-Hebrew%20%7C%20English-14919B?style=flat-square&logo=googletranslate&logoColor=white" alt="Hebrew and English" />
  <img src="https://img.shields.io/badge/UI-RTL%20%2B%20LTR-E4A11B?style=flat-square&logo=materialdesign&logoColor=white" alt="RTL and LTR" />
  <img src="https://img.shields.io/badge/Min%20SDK-26-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Min SDK 26" />
</p>

Android app that converts one amount into several currencies at once, with live rates. Switch the interface between **Hebrew** and **English** from the top bar.

אפליקציית אנדרואיד להמרת סכום למספר מטבעות במקביל, עם שערי מטבע חיים. אפשר לעבור בין **עברית** ל**אנגלית** מסרגל העליון.

---

## ✨ Features / תכונות

| | English | עברית |
| --- | --- | --- |
| 💱 | Convert into multiple currencies at once | בחירת כמה מטבעות יעד בבת אחת |
| 🟢 | Live rates from [exchangerate.dev](https://exchangerate.dev/) | שערי מטבע חיים מ־[exchangerate.dev](https://exchangerate.dev/) |
| 🌐 | Hebrew (RTL) and English (LTR) UI | ממשק עברית (RTL) ואנגלית (LTR) |
| 🔄 | Refresh on open, then every minute while the screen is open | רענון בכניסה, ואז כל דקה כל עוד המסך פתוח |
| 💾 | Remembers amount, currencies, and language | שומר סכום, מטבעות ושפת ממשק |
| 🌙 | Follows system light / dark theme | מצב בהיר וכהה לפי המערכת |

---

## 📲 Install / התקנה

No Google Play required. Every push to `main` builds an APK with GitHub Actions.

אין צורך ב־Google Play. בכל דחיפה ל־`main` נבנה APK ב־GitHub Actions.

1. Open **[Actions](https://github.com/shlomi10/currency-convertor/actions)**
2. Open the latest green **Build APK** run
3. Download the `currency-converter` artifact (`app-debug.apk`)
4. Transfer it to your phone and install (allow unknown sources)

On the device the launcher name is **ממיר מטבעות**. Inside the app, tap **English** / **עברית** to switch the interface.

---

## 🛠️ Development / פיתוח

Kotlin + Jetpack Compose. Local requirements: Android Studio, JDK 17, Android SDK 35.

```bash
./gradlew assembleDebug
```

APK output:

`app/build/outputs/apk/debug/app-debug.apk`

---

## 📈 Rates / שערים

Data comes from [exchangerate.dev](https://exchangerate.dev/). Major currencies update during market hours; some less-liquid currencies may use a daily reference rate. Rates are indicative only, not for settlement.

הנתונים מגיעים מ־[exchangerate.dev](https://exchangerate.dev/). מטבעות מרכזיים מתעדכנים תוך־יום; חלק מהמטבעות הפחות נזילים עשויים להישען על שער ייחוס יומי. השערים לידיעה בלבד, לא לסליקה.
