package model;

import java.time.LocalDate;

public class AufgabeLerngruppe extends Aufgabe{

    public AufgabeLerngruppe(String titel, String beschreibung, LocalDate ende, LocalDate start, Status status,
            Modul modul, Aufgabe folgeAufgabe) {
        super(titel, beschreibung, ende, start, status, modul, folgeAufgabe);
    }

    @Override
    public Aufgabentyp getTyp() {

        return Aufgabentyp.LG;
    }
}
