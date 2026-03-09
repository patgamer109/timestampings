# Time Stampings - App Android

App Android per la gestione delle timbrature dei dipendenti con interfaccia tablet in modalità orizzontale.

## Funzionalità

### Schermata Principale
- **Header**: Mostra titolo e sottotitolo dal file config.json
- **Sezione Orario**: Visualizza ora (HH:mm) e data (dd/mm/yyyy) in tempo reale
- **Sezione Pulsanti**: 
  - Pulsante IN (verde, circolare, grande)
  - Pulsante OUT (rosso, circolare, grande)
  - Pulsante OPTIONS (grigio, rettangolare, bordi arrotondati)

### Popup Function Code
Appare premendo IN, OUT o OPTIONS con tastierino numerico:
- Label adattiva: "IN - CODE", "OUT - CODE", "FUNCTION - CODE"
- Campo di input per 4 cifre
- Tastierino numerico (1-9, 0)
- Pulsante X rosso (chiude il popup)
- Pulsante freccia ← (cancella ultima cifra)

### Popup Message Code
Mostra messaggi e si chiude automaticamente dopo il tempo in config.json:
- "wrong code, please retry" - se codice non valido
- "NomeDipendente\nSTART" - dopo timbratura IN
- "NomeDipendente\nEND" - dopo timbratura OUT
- "VERSION: 1.0" - per codice version (3401)
- "DOWNLOAD OK" - per codice download (3402)

### Gestione Timbrature
- Verifica codice nel file dipendenti.json
- Crea/aggiorna file yyyymmdd_day.json nella cartella stampings
- Registra: codice, tipo (in/out), ora (HH:mm)

## File di Configurazione
- `config.json`: Configurazione generale (title, subTitle, timeMessages, specialCodes)
- `dipendenti.json`: Elenco dipendenti con codici
- `stampings/`: Cartella dove vengono salvate le timbrature

## Compilazione

### Con Android Studio
1. Aprire il progetto in Android Studio
2. Sincronizzare Gradle
3. Run > Run 'app'

### Con Gradle da linea di comando
```bash
./gradlew build
./gradlew assembleDebug
```

## Requisiti
- Android SDK 24+
- Kotlin 1.9.0+
- Jetpack Compose 1.6.0+

## Permessi
- READ_EXTERNAL_STORAGE
- WRITE_EXTERNAL_STORAGE
