package service;

import com.google.gson.*;
import com.google.gson.reflect.*;

import lang.I18n;
import model.*;
import service.*;
import util.*;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;

public class Persistenz {
    private static final String DATEI_NAME = "data/lernplaner.json";
    private Gson gson;

    public Persistenz() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting()
                .create();
    }

    // Speichern
    public void speichern(List<Aufgabe> aufgaben) {
        try (FileWriter writer = new FileWriter(DATEI_NAME)) {
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

    // Laden
    public List<Aufgabe> laden() {
        try (FileReader reader = new FileReader(DATEI_NAME)) {
            Type listType = new TypeToken<List<Aufgabe>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<Aufgabe>();
        }
    }
}
