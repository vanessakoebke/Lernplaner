package model;

import lang.I18n;

enum Lerneinheit {
    EINHEIT, KAPITEL, LEKTION, KARTEN, MODUL;

    @Override
    public String toString() {
        switch (this) {
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
