package lang;

public enum Sprachen {
    DE, EN;
    @Override
    public String toString() {
        switch (this) {
        case DE: return I18n.t("Common.de");
        case EN: return I18n.t("Common.en");
        default: return I18n.t("Common.de");
        }
    }
}
