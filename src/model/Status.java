package model;

import lang.I18n;

public enum Status {
    NEU, ANGEFANGEN, WARTEND, ERLEDIGT;
    @Override
    public String toString() {
        switch (this) {
        case ERLEDIGT: return I18n.t("model.StatusErledigt");
        case ANGEFANGEN: return I18n.t("model.StatusAngefangen");
        case WARTEND: return I18n.t("model.StatusWartend");
        case NEU: return I18n.t("model.StatusNeu");
        default: return I18n.t("Common.Errors.Fehler");
        }
    }
    public static Status fromInt(int i) {
        switch (i) {
        case 0: return NEU;
        case 1: return ANGEFANGEN;
        case 2: return WARTEND;
        case 3: return ERLEDIGT;
        default: return NEU;
        }
    }
    public int toInt() {
        switch (this) {
        case NEU: return 0;
        case ANGEFANGEN: return 1;
        case WARTEND: return 2;
        case ERLEDIGT: return 3;
        default: return 0;
        }
    }
}
