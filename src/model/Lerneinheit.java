package model;

import lang.I18n;

public enum Lerneinheit {
    EINHEIT, KAPITEL, LEKTION, KARTEN, MODUL, ALTKLAUSUR;

    @Override
    public String toString() {
        switch (this) {
        case ALTKLAUSUR:
            return I18n.t("model.Lerneinheiten.Altklausur");
        case KAPITEL:
            return I18n.t("model.Lerneinheiten.Kapitel");
        case LEKTION:
            return I18n.t("model.Lerneinheiten.Lektion");
        case KARTEN:
            return I18n.t("model.Lerneinheiten.Karten");
        case MODUL:
            return I18n.t("model.Lerneinheiten.Modul");
        default:
            return I18n.t("model.Lerneinheiten.Einheit");
        }
    }
}
