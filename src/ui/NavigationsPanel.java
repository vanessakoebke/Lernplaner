package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import service.Control;
import util.FALoader;

public class NavigationsPanel extends JPanel{

    public NavigationsPanel(Hauptfenster hf) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        
        //AufgabenAnsicht-Button aktuell
        JButton buttonAufgabenAktuell = new JButton("\uf0ca");
        buttonAufgabenAktuell.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonAufgabenAktuell.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAufgabenAktuell.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonAufgabenAktuell.addActionListener(e -> {
           hf.setCenter(0);
            
        });
        this.add(buttonAufgabenAktuell, gbc);
        gbc.gridy = 1;
        
        //AufgabenAnsicht-Button alt
        JButton buttonAufgabenAlt = new JButton("\uf00c");
        buttonAufgabenAlt.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonAufgabenAlt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonAufgabenAlt.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonAufgabenAlt.addActionListener(e -> {
           hf.setCenter(1);
            
        });
        this.add(buttonAufgabenAlt, gbc);
        gbc.gridy = 2;
        
        //Fortschritts-Button
        JButton buttonFortschritt = new JButton("\uf828");
        buttonFortschritt.setPreferredSize(new java.awt.Dimension(45, 45));
        buttonFortschritt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonFortschritt.setFont(FALoader.loadFontAwesome().deriveFont(28f));
        buttonFortschritt.addActionListener(e -> {
            hf.setCenter(3);
            
        });
        
        
        this.add(buttonFortschritt, gbc);
        
        
        setPreferredSize(new java.awt.Dimension(80, 0)); 
        setBackground(new java.awt.Color(245, 245, 245)); // leichtes Grau
        setVisible(true);
    }
}