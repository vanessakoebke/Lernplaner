package ui;

import java.awt.BorderLayout;

import javax.swing.*;

import lang.I18n;
import model.Modul;
import service.ModulManager;
import ui.buttons.CancelButton;


public class ModulBearbeiten extends JFrame implements IAnsicht{
    private ModulPanel modulPanel;

    public ModulBearbeiten(ModulManager modulManager, IAnsicht ursprungsfenster, Modul modul) {
        super(I18n.t("ui.Modulverwaltung.ModulHinzu"));
        Modul altesModul = modul;
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        modulPanel = new ModulPanel(modul);
        
        this.add(modulPanel, BorderLayout.CENTER);
        
        //Buttons Abbrechen und Ok
        JButton buttonHinzu = new JButton(I18n.t("Common.ButtonOk"));
        buttonHinzu.addActionListener(e -> {
            if (modulManager.getAlleModule().contains(altesModul)) {
                modulManager.removeModul(altesModul);
            } 
            modulManager.addModul(modulPanel.getModul()); // Callback an Main
            this.dispose();
            ursprungsfenster.refresh();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonHinzu);
        buttonPanel.add(new CancelButton());
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    @Override
    public void refresh() {
        // hier ist nichts zu tun
        
    }


    
}
