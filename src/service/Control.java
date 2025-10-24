package service;

import java.io.File;

import lang.I18n;
import model.*;

public class Control {
    private AufgabenManager am;
    private ModulManager mm;
    private Einstellungen einstellungen;
    private Persistenz persistenz;
    private Datenbank db;
    private LGManager lm;
    
    public Control() {
        new File("data").mkdirs();
        this.db = new Datenbank(this);
        db.init();
        this.persistenz =  new Persistenz(this);
        this.am = new AufgabenManager(persistenz.aufgabenLaden(), this);
        this.mm = persistenz.moduleLaden();
        this.lm = new LGManager(this, db.getLerngruppen());
        this.einstellungen = persistenz.einstellungenLaden();
        I18n.load(einstellungen.getSprachcode());
    }
    
    public Datenbank getDb() {
        return db;
    }

    public AufgabenManager getAm() {
        return am;
    }

    public void setAm(AufgabenManager am) {
        this.am = am;
    }
    
    public LGManager getLm() {
        return lm;
    }

    public void setLm(LGManager lm) {
        this.lm = lm;
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
        this.am = new AufgabenManager(persistenz.aufgabenLaden(), this);
        this.mm = persistenz.moduleLaden();

        // Einstellungen neu laden
        this.einstellungen = persistenz.einstellungenLaden();

        // Sprache aktualisieren
        I18n.load(einstellungen.getSprachcode());

        System.out.println("✅ Daten wurden erfolgreich neu geladen.");
    }



    
}
