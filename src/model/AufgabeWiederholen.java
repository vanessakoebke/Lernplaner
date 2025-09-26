package model;

import java.time.*;

import lang.I18n;

public class AufgabeWiederholen extends Aufgabe {
    private int einheiten;
    private Lerneinheit einheitstyp; 

    public AufgabeWiederholen(String name, String beschreibung,  LocalDate ende, LocalDate start, Status status, Modul modul, int einheiten, Lerneinheit einheitstyp) {
        super(name,"", ende, start, status, modul);
        this.einheiten = einheiten;
        this.einheitstyp = einheitstyp;
    }


    public AufgabeWiederholen() {
        this(I18n.t("model.Aufgabentyp.Wiederholen"), "", null, null, Status.NEU, null, 0, null);
    }

    public void einplanenTaeglich() {
        int einheitenProTag = einheiten / (int) getTageBisFaellig() ;
        for (int i = 1; i <= getTageBisFaellig(); i++) {
            addTeilaufgabe(new AufgabeWiederholen(einheitstyp.toString() + " " + i, "", getStart(), getStart().plusDays(i-1), Status.NEU, getModul(), einheitenProTag, einheitstyp));
        }
    }

    public int getEinheiten() {
        return einheiten;
    }


    public void setEinheiten(int einheiten) {
        this.einheiten = einheiten;
    }


    public Lerneinheit getEinheitstyp() {
        return einheitstyp;
    }


    public void setEinheitstyp(Lerneinheit einheitstyp) {
        this.einheitstyp = einheitstyp;
    }


    @Override
    public Aufgabentyp getTyp() {
        return Aufgabentyp.WIEDERHOLEN;
    }
    

}
