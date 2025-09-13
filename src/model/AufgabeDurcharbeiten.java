package model;

import java.time.*;
import java.util.*;

import lang.I18n;

public class AufgabeDurcharbeiten extends Aufgabe {
    private int seiten;

    public AufgabeDurcharbeiten(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, Modul modul, int seiten) {
        super("durcharbeiten", titel, beschreibung, ende, start, status, modul);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, int seiten, Modul modul,
            int anzahlTeilaufgaben) {
        super("durcharbeiten", titel, beschreibung, ende, start, status, modul);
        this.seiten = seiten;
        int seitenzahlProAufgabe = seiten / anzahlTeilaufgaben;
        long tageProAufgabe = this.getTageBisFaellig() / anzahlTeilaufgaben;
        LocalDate startProAufgabe = LocalDate.now();
        LocalDate endeProAufgabe = LocalDate.now();
        for (int i = 1; i <= anzahlTeilaufgaben; i++) {
            endeProAufgabe = endeProAufgabe.plusDays(tageProAufgabe);
            addTeilaufgabe(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Lektion") + " " + i, "", endeProAufgabe, startProAufgabe, 0, modul, seitenzahlProAufgabe));
            startProAufgabe = startProAufgabe.plusDays(tageProAufgabe);
        }
    }
    
    public int getSeiten() {
        if (getTeilaufgaben() == null) {
            return seiten;
        } else {
            int summe = 0;
            for (Aufgabe a : getTeilaufgaben()) {
                try {
                    AufgabeDurcharbeiten ad = (AufgabeDurcharbeiten) a;
                    summe = summe + ad.getSeiten();
                } catch (Exception e) {
                    //Wenn eine der Teilaufgaben keine Aufgabe mit Seitenangabe ist, soll einfach normal weitergemacht werden, es wird dann nichts zu Summe addiert.
                }
            }
            return summe;
        }
    }
    
    public void updateSeiten() {
        int summe = 0;
        for (Aufgabe a : getTeilaufgaben()) {
            try {
                AufgabeDurcharbeiten ad = (AufgabeDurcharbeiten) a;
                summe = summe + ad.getSeiten();
            } catch (Exception e) {
                //Wenn eine der Teilaufgaben keine Aufgabe mit Seitenangabe ist, soll einfach normal weitergemacht werden, es wird dann nichts zu Summe addiert.
            }
        }
        seiten = summe;
    }
    @Override
    public String getTyp() {
        return I18n.t("model.Aufgabentyp.Durcharbeiten");
    }
    

}
