package model;

import lang.I18n;

public enum Aufgabentyp {
    ALLGEMEIN, DURCHARBEITEN, EA, WIEDERHOLEN, ALTKLAUSUR;
    @Override
    public String toString() {
        switch (this) {
            case ALLGEMEIN: return I18n.t("model.Aufgabentyp.Allgemein");
            case DURCHARBEITEN: return I18n.t("model.Aufgabentyp.Durcharbeiten");
            case EA: return I18n.t("model.Aufgabentyp.EA");
            case WIEDERHOLEN: return I18n.t("model.Aufgabentyp.Wiederholen");
            case ALTKLAUSUR: return I18n.t("model.Aufgabentyp.Altklausur");
            default: return  I18n.t("model.Aufgabentyp.Allgemein");
        }
    }
}
