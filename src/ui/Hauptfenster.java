package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.TableColumn;

import lang.I18n;
import model.Einstellungen;
import model.Status;
import service.*;
import ui.buttons.DeleteButton;
import ui.buttons.EditButton;
import util.FALoader;

public class Hauptfenster extends JFrame implements IAnsicht {
    private AufgabenAnsicht aufgabenAnsicht;
    private Control control;

    public Hauptfenster(Control control) {
        super(I18n.t("ui.Main.FensterTitel"));
        this.control = control;
        // Aufgabenansicht (TableModel)
         this.aufgabenAnsicht = new AufgabenAnsicht(control);
        // JTable
        JTable tabelle = new JTable(aufgabenAnsicht);
        tabelle.getTableHeader().setFont(tabelle.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        // Status-Dropdown
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(statusCombo));
        // Spalte "Bearbeiten"
        EditButton editButton = new EditButton();
        tabelle.getColumnModel().getColumn(6).setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(6).setCellEditor(editButton.getEditor(e -> {
            new AufgabeBearbeiten(aufgabenAnsicht, tabelle.getEditingRow(), control);
        }));
        // Spaltenbreite anpassen
        JButton tempButton = editButton.getButton();
        int buttonBreite = tempButton.getPreferredSize().width + 40; // Puffer hinzufügen
        TableColumn col = tabelle.getColumnModel().getColumn(6);
        col.setPreferredWidth(buttonBreite);
        col.setMaxWidth(buttonBreite);
        col.setMinWidth(buttonBreite);
        // Spalte "Löschen"
        DeleteButton deleteButton = new DeleteButton();
        tabelle.getColumnModel().getColumn(7).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(7).setCellEditor(deleteButton.getEditor(e -> {
            int row = tabelle.getSelectedRow();
            int id = aufgabenAnsicht.getAufgabe(row).getId();
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung", // TODO Texte aus I18n
                                                                                                // holen
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                aufgabenAnsicht.removeAufgabe(row, id);
            }
        }));
        // Spaltenbreite anpassen
        tempButton = deleteButton.getButton();
        buttonBreite = tempButton.getPreferredSize().width + 40; // Puffer hinzufügen
        col = tabelle.getColumnModel().getColumn(7);
        col.setPreferredWidth(buttonBreite);
        col.setMaxWidth(buttonBreite);
        col.setMinWidth(buttonBreite);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        // Hauptfenster
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                control.getPersistenz().speichern(control.getAm().getAufgabenListe());
                control.getPersistenz().speichern(control.getEinstellungen());
                System.exit(0);
            }
        });
        // Button neue Aufgabe
        JButton buttonNeueAufgabe = new JButton(I18n.t("ui.Main.ButtonNeueAufgabe"));
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(aufgabenAnsicht, control));
        // Button Einstellungen
        JButton buttonEinstellungen = new JButton("\uf013");
        buttonEinstellungen.setPreferredSize(new java.awt.Dimension(25, 25));
        buttonEinstellungen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));
        buttonEinstellungen.setFont(FALoader.loadFontAwesome());
        buttonEinstellungen.addActionListener(e -> new EinstellungenAnsicht(control));
        // Unteres Panel
        JPanel southPanel = new JPanel();
        southPanel.add(buttonNeueAufgabe);
        // Oberes Panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(buttonEinstellungen, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void refresh() {
        aufgabenAnsicht.refresh();
        
    }
}
