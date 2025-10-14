package model;

import java.time.LocalDate;

import lang.I18n;

public class AufgabeEA extends Aufgabe {
    private String ergebnis;

    public AufgabeEA(String titel, String beschreibung, LocalDate end, LocalDate start, Status status, Modul modul, String ergebnis, Aufgabe folgeAufgabe) {
        super(titel, beschreibung, end, start, status, modul, folgeAufgabe);
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
