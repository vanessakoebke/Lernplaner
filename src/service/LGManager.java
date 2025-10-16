package service;

import java.util.List;

import model.Lerngruppe;

public class LGManager {
    Control control;
    List<Lerngruppe> liste;
    
    public LGManager(Control control, List<Lerngruppe> liste) {
        this.control = control;
        this.liste = liste;
    }

    public void addLg(Lerngruppe lg) {
        liste.add(lg);
    }
}
