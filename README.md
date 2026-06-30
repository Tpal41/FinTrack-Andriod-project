# 💎 FinTrack - Personal Finance Manager

<div align="center">

![FinTrack Logo](https://img.shields.io/badge/FinTrack-v1.0.0-00e676?style=for-the-badge&logo=android)
[![Android](https://img.shields.io/badge/Android-26+-3DDC84?style=for-the-badge&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-FFC857?style=for-the-badge)](LICENSE)

**Apni finance manage karo ek hi jagah — simple, fast aur secure**

[🎯 Features](#-features) • [📸 Screenshots](#-screenshots) • [🚀 Getting Started](#-getting-started) • [👤 Author](#-author)

</div>

---

## 🌟 Overview

**FinTrack** ek personal finance tracking app hai jisko maine khud banaya hai apni zaroorat ke hisaab se. Ismein aap apne daily expenses, income, budgets aur savings goals sab ek jagah track kar sakte ho. Design dark theme mein hai jo aankhon ko aaram deta hai aur use karna bhi bahut easy hai.

Ye app maine isliye banaya kyunki mujhe koi simple aur fast finance app nahi mili jo mere kaam ki ho — toh socha khud hi bana lete hain! 😄

### ✨ Kyu FinTrack?

- 🎨 **Clean Dark UI** - Aankhon ko chain, dimaag ko sukoon
- 🔐 **Secure** - Biometric lock + encrypted data
- 📊 **Smart Charts** - Dekho paisa kahan ja raha hai
- 💰 **Budget Control** - Spending pe nazar rakho
- 🌙 **Dark Mode** - Hamesha dark, hamesha premium
- 📱 **Fast & Smooth** - Jetpack Compose se bana hai

---

## 🎯 Features

### 💳 Transaction Management
- ✅ Income aur Expense dono track karo
- ✅ Category wise organize (Food, Transport, etc.)
- ✅ Search aur filter karo
- ✅ Date wise grouping

### 📊 Budget Tracking
- ✅ Har category ka monthly budget set karo
- ✅ Real-time spending monitor
- ✅ Visual progress bar
- ✅ Overspending alert (laal ho jaata hai)

### 🎯 Savings Goals
- ✅ Kisi bhi cheez ke liye goal banao
- ✅ Target date set karo
- ✅ Progress track karo

### 🏠 Dashboard
- ✅ Total balance ek nazar mein
- ✅ Smart spending insights
- ✅ Category distribution
- ✅ Recent transactions

### 📈 Analytics
- ✅ Donut chart - category wise breakdown
- ✅ Monthly comparison bar chart
- ✅ Income vs Expense comparison

### 🔄 Recurring
- ✅ EMI, subscription, rent sab set karo
- ✅ Automatic tracking

### ⚙️ Settings
- ✅ Biometric lock
- ✅ Notifications
- ✅ Data export (CSV)
- ✅ Clear data option

---

## 📸 Screenshots

<div align="center">

| Dashboard | Transactions | Analytics |
|-----------|-------------|-----------|
| Home screen with balance | Audit ledger with filters | Donut + bar charts |

| Budget | Goals | Settings |
|--------|-------|----------|
| Monthly budget tracker | Savings goal tracker | App preferences |

</div>

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog ya newer
- **JDK** 17+
- **Android SDK** API 35

### Installation

1. **Clone karo**
```bash
git clone https://github.com/Tpal41/FinTrack-Andriod-project.git
cd FinTrack-Andriod-project
```

2. **Android Studio mein kholo**
   - "Open an Existing Project" select karo
   - Folder navigate karo
   - OK karo

3. **Gradle Sync hone do**
   - Pehli baar 5-10 min lag sakte hain

4. **Run karo**
   - Device ya emulator connect karo
   - Play button dabao ▶️

### APK Build karna

```bash
./gradlew assembleDebug
```
APK milega: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🏗️ Tech Stack

| Technology | Use |
|-----------|-----|
| **Kotlin** | Main language |
| **Jetpack Compose** | UI banane ke liye |
| **Room Database** | Local data store |
| **MVVM + Clean Architecture** | Code structure |
| **Hilt** | Dependency injection |
| **WorkManager** | Background tasks |
| **Biometric API** | Fingerprint lock |
| **Coroutines + Flow** | Async operations |

---

## 📁 Project Structure

```
com.fintrack.app/
├── core/                    # Utilities & Design system
│   ├── designsystem/        # UI components & theming
│   ├── navigation/          # Navigation setup
│   └── util/                # Helper functions
├── data/                    # Data layer
│   ├── local/               # Room database, DAOs
│   └── repository/          # Repository implementations
├── domain/                  # Business logic
│   ├── model/               # Data models
│   └── usecase/             # Use cases
├── presentation/            # UI screens
│   ├── dashboard/           # Home screen
│   ├── transaction/         # Transactions screen
│   ├── budget/              # Budget screen
│   ├── goals/               # Goals screen
│   ├── analytics/           # Analytics screen
│   └── settings/            # Settings screen
└── di/                      # Dependency injection modules
```

---

## 🎨 Design

**Colors:**
- Primary Green: `#00E676`
- Background: `#0D0D0D`
- Card: `#1C1C1E`
- Red (Expense): `#FF3B30`

Pura dark theme hai. Maine intentionally light mode nahi rakha kyunki dark mode zyada comfortable lagta hai jab raat ko accounts dekhte hain 😅

---

## 🛣️ Roadmap

- [ ] Cloud sync with Firebase
- [ ] Bank account integration
- [ ] Receipt scan with camera
- [ ] Widget support
- [ ] PDF reports
- [ ] Multiple accounts

---

## 👤 Author

**Thakurpal Rajput**
- 📧 Email: [thakurpalrajput45@gmail.com](mailto:thakurpalrajput45@gmail.com)
- 🐙 GitHub: [@Tpal41](https://github.com/Tpal41)

---

## 📄 License

MIT License — freely use karo, sirf credit dena mat bhoolna!

```
Copyright (c) 2024 Thakurpal Rajput
```

---

## 🙏 Thanks

- [Material Design 3](https://m3.material.io/) for design guidelines
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for amazing UI framework
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for charts

---

## 📞 Support

Koi problem ho toh:
- 📧 **thakurpalrajput45@gmail.com** pe mail karo
- Ya GitHub pe Issue kholo

Agar project pasand aaya to ⭐ star zaroor dena — motivate rehta hoon isse! 😊

---

<div align="center">

**Banaya with ❤️ by Thakurpal Rajput**

[⬆ Back to Top](#-fintrack---personal-finance-manager)

</div>
