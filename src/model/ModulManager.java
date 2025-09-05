package model;

import java.io.Serializable;
import java.time.*;
import java.util.*;

import lang.I18n;

public class ModulManager implements Serializable {
    private static ModulManager modulManager;   
    private List<Modul> modulliste;     
    private static int tageDurcharbeiten = 112;
    private static int tageWiederholen = 28;

    private ModulManager() {
        modulliste = new ArrayList<>();
    }

    public static ModulManager getModulManager() {
        if (modulManager == null) {
            modulManager = new ModulManager();
        }
        return modulManager;
    }

    public List<Modul> getAlleModule() {
        return modulliste;
    }
    
    public List<Modul> getAktuelleModule() {
        List<Modul> aktuelleModule = new ArrayList<>();
        for (Modul modul: modulliste) {
            if (modul.getAktuell()==true) {
                aktuelleModule.add(modul);
            }
        }
        return aktuelleModule;
    }
    
    public void addModul(String name) {
        modulliste.add(new Modul(name));
    }
    
    public void addModul(String name, LocalDate klausurTermin) {
        modulliste.add(new Modul(name, klausurTermin));
    }
    
    private static class Modul {
        private String name;
        private LocalDate klausurTermin;
        private List<Aufgabe> aufgabenliste;
        private boolean aktuell;

        private Modul(String name, LocalDate klausurTermin) {
            this.name = name;
            this.klausurTermin = klausurTermin;
            aufgabenliste = null;
            aktuell = true;
        }

        private Modul(String name) {
            this(name, null);
        }
        
        private String getName() {
            return name;
        }
        
        private LocalDate getKlausurTermin() {
            return klausurTermin;
        }
        
        private boolean getAktuell() {
            return aktuell;
        }
        
        private void setAktuell(boolean b) {
            aktuell = b;
        }
        
        private void setName(String name) {
            this.name = name;
        }
        
        private void setKlausurTermin(LocalDate termin) {
            klausurTermin = termin;
        }
        
        private void erzeugeAufgabenliste(int seiten) {
            aufgabenliste = new ArrayList<Aufgabe>();
            LocalDate deadlineDurcharbeiten = klausurTermin.minusDays(tageWiederholen);
            aufgabenliste.add(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Durcharbeiten"), deadlineDurcharbeiten, seiten));
            aufgabenliste.add(new AufgabeWiederholen());
        }
    }
    
}
