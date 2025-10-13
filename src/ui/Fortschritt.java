package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.*;
import service.Control;

public class Fortschritt extends JPanel implements IAnsicht {
    private Control control;

    // Hilfsliste, um ProgressBars mit Modulen zu verknüpfen
    private final List<JProgressBar> fortschrittBars = new ArrayList<>();
    private final List<Modul> moduleListe = new ArrayList<>();

    public Fortschritt(Control control) {
        this.control = control;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (Modul modul : control.getMm().getAktuelleModule()) {
            moduleListe.add(modul);

            JPanel panel = new JPanel(new BorderLayout());
            JLabel titel = new JLabel(modul.getName());
            panel.add(titel, BorderLayout.NORTH);

            JProgressBar fortschritt = new JProgressBar(0, 100);
            fortschritt.setValue(getProzent(modul));
            fortschritt.setStringPainted(true);
            fortschritt.setPreferredSize(new Dimension(200, 30)); // 200 = Breite, 30 = Höhe
            panel.add(fortschritt, BorderLayout.CENTER);

            fortschrittBars.add(fortschritt);

            this.add(panel, gbc);
            gbc.gridy++;
        }
    }

    @Override
    public void refresh() {
        for (int i = 0; i < fortschrittBars.size(); i++) {
            Modul modul = moduleListe.get(i);
            JProgressBar bar = fortschrittBars.get(i);
            bar.setValue(getProzent(modul));
        }
        revalidate();
        repaint();
    }

    private int getProzent(Modul modul) {
        List<Aufgabe> liste = control.getAm().getAufgabenListe(modul);
        if (liste.isEmpty()) return 0;

        long erledigt = liste.stream()
                .filter(a -> a != null && a.getStatus() == Status.ERLEDIGT)
                .count();

        return (int) ((erledigt * 100.0) / liste.size());
    }
}
