package ui;

import java.io.File;

import lang.I18n;
import model.Einstellungen;
import service.*;

public class Main {
    public static void main(String[] args) {
        // Verzeichnisse & Sprache
        new File("data").mkdirs();
        // Persistenz
        Persistenz persistenz = new Persistenz();
        Einstellungen einstellungen = persistenz.einstellungenLaden();
        I18n.load(einstellungen.getSprachcode());
        AufgabenManager aufgabenManager = new AufgabenManager(persistenz.aufgabenLaden());
        new Hauptfenster(persistenz, aufgabenManager, einstellungen);

    }
}
