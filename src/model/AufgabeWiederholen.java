package model;

import java.time.*;

import lang.I18n;

public class AufgabeWiederholen extends Aufgabe {
    private int einheiten;
    private Lerneinheit einheitstyp; //später noch in Konstruktoren einpassen

    AufgabeWiederholen(String name, LocalDate faelligkeit, int einheiten, Lerneinheit einheitstyp) {
        super(name, faelligkeit);
        this.einheiten = einheiten;
        this.einheitstyp = einheitstyp;
    }

    AufgabeWiederholen(String name, LocalDate faelligkeit) {
        this(name, faelligkeit, 0, Lerneinheit.EINHEIT);
    }

    AufgabeWiederholen(String name) {
        this(name, null, 0, Lerneinheit.EINHEIT);
    }

    AufgabeWiederholen() {
        this(I18n.t("model.Aufgabentyp.Wiederholen"));
    }

    public void einplanen() {
        int einheitenProTag = (int) getTageBisFaellig() / einheiten;
        for (int i = 1; i <= getTageBisFaellig(); i++) {
            addTeilaufgabe(new AufgabeWiederholen(einheitstyp.toString() + " " + i, LocalDate.now().plusDays(i), einheitenProTag, einheitstyp));
        }
    }


}
