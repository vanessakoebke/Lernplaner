package ui;

import java.awt.*;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.Modul;
import service.ModulManager;
import ui.buttons.CancelButton;
import util.CalendarFormatter;


public class NeuesModul extends JFrame{
    private ModulPanel modulPanel;

    public NeuesModul(ModulManager modulManager, ModulVerwAnsicht ursprungsfenster) {
        super(I18n.t("ui.Modulverwaltung.ModulHinzu"));
        if (modulManager == null) {
            System.out.println("Hilfe!");
        }
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        modulPanel = new ModulPanel(new Modul(""));
        
        this.add(modulPanel, BorderLayout.CENTER);
        
        //Buttons Abbrechen und Hinzufügen
        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonHinzufuegen"));
        buttonHinzu.addActionListener(e -> {
            Modul modul = modulPanel.getModul();
            modulManager.addModul(modul); // Callback an Main
            this.dispose();
            ursprungsfenster.refresh();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonHinzu);
        buttonPanel.add(new CancelButton());
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }


    
}
