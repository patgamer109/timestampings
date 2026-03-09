# Time Stampings - Android App
## Indice Documentazione Completa

### 📋 Documentazione Principale

#### [README.md](README.md)
- Panoramica progetto
- Features principali
- Quick start guide
- Compilazione con Gradle

#### [ARCHITECTURE.md](ARCHITECTURE.md)
- Struttura progetto dettagliata
- Descrizione componenti
- Flusso applicativo
- Struttura file JSON
- File di configurazione

#### [FLOWCHART.md](FLOWCHART.md)
- Diagrammi flusso logica
- Stati principale dell'app
- Flusso timbratura IN/OUT
- Flusso Options/Codici speciali
- Gestione file e validazione

#### [TESTING.md](TESTING.md)
- Guida setup ambiente testing
- Test case dettagliati
- Verifiche regressione
- Debug tips & tricks
- Checklist finale

#### [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md)
- Problemi comuni e soluzioni
- Compilazione manuale
- Setup da zero
- Feature future
- Distribuzione e CI/CD

---

### 🎯 Guida Rapida - Start Here

#### Per Sviluppatori Nuovi:
1. Leggere [README.md](README.md)
2. Esaminare [ARCHITECTURE.md](ARCHITECTURE.md)
3. Seguire setup da [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md)

#### Per Debugging:
1. Consultare [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md)
2. Leggere [TESTING.md](TESTING.md) - Debug Tips

#### Per Comprendere Logica:
1. Studiare [FLOWCHART.md](FLOWCHART.md)
2. Approfondire in [ARCHITECTURE.md](ARCHITECTURE.md)

---

### 📁 Struttura File Progetto

```
android/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/timstamping/app/
│   │   │   ├── MainActivity.kt          (Activity principale)
│   │   │   ├── Models.kt                (Data class)
│   │   │   ├── FileManager.kt           (Gestione file JSON)
│   │   │   └── ui/
│   │   │       ├── TimestampingApp.kt   (UI principale)
│   │   │       ├── Buttons.kt           (Componenti bottoni)
│   │   │       └── Dialogs.kt           (Popup dialogs)
│   │   ├── res/values/
│   │   │   ├── strings.xml
│   │   │   └── styles.xml
│   │   ├── assets/
│   │   │   ├── config.json
│   │   │   └── dipendenti.json
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/wrapper/
│   └── gradle-wrapper.properties
│
├── build.gradle.kts (top-level)
├── settings.gradle.kts
├── gradlew (Unix/Linux/Mac)
├── gradlew.bat (Windows)
├── local.properties
│
└── DOCUMENTATION/
    ├── README.md (questa directory)
    ├── ARCHITECTURE.md
    ├── FLOWCHART.md
    ├── TESTING.md
    └── SETUP_TROUBLESHOOTING.md
```

---

### 🔧 Prerequisiti

- **Java**: JDK 11+
- **Android SDK**: API 24+ (min)
- **Build Tool**: Gradle 8.0+
- **IDE**: Android Studio 2023.1+ (opzionale)
- **Kotlin**: 1.9.0+

---

### ⚡ Comandi Veloci

#### Compilazione
```bash
cd android
./gradlew clean build        # Build completo
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK
```

#### Esecuzione
```bash
./gradlew installDebug       # Installa su device/emulator
./gradlew run                # Build + Install + Run
```

#### Debug
```bash
./gradlew build --info       # Build verbose
adb logcat | grep timestampings
```

---

### 📱 Funzionalità Principali

#### Schermata Principale
✓ Header con title/subtitle (da config.json)
✓ Ora e data aggiornate in tempo reale
✓ Tre pulsanti: IN (verde), OPTIONS (grigio), OUT (rosso)
✓ Layout fisso orizzontale (tablet)

#### Timbratura
✓ Dialog con tastierino numerico
✓ Validazione codice 4 cifre
✓ Ricerca in dipendenti.json
✓ Salvataggio in yyyyMMdd_day.json
✓ Messaggio auto-close

#### Codici Speciali
✓ VERSION (3401) → mostra "VERSION: 1.0"
✓ DOWNLOAD (3402) → mostra "DOWNLOAD OK"

---

### 🐛 Troubleshooting Rapido

| Problema | Soluzione |
|----------|-----------|
| Gradle sync fallisce | `./gradlew clean build --refresh-dependencies` |
| File stampings non creato | Controllare permessi in AndroidManifest.xml |
| Ora formattata male | Verificare Locale device (Settings) |
| Layout non landscape | Riavviare emulator con Ctrl+F12 |

👉 Vedi [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md) per soluzioni dettagliate

---

### 📊 Formato Dati

#### config.json
```json
{
    "version": "1.0",
    "specialCodes": [
        {"code": "3401", "function": "version"},
        {"code": "3402", "function": "download"}
    ],
    "title": "Time Stampings",
    "subTitle": "Supermercato Tigre Tavullia",
    "timeMessages": 3000
}
```

#### dipendenti.json
```json
{
  "dipendenti": [
    {"nome": "Alessia", "code": "1001"},
    {"nome": "Alessandro", "code": "1002"},
    ...
  ]
}
```

#### stampings (yyyyMMdd_day.json)
```json
{
    "stampings": [
        {"code": "1001", "type": "in", "time": "14:30"},
        {"code": "1001", "type": "out", "time": "18:30"}
    ]
}
```

---

### 🚀 Prossimi Passi

1. ✅ Progetto Android base creato
2. ✅ Documentazione completa
3. ⏳ Compilare e testare app
4. ⏳ Deploy su device/emulator
5. ⏳ Integrazioni future (statistiche, cloud sync, etc.)

Vedi [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md) per distribuzione Play Store

---

### 📞 Contatti & Support

Per issues o domande sullo sviluppo:
1. Consultare la documentazione pertinente
2. Verificare [TESTING.md](TESTING.md) per debug tips
3. Controllare [SETUP_TROUBLESHOOTING.md](SETUP_TROUBLESHOOTING.md)

---

**Versione**: 1.0  
**Last Updated**: Marzo 2026  
**Language**: Italian / English (docs)
