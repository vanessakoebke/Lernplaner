package model;

import java.time.*;
import java.util.*;

import lang.I18n;

public class AufgabeDurcharbeiten extends Aufgabe {
    private int seiten;

    public AufgabeDurcharbeiten(String titel, String beschreibung, LocalDate faelligkeit, int status, int seiten) {
        super(titel, beschreibung, faelligkeit, status);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, LocalDate faelligkeit, int status, int seiten) {
        super(titel, "", faelligkeit, status);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, LocalDate faelligkeit, int seiten) {
        super(titel, "", faelligkeit, 0);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, String beschreibung, LocalDate faelligkeit, int seiten) {
        super(titel, beschreibung, faelligkeit, 0);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, String beschreibung, int seiten) {
        super(titel, beschreibung, null, 0);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, int seiten) {
        super(titel, "", null, 0);
        this.seiten = seiten;
    }

    public AufgabeDurcharbeiten(String titel, String beschreibung, LocalDate faelligkeit, int status, int seiten,
            int anzahlTeilaufgaben) {
        super(titel, beschreibung, faelligkeit, status);
        this.seiten = seiten;
        int seitenzahlProAufgabe = seiten / anzahlTeilaufgaben;
        long tageProAufgabe = this.getTageBisFaellig() / anzahlTeilaufgaben;
        LocalDate faelligkeitProAufgabe = LocalDate.now();
        for (int i = 1; i <= anzahlTeilaufgaben; i++) {
            faelligkeitProAufgabe = faelligkeitProAufgabe.plusDays(tageProAufgabe);
            addTeilaufgabe(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Lektion") + " " + i, faelligkeitProAufgabe, seitenzahlProAufgabe));
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
}
