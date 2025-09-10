package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lang.I18n;

public class Modul {
    private String name;
    private LocalDate klausurTermin;
    private List<Aufgabe> aufgabenliste;
    private boolean aktuell;
    private String note;
    private static int tageDurcharbeiten = 112;
    private static int tageWiederholen = 28;

    public Modul(String name, LocalDate klausurTermin, boolean aktuell, String note) {
        this.name = name;
        this.klausurTermin = klausurTermin;
        aufgabenliste = null;
        this.aktuell = aktuell;
        this.note = note;
    }

    public Modul(String name) {
        this(name, null, true, null);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        if (aktuell == true) {
            if (klausurTermin ==null) {
                return name;
            } else {
            return name + " (" + klausurTermin  + ")";
            }
        } else {
            return name + " (" + I18n.t("ui.Modulverwaltung.Note") + ": " + note;
        }
    }
    
    public LocalDate getKlausurTermin() {
        return klausurTermin;
    }
    
    public boolean getAktuell() {
        return aktuell;
    }
    
    public void setAktuell(boolean b) {
        aktuell = b;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setKlausurTermin(LocalDate termin) {
        klausurTermin = termin;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note= note;
    }
    
    public void erzeugeAufgabenliste(int seiten) {
        aufgabenliste = new ArrayList<Aufgabe>();
        LocalDate deadlineDurcharbeiten = klausurTermin.minusDays(tageWiederholen);
        aufgabenliste.add(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Durcharbeiten"), deadlineDurcharbeiten, seiten));
        aufgabenliste.add(new AufgabeWiederholen());
    }
}
