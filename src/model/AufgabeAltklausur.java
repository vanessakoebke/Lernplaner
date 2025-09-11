package model;

import java.time.LocalDate;

import lang.I18n;

public class AufgabeAltklausur extends Aufgabe {
    private String semester;
    private String ergebnis;
    
    public AufgabeAltklausur(LocalDate ende, LocalDate start, Modul modul, String semester){
        super(I18n.t("model.Lerneinheiten.Altklausur") + " " + semester, "", ende, start, 0, modul);
        this.semester = semester;
        this.ergebnis = null;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(String ergebnis) {
        this.ergebnis = ergebnis;
    }
}
