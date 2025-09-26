# Lernplaner

**Lernplaner** ist eine kleine Desktop-Anwendung, die das Planen und Verwalten von Aufgaben erleichtert. Ziel ist es, einen einfachen Überblick über Aufgaben und Termine zu behalten. Die Anwendung wird in Zukunft auf die besonderen Aufgabentypen beim Fernstudium an der FernUniversität in Hagen optimiert.

## Features

- Aufgaben hinzufügen, bearbeiten und löschen
- Speicherung der Aufgaben in einer lokalen Datenbank-Datei

## Anforderungen

- **Java 8 oder neuer**
- Optional: Gson-Library (für JSON-Verarbeitung, falls nicht im Build eingebunden)

## Installation

### Kompilierte Version

- Eine fertige JAR-Datei ist verfübar.  
- Alternativ kann das Projekt lokal gebaut werden (siehe folgender Punkt).

### Aus dem Quellcode

1. Repository klonen:  
    git clone https://github.com/vanessakoebke/Lernplaner.git
2. In Eclipse oder IntelliJ importieren (als Maven/Java-Projekt).  
3. Projekt bauen und ausführen (`ui.Main` als Main-Klasse).

## Nutzung

- Programm starten: Doppelklick auf die jar oder Main.java starten aus einer IDE
- Aufgaben über die GUI verwalten  
- Änderungen werden automatisch in `data/Lernplaner.db` gespeichert

## Hinweise

- Alle Daten werden lokal gespeichert, es gibt keine Cloud-Synchronisation.  
- Bitte Java 8+ verwenden, da ältere Versionen Probleme verursachen können.

## Coming soon

- [Zukünftig geplante Features](https://github.com/vanessakoebke/Lernplaner/blob/main/docs/Anforderungen.md)

## Kontakt / Support

- Für Fragen, Anregungen oder Feedback: [GitHub Issues](https://github.com/vanessakoebke/Lernplaner/issues)
