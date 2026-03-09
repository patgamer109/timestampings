# Guida Testing - Time Stampings App

## Preparazione Ambiente

### Requisiti
- Android Studio 2023.1+
- JDK 11+
- Android SDK (API 24+)
- Emulatore o Device con Android 6.0+

### Setup Iniziale

1. **Clonare/Aprir il progetto in Android Studio**
   ```bash
   # Se da terminale
   cd android
   ./gradlew clean build
   ```

2. **Sincronizzare Gradle**
   - File → Sync Now
   - Attendere il download delle dipendenze

3. **Configurare Emulatore**
   - AVD Manager → Create Virtual Device
   - Selezionare "Tablet" (landscape-oriented)
   - API Level 24+
   - Avviare l'emulatore

## Test Case Basics

### TC001: Avvio Applicazione
**Precondizioni**: App installata
**Step**:
1. Avviare l'app
2. Attendere caricamento

**Risultato Atteso**:
- Header visualizza "Time Stampings" e "Supermercato Tigre Tavullia"
- Ora e data aggiornate in tempo reale
- Tre pulsanti visibili (IN, OUT, OPTIONS)
- Orientamento: landscape

### TC002: Timbratura IN - Codice Valido
**Precondizioni**: App avviata
**Step**:
1. Premere pulsante IN
2. Inserire codice "1001" (Alessia)
3. Attendere popup

**Risultato Atteso**:
- Dialog "IN - CODE" con tastierino
- Dopo 4 cifre:
  - Salva in file stampings
  - Popup mostra "Alessia\nSTART"
  - Si chiude automaticamente dopo 3 secondi
- Ritorna a schermata principale

### TC003: Timbratura IN - Codice Invalido
**Precondizioni**: App avviata
**Step**:
1. Premere pulsante IN
2. Inserire codice "9999"
3. Attendere popup

**Risultato Atteso**:
- Dialog "IN - CODE" con tastierino
- Dopo 4 cifre:
  - Popup mostra "wrong code, please retry"
  - Si chiude automaticamente dopo 3 secondi
- File stampings NON viene modificato
- Ritorna a schermata principale

### TC004: Timbratura OUT
**Precondizioni**: App avviata
**Step**:
1. Premere pulsante OUT
2. Inserire codice "2001" (Barbara)
3. Attendere popup

**Risultato Atteso**:
- Dialog "OUT - CODE" con tastierino
- Dopo 4 cifre:
  - Salva in file stampings con type="out"
  - Popup mostra "Barbara\nEND"
  - Si chiude automaticamente dopo 3 secondi

### TC005: Tastierino - Cancellazione Cifra
**Precondizioni**: Dialog FunctionCode aperto
**Step**:
1. Inserire "100"
2. Premere ← (freccia sinistra)
3. Verificare campo

**Risultato Atteso**:
- Campo mostra "10"
- Ultima cifra rimossa

### TC006: Tastierino - Chiusura
**Precondizioni**: Dialog FunctionCode aperto
**Step**:
1. Inserire qualche cifra
2. Premere X (quadrato rosso)

**Risultato Atteso**:
- Dialog si chiude
- Nessuna timbratura salvata
- Ritorna a schermata principale

### TC007: Codice Speciale - Version
**Precondizioni**: App avviata
**Step**:
1. Premere OPTIONS
2. Inserire "3401"

**Risultato Atteso**:
- Dialog "FUNCTION - CODE"
- Popup mostra "VERSION: 1.0"
- Si chiude dopo 3 secondi

### TC008: Codice Speciale - Download
**Precondizioni**: App avviata
**Step**:
1. Premere OPTIONS
2. Inserire "3402"

**Risultato Atteso**:
- Dialog "FUNCTION - CODE"
- Popup mostra "DOWNLOAD OK"
- Si chiude dopo 3 secondi

### TC009: Verifica File Stampings
**Precondizioni**: Almeno una timbratura salvata
**Step**:
1. Connettere device a PC (se necessario)
2. Aprire Android Device Monitor / Logcat
3. Navigare a: `/data/data/com.timstamping.app/files/stampings/`
4. Leggere file `yyyyMMdd_day.json` (es: `20240309_Friday.json`)

**Risultato Atteso**:
```json
{
    "stampings": [
        {"code": "1001", "type": "in", "time": "14:30"},
        {"code": "2001", "type": "in", "time": "14:35"},
        {"code": "1001", "type": "out", "time": "18:30"}
    ]
}
```

## Test di Regressione Settimanale

### Input Test
Dipendenti comuni:
- **1001**: Alessia (IN ore 14:30, OUT ore 18:30)
- **1002**: Alessandro (IN ore 14:45, OUT ore 18:45)
- **2001**: Barbara (IN ore 15:00, OUT ore 19:00)

Codici speciali:
- **3401**: VERSION → "VERSION: 1.0"
- **3402**: DOWNLOAD → "DOWNLOAD OK"

### Verifiche Settimanali
1. ✓ Orientamento landscape fisso
2. ✓ Header aggiornato
3. ✓ Ora aggiorna ogni secondo
4. ✓ File stampings creati correttamente
5. ✓ Codici dipendenti validati
6. ✓ Popup auto-close funzionante
7. ✓ Tastierino input corretto
8. ✓ Pulsante X chiude dialog
9. ✓ Pulsante ← cancella cifre
10. ✓ Nessun crash o ANR

## Debug Tips

### Logcat Filtering
```bash
# In Android Studio terminal
adb logcat | grep "time\|stamp\|error"

# Oppure direttamente in Logcat con filtro
com.timstamping.app
```

### File Manager - Android
```bash
# Accedere ai file dell'app
adb shell
su
cd /data/data/com.timstamping.app/files
ls -la
cat stampings/20240309_Friday.json
```

### Emulator Timezone
Se necessario cambiare timezone per test:
```bash
adb shell setprop persist.sys.timezone "Europe/Rome"
adb reboot
```

### Resettare App
```bash
# Cancellare tutti i dati
adb shell pm clear com.timstamping.app

# Riavviare app
adb shell am start -n com.timstamping.app/.MainActivity
```

## Performance Check

### Memory Profiler
1. Run → Profiler
2. Selezionare Memory
3. Aprire/chiudere dialog ripetutamente
4. Verificare no memory leaks

### Layout Inspector
1. Tools → Layout Inspector
2. Selezionare device
3. Verificare gerarchia UI corretta

## Build Variants

### Debug APK
```bash
./gradlew assembleDebug
# Output: android/app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (unsigned)
```bash
./gradlew assembleRelease
# Output: android/app/build/outputs/apk/release/app-release-unsigned.apk
```

## Checklist Finale

- [ ] App avvia correttamente
- [ ] Layout landscape fisso
- [ ] Header mostra title + subtitle
- [ ] Ora aggiorna ogni secondo
- [ ] Pulsanti IN, OUT, OPTIONS visibili
- [ ] Timbratura IN salva correttamente
- [ ] Timbratura OUT salva correttamente
- [ ] File stampings ha formato corretto
- [ ] Codici invalidi mostrano "wrong code"
- [ ] Popup si chiude dopo 3 secondi
- [ ] Tastierino fornisce input corretto
- [ ] Pulsante X chiude dialog
- [ ] Pulsante ← cancella cifre
- [ ] Codici speciali funzionano
- [ ] Nessun crash con input random
- [ ] Memory usage stabile
