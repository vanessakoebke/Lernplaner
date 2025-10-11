package ui;

import java.awt.*;
import javax.swing.*;

import lang.I18n;
import model.*;
import service.*;
import ui.buttons.*;

public class NeueAufgabe extends JFrame {
    private CenterPanel center;
    private EingabePanel eingabePanel;

    public NeueAufgabe(CenterPanel center, Control control) {
        super(I18n.t("ui.NeueAufgabe.FensterTitel"));
        this.center = center;
        this.eingabePanel = new EingabePanel(control);

        // Buttons
        JButton buttonAbbrechen = new CancelButton();

        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonHinzufuegen"));
        buttonHinzu.addActionListener(e -> {
            Aufgabe aufgabe = eingabePanel.getAufgabe();
            
            // 1. In den globalen AufgabenManager speichern
            control.getAm().addAufgabe(aufgabe);

            // 2. In die Datenbank speichern
            if (aufgabe.getId() == null) {
                control.getDb().upsertAufgabe(aufgabe);
            }

            // 3. TableModel refreshen
            center.refresh(); // fireTableDataChanged() wird intern aufgerufen  

            this.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonHinzu);
        buttonPanel.add(buttonAbbrechen);

        // Layout
        add(eingabePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
