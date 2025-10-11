package service;

import java.io.File;

import lang.I18n;
import model.Einstellungen;

public class Control {
    private AufgabenManager am;
    private ModulManager mm;
    private Einstellungen einstellungen;
    private Persistenz persistenz;
    private DatenbankService db;
    
    public Control() {
        new File("data").mkdirs();
        this.db = new DatenbankService();
        db.init();
        this.persistenz =  new Persistenz();
        this.am = new AufgabenManager(persistenz.aufgabenLaden());
        this.mm = persistenz.moduleLaden();
        this.einstellungen = persistenz.einstellungenLaden();
        I18n.load(einstellungen.getSprachcode());
    }
    
    public DatenbankService getDb() {
        return db;
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
    
    
    public void reloadData() {
        System.out.println("🔄 Daten werden neu geladen...");

        // Verbindung neu initialisieren
        db.init();

        // Aufgaben und Module neu laden
        this.am = new AufgabenManager(persistenz.aufgabenLaden());
        this.mm = persistenz.moduleLaden();

        // Einstellungen neu laden
        this.einstellungen = persistenz.einstellungenLaden();

        // Sprache aktualisieren
        I18n.load(einstellungen.getSprachcode());

        System.out.println("✅ Daten wurden erfolgreich neu geladen.");
    }

    
}
