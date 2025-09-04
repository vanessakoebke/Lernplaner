package model;

import lang.I18n;

public enum Status {
    NEU, ANGEFANGEN, ERLEDIGT;
    @Override
    public String toString() {
        switch (this) {
        case ERLEDIGT: return I18n.t("model.StatusErledigt");
        case ANGEFANGEN: return I18n.t("model.StatusAngefangen");
        case NEU: return I18n.t("model.StatusNeu");
        default: return I18n.t("Common.Errors.Fehler");
        }
    }
    public static Status fromInt(int i) {
        switch (i) {
        case 0: return NEU;
        case 1: return ANGEFANGEN;
        case 2: return ERLEDIGT;
        default: return NEU;
        }
    }
    public int toInt() {
        switch (this) {
        case ERLEDIGT: return 2;
        case ANGEFANGEN: return 1;
        case NEU: return 0;
        default: return 0;
        }
    }
}
