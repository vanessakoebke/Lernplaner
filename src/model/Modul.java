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
    private static int tageDurcharbeiten = 112;
    private static int tageWiederholen = 28;

    public Modul(String name, LocalDate klausurTermin, boolean aktuell) {
        this.name = name;
        this.klausurTermin = klausurTermin;
        aufgabenliste = null;
        this.aktuell = aktuell;
    }

    public Modul(String name) {
        this(name, null, true);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
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
    
    public void erzeugeAufgabenliste(int seiten) {
        aufgabenliste = new ArrayList<Aufgabe>();
        LocalDate deadlineDurcharbeiten = klausurTermin.minusDays(tageWiederholen);
        aufgabenliste.add(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Durcharbeiten"), deadlineDurcharbeiten, seiten));
        aufgabenliste.add(new AufgabeWiederholen());
    }
}
