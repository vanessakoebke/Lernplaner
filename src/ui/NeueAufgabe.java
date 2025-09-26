package ui;

import java.awt.*;
import javax.swing.*;

import lang.I18n;
import model.*;
import service.*;
import ui.buttons.*;

public class NeueAufgabe extends JFrame {
    private AufgabenAnsicht aufgabenAnsicht;
    private EingabePanel eingabePanel;

    public NeueAufgabe(AufgabenAnsicht aufgabenAnsicht, Control control) {
        super(I18n.t("ui.NeueAufgabe.FensterTitel"));
        this.aufgabenAnsicht = aufgabenAnsicht;
        this.eingabePanel = new EingabePanel(control);

        // Buttons
        JButton buttonAbbrechen = new CancelButton();

        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonHinzufuegen"));
        buttonHinzu.addActionListener(e -> {
            Aufgabe aufgabe = eingabePanel.getAufgabe();
            aufgabenAnsicht.addAufgabe(aufgabe); // Callback an Main
            if (aufgabe.getId() == null) {
                DatenbankService db = new DatenbankService();
                db.init();
                db.upsertAufgabe(aufgabe);
            }

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
