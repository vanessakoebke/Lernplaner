package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.*;
import service.Control;
import util.CalendarFormatter;

public class EingabePanel extends JPanel implements IAnsicht{
    private Aufgabe aufgabe;
    private JComboBox<Modul> modulFeld;
    private JComboBox<String> aufgabentypFeld;
    private JTextField titelFeld;
    private JTextArea beschreibungFeld;
    private JDatePickerImpl startFeld;
    private JDatePickerImpl endeFeld;
    private JComboBox<Status> statusFeld;
    private JTextField seitenDurcharbeitenFeld;
    private Control control;

    public EingabePanel(Control control) {
        this(new AufgabeAllgemein("", ""), control);
    }

    public EingabePanel(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, Modul modul, Control control) {
        this(new AufgabeAllgemein(titel, beschreibung, ende, start, status, modul), control);
    }
    

    public EingabePanel(Aufgabe aufgabe, Control control) {
        this.aufgabe = aufgabe;
        this.control = control;
        DateTimeFormatter formatter = control.getEinstellungen().getDatumsformat();
        
        // Panel für das Formular
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        //Label und Textfeld für das Modul
        JLabel modulLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
        this.modulFeld = new JComboBox<Modul>(control.getMm().getAktuelleModule().toArray(new Modul[0]));
        if (aufgabe.getModul() != null) {
            modulFeld.setSelectedItem(aufgabe.getModul());
        } else {
            modulFeld.setSelectedItem(null);
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(modulLabel, gbc);
        gbc.gridx = 1;
        this.add(modulFeld, gbc);
        
     // Label und Textfeld für Aufgabentyp-Dropdown
        JLabel aufgabentypLabel = new JLabel(I18n.t("model.Aufgabentyp.Typ"));
        aufgabentypFeld = new JComboBox<String>();
        aufgabentypFeld.addItem(I18n.t("model.Aufgabentyp.Allgemein"));
        aufgabentypFeld.addItem(I18n.t("model.Aufgabentyp.Durcharbeiten"));
        aufgabentypFeld.addItem( I18n.t("model.Aufgabentyp.EA"));
        aufgabentypFeld.addItem(I18n.t("model.Aufgabentyp.Wiederholen"));
        aufgabentypFeld.addItem(I18n.t("model.Aufgabentyp.Altklausur"));
        aufgabentypFeld.setSelectedItem(aufgabe.getTyp());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(aufgabentypLabel, gbc);
        gbc.gridx = 1;
        this.add(aufgabentypFeld, gbc);
        
        // Label und Textfeld für Titel
        JLabel titelLabel = new JLabel(I18n.t("ui.Common.Aufgabenname"));
        titelFeld = new JTextField(aufgabe.getTitel());
        titelFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 5;
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
        gbc.gridy = 10;
        this.add(beschreibungLabel, gbc);
        gbc.gridx = 1;
        this.add(beschreibungScroll, gbc);
        
        //Label und JDatePicker für Starttermin
        JLabel startLabel = new JLabel(I18n.t("ui.Common.Start"));
        UtilDateModel startModel = new UtilDateModel();
        if (aufgabe.getStart() != null) {
            LocalDate start = aufgabe.getStart();

            // LocalDate konvertieren
            startModel.setDate(
                start.getYear(),
                start.getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                start.getDayOfMonth()
            );
            startModel.setSelected(true);
        } else if (titelFeld.getText() == ""){
            startModel.setDate(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                    LocalDate.now().getDayOfMonth()
                );
            startModel.setSelected(true); // Starttermin heute vorausgewählt bei neuen Aufgaben
        } else {
            startModel.setSelected(false); //Starttermin leer bei bestehenden Aufgaben ohne Starttermin
        }
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, p);
        this.startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 12;
        this.add(startLabel, gbc);
        gbc.gridx = 1;
        this.add(startFeld, gbc);
        
        // Label und Textfeld für Endtermin
        JLabel endeLabel = new JLabel(I18n.t("ui.Common.Faelligkeit"));
        UtilDateModel endeModel = new UtilDateModel();
        if (aufgabe.getEnde() != null) {
            LocalDate ende = aufgabe.getEnde();

            // LocalDate konvertieren
            endeModel.setDate(
                ende.getYear(),
                ende.getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                ende.getDayOfMonth()
            );
            endeModel.setSelected(true);
        } else {
            startModel.setSelected(false); //Endtermin leer bei bestehenden Aufgaben ohne Endtermin
        }
        JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, p);
        this.endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 15;
        this.add(endeLabel, gbc);
        gbc.gridx = 1;
        this.add(endeFeld, gbc);
        
        // Label und Textfeld für Status-Dropdown
        JLabel statusLabel = new JLabel(I18n.t("ui.Common.Status"));
        statusFeld = new JComboBox<Status>(Status.values());
        statusFeld.setSelectedItem(aufgabe.getStatus());
        gbc.gridx = 0;
        gbc.gridy = 20;
        this.add(statusLabel, gbc);
        gbc.gridx = 1;
        this.add(statusFeld, gbc);
        
        //Label und Textfeld für Seitenzahl für Aufgabentyp Durcharbeiten
        JLabel seitenDurcharbeitenLabel = new JLabel(I18n.t("model.Lerneinheiten.Seiten"));
        seitenDurcharbeitenFeld = new JTextField();
        if (aufgabe instanceof AufgabeDurcharbeiten) {
            AufgabeDurcharbeiten ad = (AufgabeDurcharbeiten) aufgabe;
            if(ad.getSeiten() !=0) {
                seitenDurcharbeitenFeld.setText(String.valueOf(ad.getSeiten())); 
                seitenDurcharbeitenLabel.setVisible(true);
                seitenDurcharbeitenFeld.setVisible(true);
            }
        } else {
            seitenDurcharbeitenLabel.setVisible(false);
            seitenDurcharbeitenFeld.setVisible(false);
        }
        seitenDurcharbeitenFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        seitenDurcharbeitenFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 22;
        this.add(seitenDurcharbeitenLabel, gbc);
        gbc.gridx = 1;
        this.add(seitenDurcharbeitenFeld, gbc);
        
        //ActionListener um korrekte Felder einzublenden
        aufgabentypFeld.addActionListener(e -> {
            switch(aufgabentypFeld.getSelectedIndex()) {
            case 1:        
                seitenDurcharbeitenLabel.setVisible(true);
                seitenDurcharbeitenFeld.setVisible(true);
                return;
            case 2:
            case 3:
                
            case 4:
            default: ;
            }
        });
    }

    public Aufgabe getAufgabe() {
        Date selectedStartDate = (Date) startFeld.getModel().getValue();
        LocalDate start = null;
        if (selectedStartDate != null) {
            start = selectedStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        Date selectedEndDate = (Date) endeFeld.getModel().getValue();
        LocalDate ende = null;
        if (selectedEndDate != null) {
            ende = selectedEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        switch (aufgabentypFeld.getSelectedIndex()) {
        case 1:  
            int seiten = 0;
            try {
                seiten = Integer.parseInt(seitenDurcharbeitenFeld.getText());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,                        // Parent-Komponente, null = zentriert auf Bildschirm
                        I18n.t("Common.Errors.UngueltigeSeiten"),     // Nachricht
                        I18n.t("Common.Errors.Warnung"),                   // Titel des Fensters
                        JOptionPane.WARNING_MESSAGE  // Icon / Typ des Dialogs
                    );
            }
            return new AufgabeDurcharbeiten(titelFeld.getText(), beschreibungFeld.getText(), ende, start,
                    statusFeld.getSelectedIndex(), (Modul) modulFeld.getSelectedItem(), seiten);
            
        case 2:
        case 3:
            
        case 4:
        default: 
            return new AufgabeAllgemein(titelFeld.getText(), beschreibungFeld.getText(), ende, start,
                statusFeld.getSelectedIndex(), (Modul) modulFeld.getSelectedItem());
        }
        
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }
}
