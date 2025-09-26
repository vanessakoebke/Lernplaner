package model;

import java.time.LocalDate;

public class AufgabeAltklausur extends Aufgabe {

    private String ergebnis;
    
    public AufgabeAltklausur(String titel, String beschreibung, LocalDate ende, LocalDate start, Status status, Modul modul, String ergebnis){
        super(titel, beschreibung, ende, start, status, modul);
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
        return Aufgabentyp.ALTKLAUSUR;
    }
    
}
