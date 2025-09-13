package model;

import java.io.Serializable;
import java.time.LocalDate;

import lang.I18n;


public class AufgabeAllgemein extends Aufgabe implements Serializable {


    public AufgabeAllgemein(String titel, String beschreibung, LocalDate ende) {
        this(titel, beschreibung, ende, LocalDate.now(),  0, null);
    }

    public AufgabeAllgemein(String titel, String beschreibung) {
        this(titel, beschreibung, null);
    }

    public AufgabeAllgemein(String titel, LocalDate ende) {
        this(titel, null, ende);
    }
    
    public AufgabeAllgemein(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, Modul modul) {
        super("allgemein", titel, beschreibung, ende, start, status, modul);
    }



    public AufgabeAllgemein(String titel) {
        this(titel, null, null);
    }
    
    @Override
    public String getTyp() {
        return I18n.t("model.Aufgabentyp.Allgemein");
    }

}