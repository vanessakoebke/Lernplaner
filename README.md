# Lernplaner

**Lernplaner** ist eine kleine Desktop-Anwendung, die das Planen und Verwalten von Studiums-bezogenen Aufgaben erleichtert. Ziel ist es, einen einfachen Überblick über Aufgaben, Termine, Fortschritt und Leistungen zu behalten. Die Anwendung ist auf die besonderen Anforderungen beim Studium an der FernUniversität in Hagen optimiert.

## Features

- Aufgaben hinzufügen, bearbeiten und löschen
- Sortierbare Aufgabenübersicht, verschiedene Ansicht (aktuelle Aufgaben, Archiv, alle Aufgaben)
- Module verwalten und einplanen
    - Prüfungstermine verwalten
    - automatisches Einplanen der Aufgabentypen *Skript durcharbeiten*, *Einsendeaufgaben* und *Altklausuren* bis zum Prüfungstermin
    - Notenübersicht
    - Fortschrittsbalken pro Modul
- Lerngruppen hinzufügen, bearbeiten und löschen inkl. automatischem Erstellen der Aufgaben *Lerngruppe vorbereiten*
- Speicherung der Aufgaben in einer lokalen Datenbank-Datei
- Exportieren und Importieren der Aufgaben

## Anforderungen

- **Java 21 oder neuer**

## Installation

### Kompilierte Version

- Eine fertige JAR-Datei ist verfübar.  
- Alternativ kann das Projekt lokal gebaut werden (siehe folgender Punkt).

### Aus dem Quellcode

1. Repository klonen:  
    git clone https://github.com/vanessakoebke/Lernplaner.git
2. In Eclipse oder IntelliJ importieren.  
3. Projekt bauen und ausführen (`ui.Main` als Main-Klasse).

## Nutzung

- Programm starten: Doppelklick auf die jar oder Main.java starten aus einer IDE
- Aufgaben über die GUI verwalten  
- Änderungen werden automatisch in `data/Lernplaner.db` gespeichert

## Hinweise

- Alle Daten werden lokal gespeichert, es gibt keine Cloud-Synchronisation.  
- Bitte Java 21+ verwenden, da ältere Versionen Probleme verursachen können.

## Coming soon

[Zukünftig geplante Features](https://github.com/vanessakoebke/Lernplaner/blob/main/docs/Anforderungen.md)

## Kontakt / Support

Für Fragen, Anregungen oder Feedback: [GitHub Issues](https://github.com/vanessakoebke/Lernplaner/issues)

## Disclaimer: Verwendung von ChatGPT
Die GUI-Elemente wurden mit Hilfe von ChatGPT erstellt.

## Lizenz
Dieses Projekt steht unter der MIT-Lizenz – siehe die Datei LICENSE für weitere Details.
