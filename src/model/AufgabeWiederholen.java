package model;

import java.time.*;

import lang.I18n;

public class AufgabeWiederholen extends Aufgabe {
    private int einheiten;
    private Lerneinheit einheitstyp; 

    AufgabeWiederholen(String name, LocalDate ende, LocalDate start, Modul modul, int einheiten, Lerneinheit einheitstyp) {
        super("wiederholen", name,"", ende, start, 0, modul);
        this.einheiten = einheiten;
        this.einheitstyp = einheitstyp;
    }


    public AufgabeWiederholen() {
        this(I18n.t("model.Aufgabentyp.Wiederholen"), null, null, null, 0,null);
    }

    public void einplanenTaeglich() {
        int einheitenProTag = einheiten / (int) getTageBisFaellig() ;
        for (int i = 1; i <= getTageBisFaellig(); i++) {
            addTeilaufgabe(new AufgabeWiederholen(einheitstyp.toString() + " " + i, getStart(), getStart().plusDays(i-1), getModul(), einheitenProTag, einheitstyp));
        }
    }

    @Override
    public String getTyp() {
        return I18n.t("model.Aufgabentyp.Wiederholen");
    }
    

}
