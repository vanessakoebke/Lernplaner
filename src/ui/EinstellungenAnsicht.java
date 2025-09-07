package ui;

import java.awt.*;

import javax.swing.*;

import lang.I18n;
import lang.Sprache;
import model.Einstellungen;
import ui.buttons.CancelButton;

public class EinstellungenAnsicht extends JFrame {
    private Einstellungen einstellungen;
    
    public EinstellungenAnsicht(Einstellungen einstellungen) {
        super(I18n.t("Common.Einstellungen"));
        this.einstellungen = einstellungen;
        // Haupt-Panel
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        //Version
        JLabel version = new JLabel(I18n.t("Common.Version"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(version, gbc);
        JLabel versionsNr = new JLabel("1.0");
        gbc.gridx = 1;
        this.add(versionsNr, gbc);


        JLabel spracheLabel = new JLabel(I18n.t("Common.Sprache"));
        JComboBox<Sprache> sprachauswahl = new JComboBox<>(Sprache.values());
        sprachauswahl.setSelectedItem(einstellungen.getSprache());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(spracheLabel, gbc);
        gbc.gridx = 1;
        this.add(sprachauswahl, gbc);
        sprachauswahl.addActionListener(e -> {
            einstellungen.setSprache((Sprache) sprachauswahl.getSelectedItem());
            JOptionPane.showMessageDialog(
                    null,                             // Elternfenster (null = zentriert auf Bildschirm)
                    I18n.t("Common.Hinweis.Neustart"),    // Nachricht
                    "",                        // Titel vom Fenster
                    JOptionPane.INFORMATION_MESSAGE   // Icon-Typ (Info-Symbol)
                );
            this.dispose();
            });
        
        JButton buttonModule = new JButton(I18n.t("model.ModuleVerwalten"));
        buttonModule.addActionListener(e -> new ModulVerwAnsicht(einstellungen));
        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(buttonModule, gbc);
        
 
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
