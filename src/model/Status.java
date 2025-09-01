package model;

import lang.I18n;

public enum Status {
    NEU, ANGEFANGEN, ERLEDIGT;
    @Override
    public
    String toString() {
        switch (this) {
        case ERLEDIGT: return I18n.t("model.StatusErledigt");
        case ANGEFANGEN: return I18n.t("model.StatusAngefangen");
        case NEU: return I18n.t("model.StatusNeu");
        default: return I18n.t("Common.Errors.Fehler");
        }
    }
}
