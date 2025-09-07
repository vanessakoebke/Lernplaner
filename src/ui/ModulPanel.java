package ui;

import java.awt.*;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.Modul;
import util.CalendarFormatter;

public class ModulPanel extends JPanel {
    private JTextField nameFeld;
    private JDatePickerImpl klausurFeld;
    
    public ModulPanel(Modul modul) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        //Modulname
        JLabel nameLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // links oben
        this.add(nameLabel, gbc);
        
        this.nameFeld = new JTextField(modul.getName());
        this.nameFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        this.nameFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.add(this.nameFeld, gbc);
        
        //Klausurtermin
        JLabel klausurLabel = new JLabel(I18n.t("ui.Modulverwaltung.Klausurtermin"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START; // links oben
        this.add(klausurLabel, gbc);
        
        // Klausurtermin mit Datepicker
        UtilDateModel model = new UtilDateModel();
        model.setSelected(false); // optional: aktuelles Datum vorab auswählen
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        this.klausurFeld = new JDatePickerImpl(datePanel, new CalendarFormatter());
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.add(this.klausurFeld, gbc); // TODO Icon für Kalender setzen
    }
    
    public Modul getModul() {
        return new Modul(nameFeld.getText());
    }
}
