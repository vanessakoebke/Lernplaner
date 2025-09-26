package service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import lang.I18n;
import model.*;

public class AufgabenManager {
    private List<Aufgabe> aufgabenListe;
    
    public AufgabenManager() {
        aufgabenListe = new ArrayList<Aufgabe>();
    }
    
    public AufgabenManager(List<Aufgabe> aufgabenListe) {
        this.aufgabenListe = aufgabenListe;
    }
    
    public void addAufgabe(Aufgabe aufgabe) {
        aufgabenListe.add(aufgabe);
    }
    
    public void deleteAufgabe(int id) {
        aufgabenListe.removeIf(a -> a.getId() == id);
    }
    
    public void updateAufgabe(Aufgabe neu, int id) {
        for (Aufgabe a: aufgabenListe) {
            if (a.getId() == id) {
                a.update(neu);
                return;
            }
        }
    }

    public List<Aufgabe> getAufgabenListe() {
        return aufgabenListe;
    }
    
    public List<Aufgabe> getAufgabenListe(Modul modul) {
        List<Aufgabe> result = new ArrayList<>();
        for (Aufgabe a : aufgabenListe) {
            if (a.getModul().equals(modul)) {
                result.add(a);
            }
        }
        return result;
    }
    
    public void erzeugeAufgabenlisteDurcharbeiten(Modul modul, int seiten ) {
        LocalDate ende = modul.getKlausurTermin().minusDays(modul.getTageWiederholen());
        int seitenProLektion = seiten / 7;
        int tageProLektion = (int) Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), ende));;
        for (int i = 1; i <=7; i++) {
            aufgabenListe.add(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Durcharbeiten") + ": " + I18n.t("model.Lerneinheiten.Lektion") + " " + i, "", ende, LocalDate.now().plusDays(tageProLektion * (i-1)), Status.NEU, modul, seitenProLektion));
        }
    }
    
    public void erzeugeAufgabenlisteDurcharbeiten(Modul modul, int[] seiten ) {
        LocalDate ende = modul.getKlausurTermin().minusDays(modul.getTageWiederholen());
        int tageProLektion = (int) Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), ende));
        for (int i = 1; i <=seiten.length; i++) {
            aufgabenListe.add(new AufgabeDurcharbeiten(I18n.t("model.Aufgabentyp.Durcharbeiten") + ": " + I18n.t("model.Lerneinheiten.Lektion") + " " + i, "", LocalDate.now().plusDays(tageProLektion * i), LocalDate.now().plusDays(tageProLektion * (i-1)),  Status.NEU, modul, seiten[i-1]));
        }
    }
    
    public void erzeugeAufgabenListeAltklausuren(Modul modul, List<String> altklausuren) {
        int tageProKlausur = (int) Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), modul.getKlausurTermin()));
        LocalDate start = modul.getKlausurTermin().minusDays(modul.getTageWiederholen());
        for (int i = 1; i<= altklausuren.size(); i++) {
            aufgabenListe.add(new AufgabeAltklausur(altklausuren.get(i-1), "", start.plusDays(tageProKlausur), start, Status.NEU, modul, ""));
            start = start.plusDays(tageProKlausur);
        }
    }
    
    public void erzeugeAufgabenlisteEAs(Modul modul, List<LocalDate> eas) {
        LocalDate start = LocalDate.now();
        for (int i =1; i <= eas.size(); i++) {
            LocalDate ende = eas.get(i-1);
            aufgabenListe.add(new AufgabeEA(I18n.t("Model.Aufgabentyp.EA") + ": " + String.valueOf(i), null, ende, start, Status.NEU, modul, null));
            start = ende;
        }
    }
    
}
