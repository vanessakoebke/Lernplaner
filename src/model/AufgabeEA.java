package model;

import java.time.LocalDate;

import lang.I18n;

public class AufgabeEA extends Aufgabe {
    private String ergebnis;

    public AufgabeEA(int nummer, LocalDate end, LocalDate start, Modul modul) {
        super("ea", I18n.t("model.Aufgabentyp.EA") + " " + nummer, "", end, start, 0, modul);
        this.ergebnis = null;
    }
    
    public String getErgebnis() {
        return ergebnis;
    }
    
    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }
    
    @Override
    public String getTyp() {
        return I18n.t("model.Aufgabentyp.EA");
    }
    

}
