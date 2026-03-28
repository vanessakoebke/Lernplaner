package ui;

import java.awt.BorderLayout;

import javax.swing.*;

import lang.I18n;
import model.Lerngruppe;
import model.Lerngruppe.Termin;
import service.Control;
import ui.buttons.CancelButton;

public class NeueLerngruppe extends JFrame{
    private CenterPanel center;
    private EingabePanelLg eingabePanel;
    
    public NeueLerngruppe(CenterPanel center, Control control) {
        super(I18n.t("ui.Lerngruppe.Fenstertitel"));
        this.center = center;
        
        //EingabePanel
        eingabePanel = new EingabePanelLg(control);
        
        // Buttons
        JButton buttonAbbrechen = new CancelButton();

        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonHinzufuegen"));
        buttonHinzu.addActionListener(e -> {
            Lerngruppe lg = eingabePanel.getLg();
            
            // 1. In den globalen LerngruppenManager speichern
            control.getLm().addLg(lg);
            
            //2. Aufgaben in Aufgabenmanager speichern
            for (Termin t: lg.getTermine()) {
                control.getAm().addAufgabe(t.getAufgabe());
                control.getDb().upsertAufgabe(t.getAufgabe());
            }

            // 3. In die Datenbank speichern
            if (lg.getId() == null) {
                control.getDb().upsertLg(lg);
            }

            // 4. TableModel refreshen
            center.refresh(); 
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
