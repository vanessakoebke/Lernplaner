package service;

import java.io.File;

import lang.I18n;
import model.Einstellungen;

public class Control {
    private AufgabenManager am;
    private ModulManager mm;
    private Einstellungen einstellungen;
    private Persistenz persistenz;
    
    public Control() {
        new File("data").mkdirs();
        this.persistenz =  new Persistenz();
        this.am = new AufgabenManager(persistenz.aufgabenLaden());
        this.mm = persistenz.moduleLaden();
        this.einstellungen = persistenz.einstellungenLaden();
        I18n.load(einstellungen.getSprachcode());
    }

    public AufgabenManager getAm() {
        return am;
    }

    public void setAm(AufgabenManager am) {
        this.am = am;
    }

    public ModulManager getMm() {
        return mm;
    }

    public void setMm(ModulManager mm) {
        this.mm = mm;
    }

    public Einstellungen getEinstellungen() {
        return einstellungen;
    }

    public void setEinstellungen(Einstellungen einstellungen) {
        this.einstellungen = einstellungen;
    }

    public Persistenz getPersistenz() {
        return persistenz;
    }

    public void setPersistenz(Persistenz persistenz) {
        this.persistenz = persistenz;
    }
    
    
    
    
}
