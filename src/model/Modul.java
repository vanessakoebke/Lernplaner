package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lang.I18n;

public class Modul {
    private String name;
    private LocalDate klausurTermin;
    private boolean aktuell;
    private String note;
    private int tageWiederholen = 30;

    public Modul(String name, LocalDate klausurTermin, boolean aktuell, String note) {
        this.name = name;
        this.klausurTermin = klausurTermin;
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
    
    public int getTageWiederholen() {
        return tageWiederholen;
    }
    
    public void setTageWiederholen(int tageWiederholen) {
        this.tageWiederholen = tageWiederholen;
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
    
    
}
