package ui;

import java.awt.*;

import javax.swing.*;

import lang.I18n;
import model.Einstellungen;
import model.Modul;
import service.Control;
import util.FALoader;

public class ModulVerwAnsicht extends JFrame implements IAnsicht {
    private Control control;
    private DefaultListModel<Modul> listModelAkt;
    private JList<Modul> aktuelleModule;
    private DefaultListModel<Modul> listModelAlt;
    private JList<Modul> alteModule;

    public ModulVerwAnsicht(Control control) {
        super(I18n.t("ui.Modulverwaltung.Titel"));
        this.control = control;

        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Aktuelle Module
        JLabel aktModuleLabel = new JLabel(I18n.t("ui.Modulverwaltung.AktuelleModule"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(aktModuleLabel, gbc);

        listModelAkt = new DefaultListModel<>();
        aktuelleModule = new JList<>(listModelAkt);
        aktuelleModule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aktuelleModule.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Modul m = (Modul) value;

                if (m.getKlausurTermin() == null) {
                    setText(m.getName());
                } else {
                    String datum = m.getKlausurTermin().format(control.getEinstellungen().getDatumsformat());
                    setText(m.getName() + "    (" + datum + ")");
                }

                return this;
            }
        });


        JScrollPane aktModuleScroll = new JScrollPane(
                aktuelleModule,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        JPanel panelAkt = new JPanel(new BorderLayout());
        panelAkt.add(aktModuleScroll, BorderLayout.CENTER);

        // Delete Button aktuelle Module
        JButton deleteButtonAkt = getDeleteButton();
        deleteButtonAkt.addActionListener(e -> {
            Modul selected = aktuelleModule.getSelectedValue(); 
            if (selected != null) {
                listModelAkt.removeElement(selected);           
                control.getMm().removeModul(selected);             
            }
        });

        //Edit Button aktuelle Module
        JButton editButtonAkt= getEditButton();
        editButtonAkt.addActionListener(e -> {
            if (aktuelleModule.getSelectedValue() != null) {
                new ModulBearbeiten(control.getMm(), this, aktuelleModule.getSelectedValue());
            }
        });
        
        JPanel buttonPanelAkt = new JPanel();
        buttonPanelAkt.setLayout(new BorderLayout());
        buttonPanelAkt.add(editButtonAkt, BorderLayout.NORTH);
        buttonPanelAkt.add(deleteButtonAkt, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // ScrollPane soll vertikal wachsen
        gbc.fill = GridBagConstraints.BOTH;
        this.add(aktModuleScroll, gbc);
        
        gbc.weightx = 0;
        gbc.weighty = 0; // ScrollPane soll vertikal wachsen
        gbc.fill = GridBagConstraints.NORTHWEST;
        gbc.gridx =2;
        this.add(buttonPanelAkt, gbc); 

        // Alte Module
        JLabel alteModuleLabel = new JLabel(I18n.t("ui.Modulverwaltung.AlteModule"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(alteModuleLabel, gbc);
        
        listModelAlt = new DefaultListModel<>();
        alteModule = new JList<>(listModelAlt);
        alteModule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alteModule.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Modul m = (Modul) value;

                if (m.getNote() == null) {
                    setText(m.getName());
                } else {
                    setText(m.getName() + "    (Note: " + m.getNote() + ")");
                }

                return this;
            }
        });


        JScrollPane altModuleScroll = new JScrollPane(
                alteModule,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );


        // Delete Button alte Module
        JButton deleteButtonAlt = getDeleteButton();
        deleteButtonAlt.addActionListener(e -> {
            Modul selected = alteModule.getSelectedValue(); 
            if (selected != null) {
                listModelAlt.removeElement(selected);           
                control.getMm().removeModul(selected);             
            }
        });
        
        //Edit Button alte Module
        JButton editButtonAlt= getEditButton();
        editButtonAlt.addActionListener(e -> {
            if (alteModule.getSelectedValue() != null) {
                new ModulBearbeiten(control.getMm(), this, alteModule.getSelectedValue());
            }
        });
        
        JPanel buttonPanelAlt = new JPanel();
        buttonPanelAlt.setLayout(new BorderLayout());
        buttonPanelAlt.add(editButtonAlt, BorderLayout.NORTH);
        buttonPanelAlt.add(deleteButtonAlt, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // ScrollPane soll vertikal wachsen
        gbc.fill = GridBagConstraints.BOTH;
        this.add(altModuleScroll, gbc);
        
        gbc.weightx = 0;
        gbc.weighty = 0; // ScrollPane soll vertikal wachsen
        gbc.fill = GridBagConstraints.NORTHWEST;
        gbc.gridx =2;
        this.add(buttonPanelAlt, gbc); 

        // Neuer Modul Button
        JButton buttonModulNeu = new JButton(I18n.t("ui.Modulverwaltung.ModulHinzu"));
        buttonModulNeu.addActionListener(e -> new ModulBearbeiten(control.getMm(), this, new Modul("")));
        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(buttonModulNeu, gbc);

        // Füller, damit die Komponenten richtig positioniert werden
        gbc.gridx = 100;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(Box.createGlue(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 100;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(Box.createGlue(), gbc);

        // Initiale Inhalte laden
        refresh();

        this.setVisible(true);
    }

    private JButton getDeleteButton() {
        JButton button = new JButton("\uf1f8");
//        button.setPreferredSize(new Dimension(24, 24));
        button.setFont(FALoader.loadFontAwesome());
        button.setFocusPainted(true);
        button.setBorderPainted(false);
        return button;
    }
    
    private JButton getEditButton() {
        JButton button = new JButton("\uf044");
//        button.setPreferredSize(new Dimension(50, 50));
        button.setFont(FALoader.loadFontAwesome());
        button.setFocusPainted(true);
        button.setBorderPainted(false);
        return button;
    }

    @Override
    public void refresh() {
        listModelAkt.clear();
        for (Modul m : control.getMm().getAktuelleModule()) {
               listModelAkt.addElement(m);
        }
        listModelAlt.clear();
        for (Modul m : control.getMm().getAlteModule()) {
                listModelAlt.addElement(m);
        }
    }
}




    
    

