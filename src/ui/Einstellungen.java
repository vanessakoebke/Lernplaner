package ui;

import java.awt.*;

import javax.swing.*;
import lang.*;

public class Einstellungen extends JFrame {
    
    public Einstellungen() {
        super(I18n.t("Common.Einstellungen"));
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

        //TODO
        //Sprachauswahl
        JLabel sprache = new JLabel(I18n.t("Common.Sprache"));
        JComboBox<Sprachen> sprachauswahl = new JComboBox<>(Sprachen.values());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(sprache, gbc);
        gbc.gridx = 1;
        this.add(sprachauswahl, gbc);
        
        //TODO
        //Module verwalten
        JButton buttonModule = new JButton(I18n.t("model.ModuleVerwalten"));
        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(buttonModule, gbc);
        
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
