package service;

import java.util.List;

import model.Lerngruppe;
import model.Lerngruppe.Termin;

public class LGManager {
    Control control;
    List<Lerngruppe> liste;
    
    public LGManager(Control control, List<Lerngruppe> liste) {
        this.control = control;
        this.liste = liste;
    }
    
    public List<Lerngruppe> getLerngruppen(){
        return liste;
    }

    public void addLg(Lerngruppe lg) {
        liste.add(lg);
    }
    
    public void deleteLg(Lerngruppe lg) {
        for (Termin t: lg.getTermine()) {
            control.getDb().deleteAufgabe(t.getAufgabe());
            control.getAm().deleteAufgabe(t.getAufgabe().getId());
        }
        liste.remove(lg);
    }
}
