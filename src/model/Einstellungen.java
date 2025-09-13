package model;

import java.time.format.DateTimeFormatter;

import lang.I18n;
import lang.Sprache;

public class Einstellungen {
    private Sprache sprache;
    private transient DateTimeFormatter datumsformat;


    public Einstellungen(Sprache sprache) {
        this.sprache = sprache;
        try {
            datumsformat = DateTimeFormatter.ofPattern(I18n.t("Common.Datumsformat_Java"));
        } catch (Exception e) {
            datumsformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        }
    }

    public Sprache getSprache() {
        return sprache;
    }

    public String getSprachcode() {
        return sprache.getCode();
    }

    public void setSprache(Sprache sprache) {
        this.sprache = sprache;
    }

    public DateTimeFormatter getDatumsformat() {
        return datumsformat;
    }

    public void initDatumsformat() {
        try {
            datumsformat = DateTimeFormatter.ofPattern(I18n.t("Common.Datumsformat_Java"));
        } catch (Exception e) {
            datumsformat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        }
    }

}
