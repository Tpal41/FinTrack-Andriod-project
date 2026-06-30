# 💎 FinTrack - Premium Personal Finance Manager

<div align="center">

![FinTrack Logo](https://img.shields.io/badge/FinTrack-v1.0.0-20E3A2?style=for-the-badge&logo=android)
[![Android](https://img.shields.io/badge/Android-26+-3DDC84?style=for-the-badge&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Compose-Latest-4285F4?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-FFC857?style=for-the-badge)](LICENSE)

**Precision Finance Management with Glassmorphic Design**

[📱 Download APK](#-download) • [🎯 Features](#-features) • [📸 Screenshots](#-screenshots) • [🚀 Getting Started](#-getting-started)

</div>

---

## 🌟 Overview

**FinTrack** is a modern, feature-rich personal finance management Android application built with cutting-edge technologies. It combines powerful financial tracking capabilities with a stunning glassmorphic UI design, offering users a premium experience in managing their finances.

### ✨ Why FinTrack?

- 🎨 **Beautiful Design** - Glassmorphic UI with Material 3
- 🔐 **Secure** - Biometric authentication & encrypted data storage
- 📊 **Smart Insights** - AI-powered spending analysis
- 💰 **Complete Control** - Track transactions, budgets, and goals
- 🌙 **Dark Mode** - Easy on the eyes, premium feel
- 📱 **Modern Stack** - Built with Jetpack Compose & Clean Architecture

---

## 🎯 Features

### 💳 Transaction Management
- ✅ Add income and expense transactions
- ✅ Categorize by type (Food, Transport, Entertainment, etc.)
- ✅ Multiple payment methods (Cash, Card, UPI)
- ✅ Search and filter transactions
- ✅ Date-based sorting and grouping
- ✅ Quick add with floating action button

### 📊 Budget Tracking
- ✅ Set monthly budgets per category
- ✅ Real-time spending monitoring
- ✅ Visual progress indicators
- ✅ Overspending alerts (turns red when exceeded)
- ✅ Budget vs actual comparison
- ✅ Remaining balance calculation

### 🎯 Financial Goals
- ✅ Create savings goals with target amounts
- ✅ Track progress with visual indicators
- ✅ Set target dates for motivation
- ✅ Multiple goals support
- ✅ Goal achievement notifications

### 🏠 Smart Dashboard
- ✅ Total balance overview
- ✅ Monthly savings tracker
- ✅ Financial health index
- ✅ Smart spending insights
- ✅ Category distribution charts
- ✅ Recent transaction feed

### 📈 Analytics & Insights
- ✅ Spending patterns analysis
- ✅ Category-wise breakdown
- ✅ Monthly comparison
- ✅ Trend visualization
- ✅ AI-powered recommendations

### 🔒 Security
- ✅ Biometric authentication (Fingerprint/Face ID)
- ✅ App lock feature
- ✅ Secure local database
- ✅ Data encryption

### 📤 Data Export
- ✅ Export to CSV format
- ✅ Export to Excel
- ✅ Share via any app
- ✅ Backup and restore

### ⚙️ Customization
- ✅ Dark and Light themes
- ✅ Multiple currency support
- ✅ Flexible date formats
- ✅ Personalized categories

### 🔄 Recurring Transactions
- ✅ Automated recurring payments
- ✅ Background processing with WorkManager
- ✅ Scheduled transaction creation
- ✅ Custom frequencies

---

## 🏗️ Tech Stack

### Core Technologies
- **Language**: Kotlin
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Build System**: Gradle (Kotlin DSL)

### Architecture & Patterns
- **Architecture**: MVVM + Clean Architecture
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3 with custom glassmorphic components
- **Dependency Injection**: Hilt/Dagger
- **Async**: Kotlin Coroutines + Flow

### Jetpack Libraries
- 🎨 **Compose** - Declarative UI
- 🏗️ **Room** - Local database
- 🧭 **Navigation** - Screen navigation
- 🔄 **WorkManager** - Background tasks
- 💾 **DataStore** - Preferences storage
- 🔐 **Biometric** - Authentication
- 🎯 **Lifecycle** - Lifecycle-aware components
- 🧪 **Hilt** - Dependency injection

### Additional Libraries
- **MPAndroidChart** - Data visualization
- **Coil** - Image loading
- **Timber** - Logging

---

## 📸 Screenshots

<div align="center">

| Dashboard | Transactions | Budgets |
|-----------|-------------|---------|
| <img src="screenshots/dashboard.png" width="250"/> | <img src="screenshots/transactions.png" width="250"/> | <img src="screenshots/budgets.png" width="250"/> |

| Goals | Analytics | Settings |
|-------|-----------|----------|
| <img src="screenshots/goals.png" width="250"/> | <img src="screenshots/analytics.png" width="250"/> | <img src="screenshots/settings.png" width="250"/> |

</div>

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK** 17 or higher
- **Android SDK** with API 35
- **Gradle** 8.0+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/fintrack-android.git
cd fintrack-android
```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Project**
   - Wait for Gradle sync to complete
   - This may take 5-10 minutes for first-time setup

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`
   - Select your device and click "OK"

### Building APK

**Debug Build:**
```bash
./gradlew assembleDebug
```
APK location: `app/build/outputs/apk/debug/app-debug.apk`

**Release Build:**
```bash
./gradlew assembleRelease
```
APK location: `app/build/outputs/apk/release/app-release.apk`

---

## 📁 Project Structure

```
com.fintrack.app/
├── core/                           # Core utilities & design system
│   ├── designsystem/               # UI components & theming
│   │   ├── component/              # Reusable components
│   │   │   ├── GlassComponents.kt  # Glassmorphic cards
│   │   │   └── FinTrackMetricCard.kt
│   │   └── theme/                  # Theme configuration
│   │       ├── Color.kt            # Color palette
│   │       ├── Theme.kt            # Theme definition
│   │       └── Type.kt             # Typography
│   ├── navigation/                 # Navigation setup
│   │   ├── FinTrackDestination.kt
│   │   └── FinTrackNavHost.kt
│   ├── util/                       # Utilities
│   │   ├── BiometricHelper.kt
│   │   ├── DateUtils.kt
│   │   └── MoneyFormatter.kt
│   └── work/                       # Background workers
│       ├── RecurringTransactionWorker.kt
│       └── WorkScheduler.kt
│
├── data/                           # Data layer
│   ├── local/                      # Local data source
│   │   ├── dao/                    # Room DAOs
│   │   ├── entity/                 # Room entities
│   │   └── FinTrackDatabase.kt
│   ├── repository/                 # Repository implementations
│   └── export/                     # Export functionality
│
├── domain/                         # Business logic
│   ├── model/                      # Domain models
│   ├── repository/                 # Repository interfaces
│   └── usecase/                    # Use cases
│
├── presentation/                   # UI layer
│   ├── dashboard/                  # Dashboard screen
│   ├── transaction/                # Transactions screen
│   ├── budget/                     # Budgets screen
│   ├── goals/                      # Goals screen
│   ├── analytics/                  # Analytics screen
│   ├── settings/                   # Settings screen
│   └── component/                  # Shared UI components
│
└── di/                             # Dependency injection
    ├── DatabaseModule.kt
    ├── RepositoryModule.kt
    └── ExportModule.kt
```

---

## 🎨 Design System

### Color Palette

**Dark Theme (Default)**
- Primary: `#20E3A2` (Emerald)
- Secondary: `#FFC857` (Gold)
- Error: `#FF6B6B` (Coral)
- Background: `#020713` (Deep Navy)
- Surface: `#0B1424`

**Light Theme**
- Background: `#F8F9FB`
- Surface: `#FFFFFF`
- Text Primary: `#1E1E2C`

### Typography
- **Headlines**: Bold, Extra Bold with gradient effects
- **Body**: Regular, Medium weights
- **Labels**: Small, Medium sizes

### Components
- **GlassCard**: Glassmorphic card with blur effect
- **PremiumHeader**: Gradient text headers
- **FinTrackMetricCard**: Metric display cards
- **TransactionRow**: Transaction list item
- **SectionCard**: Content section container

---

## 🧪 Testing

**Run Unit Tests:**
```bash
./gradlew test
```

**Run Instrumented Tests:**
```bash
./gradlew connectedAndroidTest
```

**Test Coverage:**
```bash
./gradlew jacocoTestReport
```

---

## 📦 Dependencies

```kotlin
dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Other
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}
```

---

## 🔑 Key Learnings

Building FinTrack taught valuable lessons in:
- **Jetpack Compose** - Modern declarative UI development
- **Clean Architecture** - Separation of concerns and testability
- **Room Database** - Efficient local data persistence
- **WorkManager** - Reliable background task scheduling
- **Material 3** - Latest design guidelines implementation
- **Biometric API** - Secure authentication integration
- **Hilt DI** - Dependency management at scale

---

## 🛣️ Roadmap

### Version 1.1 (Coming Soon)
- [ ] Cloud sync with Firebase
- [ ] Multi-device support
- [ ] Bank account integration
- [ ] Receipt scanning with ML
- [ ] Widget support
- [ ] Investment tracking

### Version 1.2
- [ ] Bill reminders
- [ ] Spending predictions
- [ ] Financial reports (PDF)
- [ ] Multiple accounts
- [ ] Shared budgets
- [ ] Credit score integration

### Version 2.0
- [ ] Web dashboard
- [ ] iOS app
- [ ] Advanced analytics
- [ ] Tax calculation
- [ ] Financial advisor AI
- [ ] Cryptocurrency tracking

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and development process.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 FinTrack

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/) - Design guidelines
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI framework
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Chart library
- [Hilt](https://dagger.dev/hilt/) - Dependency injection
- [Room](https://developer.android.com/training/data-storage/room) - Database

---

## 📊 Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/fintrack-android?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/fintrack-android?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/yourusername/fintrack-android?style=social)

---

## 📞 Support

If you like this project, please consider:
- ⭐ Starring the repository
- 🐛 Reporting bugs
- 💡 Suggesting new features
- 🤝 Contributing to the code

For support, email your.email@example.com or open an issue.

---

<div align="center">

**Made with ❤️ and Kotlin**

[⬆ Back to Top](#-fintrack---premium-personal-finance-manager)

</div>
