package service;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lang.I18n;
import lang.Sprache;
import model.*;
import util.LocalDateAdapter;

public class Persistenz {
    private static final String DATEI_NAME_AUFGABEN = "data/aufgaben.json";
    private static final String DATEI_NAME_MODULE = "data/module.json";
    private static final String DATEI_NAME_EINSTELLUNGEN = "data/settings.json";
    private Gson gson;

    public Persistenz() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting()
                .create();
    }

    // Speichern
    public void speichern(List<Aufgabe> aufgaben) {
        try (FileWriter writer = new FileWriter(DATEI_NAME_AUFGABEN)) {
            gson.toJson(aufgaben, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, // zentriert
                    I18n.t("Common.Errors.SpeichernFehler"), // Nachricht
                    I18n.t("Common.Errors.Fehler"), // Fenstertitel
                    JOptionPane.ERROR_MESSAGE // Icon-Typ
            );
            e.printStackTrace();
        }
    }
    
    public void speichern(ModulManager modulManager) {
        try (FileWriter writer = new FileWriter(DATEI_NAME_MODULE)) {
            gson.toJson(modulManager, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, // zentriert
                    I18n.t("Common.Errors.SpeichernFehler"), // Nachricht
                    I18n.t("Common.Errors.Fehler"), // Fenstertitel
                    JOptionPane.ERROR_MESSAGE // Icon-Typ
            );
            e.printStackTrace();
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
        try (FileReader reader = new FileReader(DATEI_NAME_AUFGABEN)) {
            Type listType = new TypeToken<List<Aufgabe>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<Aufgabe>();
        }
    }
    //TODO prüfen ob notwendig
    public ModulManager moduleLaden() {
        try (FileReader reader = new FileReader(DATEI_NAME_MODULE)) {
            Type listType = new TypeToken<ModulManager>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ModulManager();
        }
    }
    
    public Einstellungen einstellungenLaden() {
        try (FileReader reader = new FileReader(DATEI_NAME_EINSTELLUNGEN)) {
            Type listType = new TypeToken<Einstellungen>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new Einstellungen(Sprache.DE, new ModulManager());
        }
    }
    
}
