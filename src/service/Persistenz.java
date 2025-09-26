package service;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lang.I18n;
import lang.Sprache;
import model.*;
import util.LocalDateAdapter;
import util.RuntimeTypeAdapterFactory;

public class Persistenz {
    private static final String DATEI_NAME_EINSTELLUNGEN = "data/settings.json";
    private Gson gson;

    public Persistenz() {
        gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();
    }


    // Speichern
    public void speichern(List<Aufgabe> aufgaben) {
        DatenbankService db = new DatenbankService();
        db.init();
        for (Aufgabe a : aufgaben) {
            db.upsertAufgabe(a);
        }
    }

    public void speichern(ModulManager modulManager) {
        DatenbankService db = new DatenbankService();
        for (Modul modul : modulManager.getAlleModule()) {
            db.upsertModul(modul);
        }
    }


    public void speichern(Einstellungen einstellungen) {
        try (FileWriter writer = new FileWriter(DATEI_NAME_EINSTELLUNGEN)) {
            gson.toJson(einstellungen, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, // zentriert
                    I18n.t("Common.Errors.SpeichernFehler"), // Nachricht
                    I18n.t("Common.Errors.Fehler"), // Fenstertitel
                    JOptionPane.ERROR_MESSAGE // Icon-Typ
            );
            e.printStackTrace();
        }
    }

    // Laden


    public List<Aufgabe> aufgabenLaden() {
        DatenbankService db = new DatenbankService();
        db.init();
        return db.getAufgaben();
    }


    public ModulManager moduleLaden() {
        DatenbankService db = new DatenbankService();
        db.init();
        ModulManager modulManager = new ModulManager(db);
        
        List<Modul> module = db.getModule(); // holt alles aus der DB
        for (Modul modul : module) {
            modulManager.addModul(modul);
        }
        
        return modulManager;
    }


    public Einstellungen einstellungenLaden() {
        Einstellungen einstellungen;
        try (FileReader reader = new FileReader(DATEI_NAME_EINSTELLUNGEN)) {
            einstellungen = gson.fromJson(reader, Einstellungen.class);
        } catch (IOException e) {
            // Wenn Datei nicht existiert oder Fehler beim Lesen
            einstellungen = new Einstellungen(Sprache.DE);
        }
        einstellungen.initDatumsformat(); // falls du diese Methode in Einstellungen anlegst
        return einstellungen;
    }
}
