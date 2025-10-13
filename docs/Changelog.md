# Changelog

## Version 1.2

### Features

- Modul einplanen:
  Für ein Modul können über einen Assistenten die dazugehörigen Aufgaben eingeplant werden.
  - Aufgabe Durcharbeiten: Standardmäßig wird von 7 Lektionen ausgegangen und die Lektionen werden ab Semesterbeginn im 2-Wochen-Takt eingeplant. Die Anzahl Einheiten und der Einheitentyp (Lektionen oder Kapitel) können angepasst werden und die Start- und Enddaten werden entsprechend berechnet. Die Aufgaben werden automatisch benannt als "Lektion 1",..., "Lektion n" (entsprechend Kapitel, falls Kapitel als Einheit ausgewählt wurde.
  - Einsendeaufgabe: Standardmäßig wird von 7 Einsendeaufgaben ausgegangen und die Einsendeaufgaben werden ab Semesterbeginn im 2-Wochen-Takt eingeplant. Die Anzahl der Einsendeaufgaben kann angepasst werden und die Start- und Enddaten werden entsprechend berechnet. Die Aufgaben werden automatisch benannt als "EA 1",..., "EA n".
  - Altklausuren: Für eine eingegebene Anzahl Altklausuren wird eine entsprechende Anzahl Altklausuraufgaben erstellt. Der standardmäßige Starttermin zum Bearbeiten ist das Ende der Durcharbeiten-/EA-Phase des Semester. Der standard-mäßige Endtermin ist 7 Tage vor der Klausur. Die Aufgaben sind zu Beginn unbenannt.
- Speichern- und Laden-Buttons: Die Datenbank mit den Modulen und Aufgaben kann im db-Format exportiert oder importiert werden.
- Fortschrittsansicht: Für alle Module wird angezeigt, wie viel Prozent der Aufgaben zu diesem Modul erledigt sind.
- Aufgabenarchiv: Erledigte Aufgaben werden automatisch ins Archiv verschoben.
- Ansicht für aktuelle Aufgaben: Aufgaben, deren Starttermin bereits überschritten ist, werden in der Aktuelle-Aufgabeansicht angezeigt.
- Aufgabenansichten sind jetzt sortierbar durch Klicken auf den entsprecheneden Spaltenheader.

### Bugfixes
- Der Bug, dass die falschen Aufgaben gelöscht wurden, wurde behoben.


## Version 1.1

### Features

- Neue Aufgabentypen:
  - Durcharbeiten: Hier kann angegeben werden, wie viele Seiten durchgearbeitet werden müssen.
  - Einsendeaufgabe: Hier kann nach Abschluss die Bewertung eingetragen werden.
  - Altklausur: Hier kann nach Abschluss das Ergebnis eingetragen werden.
  - Wiederholen: Hier kann der Lerneinheitstyp (z.B. Lektion, Kapitel oder Karteikarten) eingetragen werden und die Menge an Lerneinheiten eingetragen werden.
- Module:
  - Module können angelegt werden und auf aktuell oder abgeschlossen gesetzt werden.
  - Aktuellen Modulen kann ein Klausurtermin zugewiesen werden.
  - Für abgeschlossenen Module kann eine Note eingetragen werden.
  - Jeder Aufgabe kann ein Modul zugewiesen werden.

### Verbesserungen
 - Die Aufgaben und Module werden nicht mehr in JSON-Dateien sondern in einer Datenbank gespeichert.

### Bugfixes
- Die jar lässt sich jetzt auch unter Mac per Doppelklick ausführen.
