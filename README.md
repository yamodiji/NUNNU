# Smart Drawer

A lightweight Android native app that provides a floating smart app drawer with search functionality. Built following **REFKOT.md** specifications for modern Android development practices.

## 📱 Features

### 🔼 **Smart App Access**
- **Floating Widget**: Always-visible draggable search button
- **Swipe-Up Gesture**: Open drawer with home screen swipe (requires accessibility permission)
- **Instant Search**: Auto-focus search with keyboard, real-time filtering
- **Quick Launch**: Tap any app to launch instantly

### 🎯 **Smart Organization**
- **Pin Favorite Apps**: Pin frequently used apps to the top
- **Grid/List View**: Choose your preferred layout
- **System Apps Toggle**: Show/hide system applications
- **Smart Sorting**: Pinned apps first, then alphabetical

### ⚡ **Performance Optimized**
- **< 2.5MB APK**: Minimal size using View-based UI (no Compose)
- **Battery Efficient**: No unnecessary background processes
- **Memory Optimized**: Lightweight overlay implementation
- **Fast Search**: Real-time app filtering

### ⚙️ **Customizable**
- **Widget Positioning**: Drag to any screen position
- **Size Options**: Small, Medium, Large widget sizes
- **Dark Mode**: Toggle dark theme
- **Flexible Permissions**: Works with just overlay permission

## 🛠️ Technical Stack

**Following REFKOT.md Standards:**
- **Language**: Kotlin only (no Java)
- **Min SDK**: 23 (Android 6.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM + Repository Pattern
- **UI**: View-based (ViewBinding) for minimal size
- **Async**: Coroutines + LiveData

## 📋 Permissions

### Required
- **System Alert Window**: Display floating widget and overlay
- **Query All Packages**: Access installed apps list (Android 11+)

### Optional
- **Accessibility Service**: Enable swipe-up gesture detection
- **Record Audio**: Voice search functionality

## 🚀 Installation & Usage

### Option 1: GitHub Actions (Recommended)
1. **Build automatically** using GitHub Actions (follows REFKOT.md CI/CD standards)
2. **Download APK** from GitHub Actions artifacts
3. **Install** on your device

### Option 2: Android Studio
1. **Clone** this repository
2. **Open** in Android Studio
3. **Build debug APK** only (`./gradlew assembleDebug`)
4. **Install** on device

⚠️ **Important**: Following REFKOT.md guidelines, **never build release APK locally**. Use GitHub Actions for all production builds.

### Setup Steps
1. **Install** the APK
2. **Grant Overlay Permission** when prompted
3. **Grant Accessibility Permission** (optional, for swipe gestures)
4. **Tap "Start Smart Drawer"**
5. **Tap floating button** or **swipe up** to open drawer

## 🔧 Configuration

### Settings Available
- **Enable/Disable Floating Widget**
- **Enable/Disable Swipe-Up Gesture**
- **Toggle Dark Mode**
- **Switch Layout: Grid ↔ List**
- **Show/Hide System Apps**
- **Widget Size: Small/Medium/Large**
- **Reset Widget Position**

### Usage Tips
- **Drag floating widget** to reposition
- **Long-press apps** to pin/unpin
- **Search while typing** for instant filtering
- **Touch outside drawer** to dismiss

## 📁 Project Structure

```
Smart Drawer/
├── .github/workflows/           # CI/CD automation (REFKOT.md compliant)
│   └── android-build.yml       # GitHub Actions workflow
├── app/
│   ├── src/main/
│   │   ├── java/com/smartdrawer/app/
│   │   │   ├── ui/              # Activities & UI components
│   │   │   ├── services/        # Floating widget & gesture services
│   │   │   ├── viewmodels/      # MVVM ViewModels
│   │   │   ├── data/models/     # Data classes
│   │   │   ├── adapters/        # RecyclerView adapters
│   │   │   └── utils/           # Utilities & helpers
│   │   └── res/                 # Layouts, strings, drawables
│   └── build.gradle            # App configuration
├── build.gradle                # Root Gradle config
├── settings.gradle             # Project settings
└── README.md                   # This file
```

## 🏗️ Build Configuration

**Exact versions following REFKOT.md:**
- **Kotlin**: 1.9.10
- **Android Gradle Plugin**: 8.1.2
- **Gradle**: 8.0
- **Min SDK**: 23, **Target SDK**: 34

**Core Dependencies:**
```gradle
// AndroidX Core
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
```

## 🧪 Testing

**Local Development Only:**
```bash
./gradlew test                 # Unit tests
./gradlew lintDebug           # Code analysis
./gradlew assembleDebug       # Debug APK only
```

**Production Builds:**
- ✅ **GitHub Actions automatic builds**
- ✅ **Download from Actions artifacts**
- ❌ **Never build release locally**

## 🚀 CI/CD Pipeline

Following REFKOT.md standards:
1. **Automatic builds** on push to main/develop
2. **Run tests** and lint checks
3. **Generate APK artifacts**
4. **Upload to GitHub Releases** for production

## 🔐 Privacy & Security

- **No data collection**: App doesn't collect personal information
- **Local operation**: All processing done on-device
- **Minimal permissions**: Only essential permissions requested
- **Open source**: Full code transparency

## 📄 License

This project follows REFKOT.md development standards and is available for educational and personal use.

## 🐛 Issues & Support

- **Report bugs** via GitHub Issues
- **Feature requests** welcome
- **Contributions** following REFKOT.md standards

---

**Built with ❤️ following REFKOT.md standards for modern Android development** 