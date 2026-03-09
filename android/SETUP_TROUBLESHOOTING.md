# Guida di Setup e Troubleshooting

## Problemi Comuni e Soluzioni

### 1. "Failed to resolve: androidx.compose.ui:ui:1.6.0"
**Soluzione**:
```
File → Invalidate Caches → Restart
```
oppure
```bash
./gradlew clean build --refresh-dependencies
```

### 2. Gradle Sync Fallisce
**Soluzione**:
1. Controllare Java version: `java -version` deve essere 11+
2. Controllare SDK path in `local.properties`
3. Scaricare Android SDK API 34:
   - Tools → SDK Manager → API Levels → Select 34

### 3. Emulator non Avvia in Landscape
**Soluzione**:
1. Aprire AVD Manager
2. Selezionare device
3. Edit → Advanced Settings
4. Setup OpenGL ES: Hardware
5. Avviare e ruotare manualmente
   - Ctrl+F12 (Windows/Linux) o Cmd+Left (Mac)

### 4. File Stampings Non Viene Creato
**Soluzioni**:
1. Verificare permessi in AndroidManifest.xml
2. Controllare se context.filesDir è accessibile
3. Eseguire in debug:
   ```kotlin
   Log.d("FileManager", "Stampings path: ${getStampingsFile().absolutePath}")
   ```

### 5. Time/Date Formattati Incorretti
**Causa**: Locale device diverso da previsto
**Soluzione**:
```kotlin
// In FileManager.kt, usare Locale.US se necessario
SimpleDateFormat("dd/MM/yyyy", Locale.US)
```

## Compilazione Manuale da Terminal

### Windows (PowerShell)
```powershell
# Entrare nella cartella del progetto
cd android

# Non serve specificare SDK se in local.properties
./gradlew.bat clean build

# Installare su emulator/device
./gradlew.bat installDebug
```

### Linux/Mac
```bash
cd android
chmod +x gradlew
./gradlew clean build
./gradlew installDebug
```

## Setup da Zero in Android Studio

1. **Clonare/Aprire Progetto**
   - File → Open → Selezionare cartella `android`

2. **Scarica Android SDK**
   - Version: API 34
   - Compilazione: Gradle 8.0+

3. **Configura Emulator (Tablet)**
   - Tools → Device Manager → Create Device
   - Hardware: Pixel Tablet (o simile)
   - Sistema: API 24+
   - Start

4. **Sincronizza Gradle**
   - Atteni sync automatico
   - Se fallisce: File → Sync Now

5. **Esegui App**
   - Run → Run 'app'
   - Selezionare emulator
   - Attendere avvio

## Feature Aggiuntive (Future)

### Statistiche Giornaliere
```kotlin
fun getDailyStats(): Map<String, Pair<String, String>> {
    // Ritorna per ogni dipendente:
    // ingresso (primo IN) e uscita (ultimo OUT)
}
```

### Export CSV
```kotlin
fun exportToCSV(): String {
    // Esporta stampings in formato CSV
    // code,type,time
}
```

### Cloud Sync
```kotlin
// Sincronizzare con backend server
// Usare Retrofit + OkHttp
```

### Auth System
```kotlin
// Pin per accedno area configurazione
// Modifica config.json senza file system
```

### Reports
```kotlin
// Grafico orari giornalieri
// Storico settimanale/mensile
```

## Distribuzione

### APK Release Firmata
```bash
# Generare keystore (una volta)
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias timestampings

# Compilare release firmata
./gradlew assembleRelease

# APK sarà in: app/build/outputs/apk/release/
```

### Play Store
1. Creare account developer
2. Aggiornare versionCode/versionName in build.gradle.kts
3. Generare App Bundle:
   ```bash
   ./gradlew bundleRelease
   ```
4. Upload su Play Console

## Configurazione CI/CD

### GitHub Actions (Example)
Creare `.github/workflows/android.yml`:
```yaml
name: Android Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build APK
        run: |
          cd android
          chmod +x gradlew
          ./gradlew build
      - name: Upload APK
        uses: actions/upload-artifact@v2
        with:
          name: app-debug.apk
          path: android/app/build/outputs/apk/debug/
```

## Logging e Debug

### Abilitare Verbose Logging
```bash
./gradlew assembleDebug --info
```

### Android Monitor in Android Studio
1. Tools → Device Explorer
2. Navigare a `/data/data/com.timstamping.app/`
3. Visualizzare file e directory

## Monitoraggio Performance

### ANR (App Not Responding) Prevention
- Dialog auto-close con `delay()` è non-bloccante ✓
- FileManager usa thread synchronously (OK per piccoli file)
- Se necessario, migrare a coroutine dispatcher Io

```kotlin
fun saveStampingAsync(code: String, type: String) = viewModelScope.launch(Dispatchers.IO) {
    saveStamping(code, type)
}
```

### Memory Leaks
- Compose gestisce composables lifecycle ✓
- FileManager non mantiene riferimenti ✓
- Dialog chiusione effettiva (no memory leaks) ✓

## Accessibilità

### Text Size
Testare con impostazioni device:
- Settings → Display → Text size → Large
- App dovrebbe adattarsi ✓

### Color Contrast
Header blue/white: 4.5:1 ratio ✓
Pulsanti: sufficienti contrasti ✓

## Localizzazione

### Aggiungere lingua
1. res/values-it/strings.xml (italiano)
2. res/values-en/strings.xml (inglese)

Currently: strings.xml è single language

## Support Matrix

| Versione | Status |
|----------|--------|
| Android 6.0 (API 24) | ✓ Supportato |
| Android 7.0-10 | ✓ Tested |
| Android 11-14 | ✓ Tested |
| iOS | ✗ No (Android only) |
