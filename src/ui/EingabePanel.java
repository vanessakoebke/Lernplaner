package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import lang.I18n;
import model.*;

public class EingabePanel extends JPanel {
    private Aufgabe aufgabe;
    JTextField titelFeld;
    JTextArea beschreibungFeld;
    JTextField datumFeld;
    JComboBox<Status> statusFeld;
    private DateTimeFormatter formatter;

    public EingabePanel() {
        this(new Aufgabe("", ""));
    }

    public EingabePanel(String titel, String beschreibung, LocalDate datum, int status) {
        this(new Aufgabe(titel, beschreibung, datum, status));
    }
    

    public EingabePanel(Aufgabe aufgabe) {
        this.aufgabe = aufgabe;
        formatter = DateTimeFormatter.ofPattern(I18n.t("Common.Datumsformat_Java"));
        
        // Panel für das Formular
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        // Label und Textfeld für Titel
        JLabel titelLabel = new JLabel(I18n.t("ui.Common.Aufgabenname"));
        titelFeld = new JTextField(aufgabe.getTitel());
        titelFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(titelLabel, gbc);
        gbc.gridx = 1;
        this.add(titelFeld, gbc);
        
        // Label und Textbereich für Beschreibung
        JLabel beschreibungLabel = new JLabel(I18n.t("ui.Common.Beschreibung"));
        beschreibungFeld = new JTextArea(aufgabe.getBeschreibung(), 4, 20); // 4 Zeilen hoch
        beschreibungFeld.setLineWrap(true);
        beschreibungFeld.setWrapStyleWord(true);
        JScrollPane beschreibungScroll = new JScrollPane(beschreibungFeld, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(beschreibungLabel, gbc);
        gbc.gridx = 1;
        this.add(beschreibungScroll, gbc);
        
        // Label und Textfeld für Datum
        JLabel datumLabel = new JLabel(I18n.t("ui.Common.Faelligkeit"));
        datumFeld = (aufgabe.getFaelligkeit() == null) ? new JTextField(I18n.t("ui.EingabePanel.Datumsformat"))
                : new JTextField(aufgabe.getFaelligkeit().format(formatter));
        datumFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        datumFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(datumLabel, gbc);
        gbc.gridx = 1;
        this.add(datumFeld, gbc);
        
        // Label und Textfeld für Status-Dropdown
        JLabel statusLabel = new JLabel(I18n.t("ui.Common.Status"));
        statusFeld = new JComboBox<Status>(Status.values());
        statusFeld.setSelectedItem(aufgabe.getStatus());
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(statusLabel, gbc);
        gbc.gridx = 1;
        this.add(statusFeld, gbc);
        // Prüfen beim Verlassen des Feldes
        // Edit: deaktiviert, weil zu nervig wenn man das Feld anklickt und danach in
        // ein anderes Feld springen will, wird bei getAufgabe eh geprüft
//        datumFeld.addFocusListener(new java.awt.event.FocusAdapter() {
//            @Override
//            public void focusLost(java.awt.event.FocusEvent e) {
//                String text = datumFeld.getText();
//                try {
//                    LocalDate.parse(text, formatter);
//                    // gültiges Datum
//                } catch (DateTimeParseException ex) {
//                    JOptionPane.showMessageDialog(null,
//                            "Bitte ein gültiges Datum im Format TT.MM.JJJJ eingeben.",
//                            "Ungültiges Datum",
//                            JOptionPane.ERROR_MESSAGE);
//                    datumFeld.requestFocus();
//                }
//            }});
    }

    public Aufgabe getAufgabe() {
        LocalDate datum = null;
        String datumText = datumFeld.getText().trim();
        if (!datumText.equals("") && !datumText.equals(I18n.t("ui.EingabePanel.Datumsformat"))) {
            try {
                datum = LocalDate.parse(datumText, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, I18n.t("Common.Erros.UngueltigesDatum"),
                        I18n.t("Common.Errors.Fehler"), JOptionPane.ERROR_MESSAGE);
                datumFeld.requestFocus();
                return null; // Abbruch, keine Aufgabe erzeugen
            }
        }
        return new Aufgabe(titelFeld.getText(), beschreibungFeld.getText(), datum, statusFeld.getSelectedIndex());
    }
}
