# 📱 Web to APK Android Project (គម្រោងបង្កើត APK ពី Website & HTML)

គម្រោង Android កម្រិតខ្ពស់សម្រាប់បំប្លែង **HTML/CSS/JS Files** និង **Website URLs** ទៅជា **Android APK Application** ពេញលេញ ជាមួយ Splash Screen, Custom App Icon, Offline Support, និង GitHub Actions Auto-Build APK។

---

## 🌟 លក្ខណៈពិសេសសំខាន់ៗ (Key Features)

- 🌐 **Website URL Mode**: អាចដាក់ Link Website ណាមួយក៏បាន (HTTPS/HTTP) ដើម្បីបើកក្នុង App។
- 📦 **Offline Local HTML Mode**: គាំទ្រការដាក់ឯកសារ HTML5, CSS3, JS ក្នុង `app/src/main/assets/www/` ដំណើរការ Offline ដោយមិនចាំបាច់មាន Internet។
- 🎨 **Splash Screen**: មាន Splash Screen ស្រស់ស្អាតជាមួយ Logo និងការ Load ដោយស្វ័យប្រវត្តិ។
- 🚀 **Adaptive App Icon**: រូបតំណាង App Icon ទំនើបតាមបទដ្ឋាន Android Material You។
- 🔄 **Smart WebView Controls**:
  - ប៊ូតុង Back / Forward / Refresh
  - Progress Bar បង្ហាញភាគរយ Loading
  - Swipe to Refresh (ទាញចុះក្រោមដើម្បី Reload)
  - គាំទ្រ Links ខាងក្រៅដូចជា `tel:`, `mailto:`, `intent:`, `whatsapp:`
  - ដោះស្រាយ Error និងមានប៊ូតុង Retry នៅពេលគ្មាន Internet
- ⚙️ **In-App Quick Settings**: អាចប្តូរ URL, ប្តូររវាង Local HTML និង Live Website, Clear Cache/Cookies ភ្លាមៗ។
- 🤖 **GitHub Actions CI/CD**: ដាក់ Workflow `.github/workflows/build_apk.yml` ស្រេចសម្រាប់ Build APK លើ GitHub ដោយស្វ័យប្រវត្តិ។

---

## 📂 រចនាសម្ព័ន្ធឯកសារ (Project Structure)

```text
├── .github/
│   └── workflows/
│       └── build_apk.yml           # GitHub Actions សម្រាប់ Build APK
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── www/            # 👈 ទីតាំងដាក់ File HTML, CSS, JS របស់អ្នក
│   │   │   │       ├── index.html
│   │   │   │       ├── style.css
│   │   │   │       └── app.js
│   │   │   ├── java/com/example/   # កូដ Kotlin Android
│   │   │   └── res/                # App Icons, Splash Art, Strings, Layouts
│   └── build.gradle.kts            # កំណត់ Application ID (com.example.webapp), Version
└── README.md
```

---

## 🛠️ របៀបប្រើប្រាស់ និងកំណត់ (Configuration Guide)

### ១. របៀបដាក់ Website URL ផ្ទាល់ខ្លួនរបស់អ្នក
បើកឯកសារ `app/src/main/java/com/example/MainActivity.kt` ហើយស្វែងរកអថេរ `DEFAULT_URL`៖
```kotlin
// ផ្លាស់ប្តូរ URL គេហទំព័ររបស់អ្នកនៅទីនេះ៖
val DEFAULT_URL = "https://yourwebsite.com"
```

### ២. របៀបដាក់ File HTML ផ្ទាល់ខ្លួន
1. ចូលទៅកាន់ថត `app/src/main/assets/www/`
2. ដាក់ File `index.html`, `style.css`, `app.js` ឬ folder រូបភាពផ្សេងៗរបស់អ្នកចូលទីនោះ។

### ៣. របៀបប្តូរឈ្មោះ App (App Name)
បើកឯកសារ `app/src/main/res/values/strings.xml`:
```xml
<resources>
    <string name="app_name">ឈ្មោះកម្មវិធីរបស់អ្នក</string>
</resources>
```

### ៤. របៀបប្តូរ Package Name (Application ID)
បើក `app/build.gradle.kts` ត្រង់ `defaultConfig`:
```kotlin
defaultConfig {
    applicationId = "com.yourcompany.appname"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"
}
```

---

## 🚀 របៀប Push ទៅកាន់ GitHub & Build APK ដោយស្វ័យប្រវត្តិ

1. **បង្កើត GitHub Repository ថ្មី** នៅលើ [GitHub.com](https://github.com/new)
2. **Push កូដនេះទៅកាន់ Repository:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit for Web to APK"
   git branch -M main
   git remote add origin https://github.com/your-username/your-repo-name.git
   git push -u origin main
   ```
3. **ទាញយក APK ពី GitHub Actions:**
   - ចូលទៅកាន់ Tab **Actions** ក្នុង GitHub Repository របស់អ្នក។
   - ចុចលើ Workflow **Build Android APK**។
   - នៅពេល Build ជោគជ័យ (សញ្ញាគ្រីសពណ៌បៃតង ✅) ចុចលើវា ហើយ Scroll ចុះក្រោមត្រង់ **Artifacts** ដើម្បី Download `app-debug-apk.zip`។
   - ពន្លា (Extract) Zip នោះ អ្នកនឹងទទួលបានឯកសារ `app-debug.apk` សម្រាប់ Install លើទូរស័ព្ទដៃ Android!

---

## 💻 របៀប Build APK លើកុំព្យូទ័រផ្ទាល់ (Local Build)

បើក Terminal ក្នុង Project រួចដំណើរការ៖
```bash
gradle assembleDebug
```
ឯកសារ APK នឹងបង្កើតនៅ៖ `app/build/outputs/apk/debug/app-debug.apk`
