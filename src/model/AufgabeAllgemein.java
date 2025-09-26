package model;

import java.io.Serializable;
import java.time.LocalDate;

import lang.I18n;


public class AufgabeAllgemein extends Aufgabe implements Serializable {


    public AufgabeAllgemein(String titel, String beschreibung, LocalDate ende) {
        this(titel, beschreibung, ende, LocalDate.now(),  Status.NEU, null);
    }

    public AufgabeAllgemein(String titel, String beschreibung) {
        this(titel, beschreibung, null);
    }

    public AufgabeAllgemein(String titel, LocalDate ende) {
        this(titel, null, ende);
    }
    
    public AufgabeAllgemein(String titel, String beschreibung, LocalDate ende, LocalDate start, Status status, Modul modul) {
        super(titel, beschreibung, ende, start, status, modul);
    }



    public AufgabeAllgemein(String titel) {
        this(titel, null, null);
    }
    
    @Override
    public Aufgabentyp getTyp() {
        return Aufgabentyp.ALLGEMEIN;
    }

}