package model;

import java.time.format.DateTimeFormatter;

import lang.I18n;
import lang.Sprache;
import service.ModulManager;

public class Einstellungen {
    private Sprache sprache;
    private ModulManager modulManager;
    private transient DateTimeFormatter datumsformat;

    public ModulManager getModulManager() {
        if (modulManager == null) {
            modulManager = new ModulManager();
        }
        return modulManager;
    }

    public void setModulManager(ModulManager modulManager) {
        this.modulManager = modulManager;
    }

    public Einstellungen(Sprache sprache, ModulManager modulManager) {
        this.sprache = sprache;
        this.modulManager = modulManager;
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
