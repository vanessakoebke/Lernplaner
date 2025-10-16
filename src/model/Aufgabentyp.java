package model;

import lang.I18n;

public enum Aufgabentyp {
    ALLGEMEIN, DURCHARBEITEN, EA, WIEDERHOLEN, ALTKLAUSUR, LG;
    @Override
    public String toString() {
        switch (this) {
            case ALLGEMEIN: return I18n.t("model.Aufgabentyp.Allgemein");
            case DURCHARBEITEN: return I18n.t("model.Aufgabentyp.Durcharbeiten");
            case EA: return I18n.t("model.Aufgabentyp.EA");
            case WIEDERHOLEN: return I18n.t("model.Aufgabentyp.Wiederholen");
            case ALTKLAUSUR: return I18n.t("model.Aufgabentyp.Altklausur");
            case LG: return I18n.t("model.Aufgabentyp.LG");
            default: return  I18n.t("model.Aufgabentyp.Allgemein");
        }
    }
}
