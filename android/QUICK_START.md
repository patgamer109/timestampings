# 🚀 Guida Rapida - Time Stampings App

## Avviamento Veloce (5 minuti)

### Requisiti Minimi
- Android Studio 2023.1+
- JDK 11+
- Android SDK API 24+

### Step 1: Aprire in Android Studio
```
File → Open → Selezionare cartella "android"
```

### Step 2: Sincronizzare Gradle
```
Attendere che Android Studio sincronizzi automaticamente
Se coda errore: File → Sync Now
```

### Step 3: Creare o Selezionare Emulator
```
Tools → Device Manager → Create Virtual Device
- Tipo: Tablet (landscape)
- API: 24+ (consigliato 33-34)
- Start emulator
```

### Step 4: Eseguire App
```
Run → Run 'app'
Selezionare emulator dalla lista
Attendere compilazione e avvio
```

---

## Schermata Principale - Cosa Vedi

Orientamento: **Landscape** (orizzontale)

```
┌─────────────────────────────────────┐
│    Time Stampings                   │  ← Header
│ Supermercato Tigre Tavullia         │
├─────────────────────────────────────┤
│         14:30                       │  ← Orario
│       09/03/2026                    │  ← Data
├─────────────────────────────────────┤
│  [IN]      [OPTIONS]      [OUT]     │  ← 3 Pulsanti
│ Verde      Grigio        Rosso       │
└─────────────────────────────────────┘
```

---

## Test Veloce

### Test 1: Premere IN
1. Premi pulsante verde **IN**
2. Inserisci codice: **1001 (Alessia)**
3. Vedrai: "Alessia\nSTART" (si chiude automaticamente)
4. Controlla file: `android/stampings/20240309_Friday.json`

### Test 2: Premere OUT
1. Premi pulsante rosso **OUT**
2. Inserisci codice: **1002 (Alessandro)**
3. Vedrai: "Alessandro\nEND"

### Test 3: Premere OPTIONS
1. Premi pulsante grigio **OPTIONS**
2. Inserisci codice: **3401** (version)
3. Vedrai: "VERSION: 1.0"

### Test 4: Codice Non Valido
1. Premi IN o OUT
2. Inserisci: **9999**
3. Vedrai: "wrong code, please retry"

---

## Tastierino

```
┌───┬───┬───┐
│ 1 │ 2 │ 3 │
├───┼───┼───┤
│ 4 │ 5 │ 6 │
├───┼───┼───┤
│ 7 │ 8 │ 9 │
├───┼───┼───┤
│ X │ 0 │ ← │
└───┴───┴───┘

X  = Chiude dialog (rosso)
←  = Cancella ultima cifra (grigio)
0-9 = Inserisci cifra (blu)
```

---

## File Sistema Creati

Dopo una timbratura, verifica il file:
```
android/app/src/main/files/stampings/
    └── 20240309_Friday.json (esempio)
```

Contenuto:
```json
{
    "stampings": [
        {"code": "1001", "type": "in", "time": "14:30"},
        {"code": "1002", "type": "out", "time": "18:30"}
    ]
}
```

---

## Comandi Utili

### Da Terminale (Windows/PowerShell)
```powershell
cd android

# Compilare
./gradlew.bat clean build

# Installare su emulator
./gradlew.bat installDebug

# Seguire log
adb logcat | find "timstamping"
```

### Da Terminale (Linux/Mac)
```bash
cd android
chmod +x gradlew

# Compilare
./gradlew clean build

# Installare
./gradlew installDebug

# Log
adb logcat | grep "timstamping"
```

---

## Codici Dipendenti (Dalla config)

| Nome | Codice |
|------|--------|
| Alessia | 1001 |
| Alessandro | 1002 |
| Barbara | 2001 |
| Carlo | 3001 |
| Davide | 4001 |
| Elena | 5001 |
| Federico | 6001 |
| Francesca | 6002 |
| Giulia | 7001 |
| Luca | 1201 |
| Maria | 1301 |
| Nicola | 1401 |
| Paolo | 1601 |
| Roberto | 1801 |
| Sara | 1901 |
| Stefano | 1902 |

---

## Codici Speciali (OPTIONS)

| Codice | Funzione | Output |
|--------|----------|--------|
| 3401 | version | "VERSION: 1.0" |
| 3402 | download | "DOWNLOAD OK" |

---

## Troubleshooting Veloce

### ❌ App non avvia
```
→ Controlla Java: java -version (deve essere 11+)
→ Sincronizza Gradle: File → Sync Now
→ Riavvia Android Studio
```

### ❌ Emulator non in landscape
```
→ Ruota manualmente: Ctrl+F12 (Windows)
→ O ricrea emulator: Tools → Device Manager
```

### ❌ File stampings non crearsi
```
→ Verifica permessi in AndroidManifest.xml
→ Prova con logcat: adb logcat | grep FileManager
```

### ❌ Ora formattata male
```
→ Verifica timezone device: Settings → Date & Time
→ Locale: Settings → Language & Input
```

---

## Configurazione da Modificare

### config.json
Percorso: `android/src/main/assets/config.json`

```json
{
    "version": "1.0",          ← Versione app
    "title": "Time Stampings", ← Titolo header
    "subTitle": "...",         ← Sottotitolo header
    "timeMessages": 3000       ← Millisecondi popup (3 sec)
}
```

### dipendenti.json
Percorso: `android/src/main/assets/dipendenti.json`

Aggiungi/modifica dipendenti qui

---

## Struttura Progetto Essenziale

```
android/
├── app/
│   └── src/main/
│       ├── kotlin/com/timstamping/app/
│       │   ├── MainActivity.kt
│       │   ├── Models.kt
│       │   ├── FileManager.kt
│       │   └── ui/
│       │       ├── TimestampingApp.kt
│       │       ├── Buttons.kt
│       │       └── Dialogs.kt
│       ├── assets/
│       │   ├── config.json
│       │   └── dipendenti.json
│       └── AndroidManifest.xml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew & gradlew.bat
└── DOCUMENTAZIONE (README, FLOWCHART, TESTING, etc.)
```

---

## Documentazione Completa

Per approfondimenti, vedi:
- **README.md** - Panoramica e setup
- **ARCHITECTURE.md** - Architettura tecnica
- **FLOWCHART.md** - Diagrammi logica
- **TESTING.md** - Come testare
- **SETUP_TROUBLESHOOTING.md** - Problemi e soluzioni

---

## Prossimi Passi Consigliati

1. ✅ Compilare e testare app
2. ⏳ Espandere elenco dipendenti
3. ⏳ Personalizzare config.json
4. ⏳ Integrare con backend
5. ⏳ Distribuire su Play Store

---

**Versione**: 1.0  
**Linguaggio**: Kotlin + Jetpack Compose  
**Tempo Setup**: ~5 minuti  
**Tempo Test**: ~10 minuti
