package service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Modul;

public class ModulManager implements Serializable {   
    private List<Modul> modulliste;    
    private DatenbankService db;


    public ModulManager(DatenbankService db) {
        this.db = db;
        modulliste = new ArrayList<>();
    }

    private void ladeModule() {
        try (Connection conn = db.connect()) {
            modulliste = db.getModule(); // Annahme: diese Methode liefert List<Modul>
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Modul> getAlleModule() {
        return modulliste;
    }
    
    public List<Modul> getAktuelleModule() {
        List<Modul> aktuelleModule = new ArrayList<>();
        for (Modul modul: modulliste) {
            if (modul.getAktuell()==true) {
                aktuelleModule.add(modul);
            }
        }
        return aktuelleModule;
    }
    
    public void addModul(Modul modul) {
        upsertModul(modul);
    }

    public void removeModul(Modul modul) {
            db.deleteModul(modul);
            modulliste.removeIf(m -> m.getName().equals(modul.getName()));
    }

    public List<Modul> getAlteModule() {
        List<Modul> alteModule = new ArrayList<>();
        for (Modul modul: modulliste) {
            if (modul.getAktuell()==false) {
                alteModule.add(modul);
            }
        }
        return alteModule;
    }
    
    public void upsertModul(Modul modul) {
        try (Connection conn = db.connect()) {
            db.upsertModul(modul); // Die Upsert-Methode aus DatenbankService
            // Cache aktualisieren
            modulliste.removeIf(m -> m.getName().equals(modul.getName()));
            modulliste.add(modul);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
