package lang;

public enum Sprache {
    DE, EN;
    @Override
    public String toString() {
        switch (this) {
        case DE: return I18n.t("Common.de");
        case EN: return I18n.t("Common.en");
        default: return I18n.t("Common.de");
        }
    }
    public String getCode() {
        switch (this) {
        case DE: return "de";
        case EN: return "en";
        default: return "de";
        }
    }
}
