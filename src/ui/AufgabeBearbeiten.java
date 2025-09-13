package ui;

import java.awt.BorderLayout;

import javax.swing.*;

import lang.I18n;
import model.Aufgabe;
import service.Control;
import ui.buttons.CancelButton;

public class AufgabeBearbeiten extends JFrame {
    private EingabePanel eingabePanel;
    private AufgabenAnsicht aufgabenAnsicht;
    private Aufgabe aufgabe;

    AufgabeBearbeiten(AufgabenAnsicht aufgabenAnsicht, int row, Control control) {
        super(I18n.t("ui.AufgabeBearbeiten.FensterTitel"));
        this.aufgabenAnsicht = aufgabenAnsicht;
        aufgabe = aufgabenAnsicht.getAufgabe(row);
        this.eingabePanel = new EingabePanel(aufgabe, control);
        // Buttons
        JButton buttonAbbrechen = new CancelButton();
        JButton buttonOk = new JButton(I18n.t("Common.ButtonOk"));
        buttonOk.addActionListener(e -> {
            Aufgabe neu = eingabePanel.getAufgabe();
           aufgabenAnsicht.updateAufgabe(neu, aufgabe.getId(), row); // Callback an Main
            this.dispose();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonOk);
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
