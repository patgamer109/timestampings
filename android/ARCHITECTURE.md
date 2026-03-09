# Architettura App Time Stampings

## Struttura del Progetto

```
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/timstamping/app/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── Models.kt
│   │   │   │   ├── FileManager.kt
│   │   │   │   └── ui/
│   │   │   │       ├── TimestampingApp.kt
│   │   │   │       ├── Buttons.kt
│   │   │   │       └── Dialogs.kt
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── styles.xml
│   │   │   ├── assets/
│   │   │   │   ├── config.json
│   │   │   │   └── dipendenti.json
│   │   │   └── AndroidManifest.xml
│   │   └── test/ (eventual)
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
│
├── build.gradle.kts (top-level)
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── local.properties
└── README.md
```

## Componenti Principali

### 1. Models.kt
Definisce i data class per la serializzazione JSON:
- `Config`: Configurazione dell'app (title, subTitle, timeMessages, specialCodes)
- `SpecialCode`: Codici speciali (version, download)
- `Dipendenti` e `Dipendente`: Elenco dei dipendenti
- `Stampings` e `Stamping`: Record delle timbrature

### 2. FileManager.kt
Gestisce tutte le operazioni file:
- Caricamento config.json e dipendenti.json da assets o file system
- Ricerca dipendente per codice
- Gestione data/ora formattati
- Creazione e aggiornamento file stampings (yyyyMMdd_day.json)
- Salvataggio timbrature

### 3. MainActivity.kt
Activity principale che configura l'app:
- Inizializza il tema Material3
- Configura screen orientation a landscape
- Carica TimestampingApp

### 4. TimestampingApp.kt
Composable principale con:
- Gestione stato (config, time, date, dialogs)
- Layout in tre sezioni (header, time/date, buttons)
- Orchestrazione dei popup

### 5. Buttons.kt
Componenti riutilizzabili:
- `CircleButton`: Pulsanti circolari (IN, OUT)
- `RectangleButton`: Pulsante rettangolare (OPTIONS)

### 6. Dialogs.kt
Popup dialogs:
- `FunctionCodeDialog`: Raccolta codice 4 cifre con tastierino
- `NumericKeypad`: Tastierino numerico stile telefono
- `MessageCodeDialog`: Messaggi automatici con auto-close

## Flusso Applicativo

### Premere IN o OUT
1. Apre `FunctionCodeDialog` con tastierino
2. Utente inserisce 4 cifre
3. Verifica codice in dipendenti.json
4. Se valido:
   - Salva timbratura in stampings file (yyyyMMdd_day.json)
   - Mostra nome dipendente + "START"/"END"
5. Se non valido:
   - Mostra "wrong code, please retry"
6. Messaggio si chiude dopo timeMessages ms (default 3000ms)

### Premere OPTIONS
1. Apre `FunctionCodeDialog` per codice speciale
2. Verifica codice in specialCodes
3. Se code="3401":
   - Mostra "VERSION: 1.0"
4. Se code="3402":
   - Mostra "DOWNLOAD OK"
5. Se non valido:
   - Mostra "wrong code, please retry"

## File di Timbratura

Formato: `stampings/yyyyMMdd_day.json` (es: `20240309_Thursday.json`)

Struttura:
```json
{
    "stampings": [
        {"code": "1001", "type": "in", "time": "14:30"},
        {"code": "1001", "type": "out", "time": "18:30"}
    ]
}
```

## Configurazione

### config.json
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

### dipendenti.json
```json
{
  "dipendenti": [
    {"nome": "Alessia", "code": "1001"},
    ...
  ]
}
```

## Compilazione da Gradle

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (signed)
./gradlew assembleRelease

# Run on emulator/device
./gradlew installDebug

# Full build cycle
./gradlew clean build
```

## Permessi Richiesti

- `READ_EXTERNAL_STORAGE`: Lettura file
- `WRITE_EXTERNAL_STORAGE`: Scrittura timbrature

## Dipendenze Principali

- Jetpack Compose 1.6.0
- Kotlin Serialization
- AndroidX Core, AppCompat, Lifecycle

## Configurazione Android Studio

1. File → New → Project → Android
2. Aggiungere path SDK in `local.properties`
3. Sincronizzare Gradle
4. Run → Run 'app'

## Note Tecniche

- Orientamento fissato a landscape (tablet)
- Timezone locale per data/ora
- JSON con pretty print per leggibilità
- Dialogs senza dismiss su click esterno (specifiche requisiti)
