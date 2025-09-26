package model;

import java.time.LocalDate;

import lang.I18n;

public class AufgabeEA extends Aufgabe {
    private String ergebnis;

    public AufgabeEA(String nummer, String beschreibung, LocalDate end, LocalDate start, Status status, Modul modul, String ergebnis) {
        super(I18n.t("model.Aufgabentyp.EA") + " " + nummer, "", end, start, status, modul);
        this.ergebnis = ergebnis;
    }
    
    public String getErgebnis() {
        return ergebnis;
    }
    
    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }
    
    @Override
    public Aufgabentyp getTyp() {
        return Aufgabentyp.EA;
    }
    

}
