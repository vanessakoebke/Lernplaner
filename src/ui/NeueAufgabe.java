package ui;

import java.awt.*;
import javax.swing.*;

import lang.I18n;
import model.Aufgabe;

public class NeueAufgabe extends JFrame {

    public interface TaskListener {
        void aufgabeErstellt(Aufgabe aufgabe);
    }

    private EingabePanel eingabePanel;

    public NeueAufgabe(TaskListener listener) {
        super(I18n.t("ui.NeueAufgabe.FensterTitel"));

        this.eingabePanel = new EingabePanel();

        // Buttons
        JButton buttonAbbrechen = new JButton(I18n.t("Common.ButtonAbbrechen"));
        buttonAbbrechen.addActionListener(e -> this.dispose());

        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonHinzufuegen"));
        buttonHinzu.addActionListener(e -> {
            Aufgabe aufgabe = eingabePanel.getAufgabe();
            listener.aufgabeErstellt(aufgabe); // Callback an Main
            this.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonHinzu);
        buttonPanel.add(buttonAbbrechen);

        // Layout
        add(eingabePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
