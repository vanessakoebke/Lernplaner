package ui;

import java.awt.*;

import javax.swing.*;

import lang.I18n;
import model.Einstellungen;
import model.Modul;
import service.ModulManager;

public class ModulVerwAnsicht extends JFrame{
    private Einstellungen einstellungen;
    private ModulManager modulManager;
    private String aktuelleModule;
    private String alteModule;

    public ModulVerwAnsicht(Einstellungen einstellungen) {
        super(I18n.t("ui.Modulverwaltung.Titel"));
        this.einstellungen = einstellungen;
        this.modulManager = einstellungen.getModulManager();
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        
        JLabel aktModuleLabel = new JLabel(I18n.t("ui.Modulverwaltung.AktuelleModule"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(aktModuleLabel, gbc);
        
        refresh();
        JTextArea aktModuleArea = new JTextArea(aktuelleModule, 4, 20);
        aktModuleArea.setEditable(false);
        gbc.gridx =1;
        this.add(aktModuleArea, gbc);
        
        JLabel alteModuleLabel = new JLabel(I18n.t("ui.Modulverwaltung.AlteModule"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(alteModuleLabel, gbc);
        
        //Module hinzufügen
        JButton buttonModulNeu = new JButton(I18n.t("ui.Modulverwaltung.ModulHinzu"));
        buttonModulNeu.addActionListener(e -> {
            new NeuesModul(modulManager, this);
        });
        gbc.gridx = 0;
        gbc.gridy = 20;
        this.add(buttonModulNeu, gbc);
        
        //Leere Füller, damit die anderen Elemente an die richtige Stelle kommen.
        gbc.gridx = 100;
        gbc.gridy = 0; // rechts von allen Komponenten
        gbc.weightx = 1; // nimmt den restlichen horizontalen Platz
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(Box.createGlue(), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 100; // unter allen Komponenten
        gbc.weighty = 1; // nimmt den restlichen vertikalen Platz
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(Box.createGlue(), gbc);
        
        
        this.setVisible(true);
    }
    
    public void refresh() {
        aktuelleModule ="";
        try {
            for (Modul modul: einstellungen.getModulManager().getAktuelleModule()) {
                aktuelleModule += modul +"\n";
            }
        } catch (Exception e) {
            //nichts tun, wenn es keine Module gibt soll dem Textfeld auch nichts hinzugefügt werden.
        }
        
    }
    
    
}
