package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import util.FALoader;

public class NavigationPanel extends JPanel {
    public NavigationPanel(Hauptfenster hf) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
     // Tägliche Aufgaben-Button
        JButton buttonTaeglich = new JButton("\uf783");
        buttonTaeglich.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonTaeglich.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonTaeglich.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonTaeglich.addActionListener(e -> {
            //TODO
        });
        this.add(buttonTaeglich, gbc);
        ++gbc.gridy;
        
     // Wöchentliche Aufgaben-Button
        JButton buttonWoechentlich = new JButton("\uf784");
        buttonWoechentlich.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonWoechentlich.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonWoechentlich.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonWoechentlich.addActionListener(e -> {
            //TODO
        });
        this.add(buttonWoechentlich, gbc);
        ++gbc.gridy;
        
        // AufgabenAnsicht-Button in Arbeit
        JButton buttonAufgabenInArbeit = new JButton("\uf04b");
        buttonAufgabenInArbeit.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonAufgabenInArbeit.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAufgabenInArbeit.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonAufgabenInArbeit.addActionListener(e -> {
            hf.setCenter(2);
        });
        this.add(buttonAufgabenInArbeit, gbc);
        ++gbc.gridy;
        
        // AufgabenAnsicht-Button aktuell
        JButton buttonAufgabenAktuell = new JButton("\uf0ca");
        buttonAufgabenAktuell.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonAufgabenAktuell.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAufgabenAktuell.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonAufgabenAktuell.addActionListener(e -> {
            hf.setCenter(0);
        });
        this.add(buttonAufgabenAktuell, gbc);
        ++gbc.gridy;
        // AufgabenAnsicht-Button alt
        JButton buttonAufgabenAlt = new JButton("\uf00c");
        buttonAufgabenAlt.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonAufgabenAlt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAufgabenAlt.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonAufgabenAlt.addActionListener(e -> {
            hf.setCenter(1);
        });
        this.add(buttonAufgabenAlt, gbc);
        ++gbc.gridy;
        
     // Lerngruppen-Button
        JButton buttonLerngruppe = new JButton("\ue533");
        buttonLerngruppe.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonLerngruppe.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonLerngruppe.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonLerngruppe.addActionListener(e -> {
            //TODO
        });
        this.add(buttonLerngruppe, gbc);
        ++gbc.gridy;
        
        // Fortschritts-Button
        JButton buttonFortschritt = new JButton("\uf828");
        buttonFortschritt.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonFortschritt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonFortschritt.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonFortschritt.addActionListener(e -> {
            hf.setCenter(3);
        });
        this.add(buttonFortschritt, gbc);
        setPreferredSize(new java.awt.Dimension(80, 0));
        setVisible(true);
    }
}