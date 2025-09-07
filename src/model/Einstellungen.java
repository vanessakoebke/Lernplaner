package model;

import lang.Sprache;
import service.ModulManager;

public class Einstellungen {
    private Sprache sprache;
    private ModulManager modulManager;
    
    public ModulManager getModulManager() {
        if (modulManager == null) {
            modulManager = new ModulManager();
        }
        return modulManager;
    }

    public void setModulManager(ModulManager modulManager) {
        this.modulManager = modulManager;
    }

    public Einstellungen(Sprache sprache, ModulManager modulManager){
        this.sprache = sprache;
        this.modulManager = modulManager;
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
    
}
