package service;

import java.time.LocalDate;

import lang.I18n;
import model.Aufgabe;
import model.Modul;

public class AufgabeEA extends Aufgabe {
    private String ergebnis;

    AufgabeEA(int nummer, LocalDate end, LocalDate start, Modul modul) {
        super(I18n.t("model.Aufgabentyp.EA") + " " + nummer, "", end, start, 0, modul);
        this.ergebnis = null;
    }
    
    public String getErgebnis() {
        return ergebnis;
    }
    
    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }
}
