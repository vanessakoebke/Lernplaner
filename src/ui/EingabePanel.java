package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;

import model.Aufgabe;

public class EingabePanel extends JPanel {
    private Aufgabe aufgabe;
    JTextField titelFeld;
    JTextArea beschreibungFeld;
    JTextField datumFeld;
    
    public EingabePanel() {
        this("", "", null);
    }
    
    public EingabePanel(Aufgabe aufgabe) {
        this(aufgabe.getTitel(), aufgabe.getBeschreibung(), aufgabe.getFaelligkeit().toString());
        this.aufgabe=aufgabe;
    }
    
    public EingabePanel(String titel, String beschreibung, String datum) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate datumParsed = datum==null? null: LocalDate.parse(datum, formatter);
        aufgabe =new Aufgabe(titel, beschreibung, datumParsed);
        
    // Panel für das Formular
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.NORTHWEST;

    // Label und Textfeld für Titel
    JLabel titelLabel = new JLabel("Aufgabenname:");
    titelFeld = new JTextField(titel);
    titelFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
    titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(titelLabel, gbc);
    gbc.gridx = 1;
    this.add(titelFeld, gbc);

    // Label und Textbereich für Beschreibung
    JLabel beschreibungLabel = new JLabel("Beschreibung:");
    beschreibungFeld = new JTextArea(beschreibung, 4, 20); // 4 Zeilen hoch
    beschreibungFeld.setLineWrap(true);
    beschreibungFeld.setWrapStyleWord(true);
    JScrollPane beschreibungScroll = new JScrollPane(beschreibungFeld,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    gbc.gridx = 0;
    gbc.gridy = 1;
    this.add(beschreibungLabel, gbc);
    gbc.gridx = 1;
    this.add(beschreibungScroll, gbc);

    // Label und Textfeld für Datum
    JLabel datumLabel = new JLabel("Fällig am:");
    datumFeld = (datum == null || datum.equals("")) ? new JTextField("TT.MM.JJJJ") : new JTextField(datum);
    datumFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
    datumFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

    gbc.gridx = 0;
    gbc.gridy = 2;
    this.add(datumLabel, gbc);
    gbc.gridx = 1;
    this.add(datumFeld, gbc);

    // Prüfen beim Verlassen des Feldes
    
    datumFeld.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            String text = datumFeld.getText();
            try {
                LocalDate.parse(text, formatter);
                // gültiges Datum
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null,
                        "Bitte ein gültiges Datum im Format TT.MM.JJJJ eingeben.",
                        "Ungültiges Datum",
                        JOptionPane.ERROR_MESSAGE);
                datumFeld.requestFocus();
            }
        }});
     
}
    public Aufgabe getAufgabe() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate datum = null;
        String datumText = datumFeld.getText().trim();
        if (!datumText.equals("") && !datumText.equals("TT.MM.JJJJ")) {
            try {
                datum = LocalDate.parse(datumText, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Ungültiges Datum! Bitte im Format TT.MM.JJJJ eingeben.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
                datumFeld.requestFocus();
                return null; // Abbruch, keine Aufgabe erzeugen
            }
        }
        return new Aufgabe(titelFeld.getText(), beschreibungFeld.getText(), datum);
    }

    }
