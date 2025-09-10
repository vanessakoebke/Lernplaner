package service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Modul;

public class ModulManager implements Serializable {   
    private List<Modul> modulliste;     


    public ModulManager() {
        modulliste = new ArrayList<>();
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
        modulliste.add(modul);
    }

    public void removeModul(Modul modul) {
        modulliste.remove(modul);
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
    
}
