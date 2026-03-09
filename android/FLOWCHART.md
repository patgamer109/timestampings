# Diagramma di Flusso - Time Stampings App

## Stato Principale dell'App

```
┌─────────────────────────────────────────────┐
│  SCHERMATA PRINCIPALE                       │
│  ┌─────────────────────────────────────────┐│
│  │ Header: Title + Subtitle                ││
│  ├─────────────────────────────────────────┤│
│  │ Orario attuale + Data attuale           ││
│  ├─────────────────────────────────────────┤│
│  │  [IN]      [OPTIONS]      [OUT]         ││
│  │ Verde    Grigio/Rect    Rosso/Circ     ││
│  └─────────────────────────────────────────┘│
└─────────────────────────────────────────────┘
          ↓          ↓                  ↓
    Premuto IN  Premuto OPTIONS   Premuto OUT
```

## Flusso Timbratura IN/OUT

```
┌──────────────────────────────────────────┐
│ Utente preme IN / OUT                    │
└──────────────────────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│ Apre Dialog FunctionCode                 │
│ Label: "IN - CODE" o "OUT - CODE"        │
│ Mostra Tastierino Numerico               │
└──────────────────────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│ Utente inserisce 4 cifre                 │
│ (0-9, ←, X)                              │
└──────────────────────────────────────────┘
               ↓
        [4 cifre validate?]
         /              \
       SI                NO
       ↓                  ↓
    Verifica         Chiude Dialog
    in DB               ↓
     ↓           ┌─────────────────┐
     ↓           │ Apre MessageCode│
  [Trovato?]     │ "wrong code,    │
   /     \       │  please retry"  │
  SI     NO      └─────────────────┘
  ↓      ↓                ↓
  ↓      └─→ Chiude       Auto-close
  ↓           dopo        dopo
  ↓           3000ms      3000ms
  ↓
Salva in
fileName:
yyyymmdd_day.json

stampings: [
  {
    code: "XXXX",
    type: "in"/"out",
    time: "HH:mm"
  }
]
  ↓
Chiude
FunctionCode
  ↓
┌──────────────────────────┐
│ Apre MessageCode         │
│ Nome Dipendente + START  │
│ (oppure + END per OUT)   │
└──────────────────────────┘
  ↓
Auto-close
dopo 3000ms
```

## Flusso Options (Codici Speciali)

```
┌──────────────────────────────────────────┐
│ Utente preme OPTIONS                     │
└──────────────────────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│ Apre Dialog FunctionCode                 │
│ Label: "FUNCTION - CODE"                 │
│ Mostra Tastierino Numerico               │
└──────────────────────────────────────────┘
               ↓
┌──────────────────────────────────────────┐
│ Utente inserisce 4 cifre                 │
│ Verifica in specialCodes                 │
└──────────────────────────────────────────┘
               ↓
         [Valido?]
          /     \
        SI       NO
        ↓         ↓
    ┌────────┐  ┌────────────────┐
    │ Codice │  │ MessageCode    │
    │ version│  │ "wrong code"   │
    │(3401)? │  │                │
    └────────┘  └────────────────┘
        / \
       /   \
      SI   NO
      ↓     ↓
     ↓   ┌─────────────┐
     ↓   │ Codice      │
     ↓   │ download    │
     ↓   │ (3402)?     │
     ↓   └─────────────┘
     ↓       / \
     ↓      /   \
     ↓    SI     NO
     ↓    ↓       ↓
    "V"  "D"  "wrong"
    E    O
    R    W
    S    N
    I    L
    O    O
    N    A
    :    D
    1    
    .0
      ↓
  MessageCode
  mostra msg
    ↓
  Auto-close
  dopo 3000ms
```

## Gestione File

```
┌─ Caricamento Iniziale ─┐
│ config.json            │
│ dipendenti.json        │
└────────────────────────┘
         ↓↓
    [In memoria]

┌─ Durante Esecuzione ─┐
│ Salvataggio          │
│ stampings/           │
│ yyyyMMdd_day.json    │
└──────────────────────┘
    (creato se non esiste)
```

## Ciclo di Lettura/Scrittura File

```
Avvio App
    ↓
FileManager.loadConfig()  → Config in RAM
    ↓
FileManager.loadDipendenti()  → Dipendenti in RAM
    ↓
Schermata Principale
    ↓
Utente Timbratura
    ↓
FileManager.saveStamping()
    ↓
Leggi stampings odierno (o crea)
    ↓
Aggiungi record
    ↓
Scrivi file JSON
    ↓
Mostra Messaggio
```

## Stato dei Dialog

```
┌─────────────────────────┐
│ Iniziale                │
│ showFunctionCodeDialog  │
│ = false                 │
│ showMsgCodeDialog       │
│ = false                 │
└─────────────────────────┘
         ↓

    Evento             Evento
    (IN/OUT)           (OPTIONS)
       ↓                   ↓
showFunction=true     showFunction=true
dialogMode="in"       dialogMode="options"
       ↓                   ↓
    [Utente            [Utente
     inserisce]         inserisce]
       ↓                   ↓
    [4 cifre]          [4 cifre]
       ↓                   ↓
showFunction=false    showFunction=false
showMsgCode=true      showMsgCode=true
       ↓                   ↓
    [Messaggio]        [Messaggio]
       ↓                   ↓
   [Attesa 3s]        [Attesa 3s]
       ↓                   ↓
    showMsgCode=false showMsgCode=false
       ↓                   ↓
  Ritorna Principale  Ritorna Principale
```

## Validazione Codice

```
Codice Inserito "XXXX"
    ↓
┌──────────────────────────┐
│ IN/OUT Mode?             │
└──────────────────────────┘
    ↓
Cerca in dipendenti.json
    ↓
┌──────────────────────────┐
│ Trovato?                 │
│ / \                      │
│/   \                     │
SI   NO                    │
│     │                    │
│     └──→ "wrong code..."│
│          Messaggio       │
↓
Salva Timbratura
    ↓
Nome Dipendente + START/END
    ↓
Messaggio
```

```
Codice Inserito "XXXX"
    ↓
┌──────────────────────────┐
│ OPTIONS Mode?            │
└──────────────────────────┘
    ↓
Cerca in specialCodes
    ↓
┌──────────────────────────┐
│ Trovato?                 │
│ / \                      │
│/   \                     │
SI   NO                    │
│     │                    │
│     └──→ "wrong code..."│
│          Messaggio       │
↓
quale function?
    ↓
version → "VERSION: X.X"
download → "DOWNLOAD OK"
```
