package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import model.Status;
import service.Control;
import ui.buttons.DeleteButton;
import ui.buttons.EditButton;

public class CenterPanel extends JPanel implements IAnsicht {
    private AufgabenAnsicht neu;
    private AufgabenAnsicht alt;
    private final List<JPanel> panelListe = new ArrayList<>();
    private JPanel aktuellesPanel;

    public CenterPanel(Control control) {
        setLayout(new BorderLayout()); // Panel nimmt den gesamten Platz
        initAufgabenAnsichtAktuell(control);
        initAufgabenAnsichtAlt(control);
        showPanel(0); // per Default das erste Panel anzeigen
    }

    // --- Initialisierung der Aufgaben-Ansicht aktuell---
    private void initAufgabenAnsichtAktuell(Control control) {
        neu = new AufgabenAnsicht(control, true);
        JTable tabelle = new JTable(neu);

        tabelle.getTableHeader().setFont(tabelle.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // Status-Dropdown
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(statusCombo));

        // Spalte Bearbeiten
        EditButton editButton = new EditButton();
        tabelle.getColumnModel().getColumn(6).setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(6).setCellEditor(editButton.getEditor(e -> {
            new AufgabeBearbeiten(neu, tabelle.getEditingRow(), control);
        }));
        fixButtonColumnWidth(tabelle, 6, editButton.getButton());

        // Spalte Löschen
        DeleteButton deleteButton = new DeleteButton();
        tabelle.getColumnModel().getColumn(7).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(7).setCellEditor(deleteButton.getEditor(e -> {
            int row = tabelle.getSelectedRow();
            int id = neu.getAufgabe(row).getId();
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                neu.removeAufgabe(row, id);
            }
        }));
        fixButtonColumnWidth(tabelle, 7, deleteButton.getButton());

        // JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelle);

        // Panel für Aufgabenansicht
        JPanel aufgabenPanel = new JPanel(new BorderLayout());
        aufgabenPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel in die Liste aufnehmen
        panelListe.add(0, aufgabenPanel);
    }
    
    
    //--- Initialisierung der Aufgaben-Ansicht alt---
    private void initAufgabenAnsichtAlt(Control control) {
        alt = new AufgabenAnsicht(control, false);
        JTable tabelle = new JTable(alt);

        tabelle.getTableHeader().setFont(tabelle.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // Status-Dropdown
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(statusCombo));

        // Spalte Bearbeiten
        EditButton editButton = new EditButton();
        tabelle.getColumnModel().getColumn(6).setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(6).setCellEditor(editButton.getEditor(e -> {
            new AufgabeBearbeiten(alt, tabelle.getEditingRow(), control);
        }));
        fixButtonColumnWidth(tabelle, 6, editButton.getButton());

        // Spalte Löschen
        DeleteButton deleteButton = new DeleteButton();
        tabelle.getColumnModel().getColumn(7).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(7).setCellEditor(deleteButton.getEditor(e -> {
            int row = tabelle.getSelectedRow();
            int id = alt.getAufgabe(row).getId();
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                alt.removeAufgabe(row, id);
            }
        }));
        fixButtonColumnWidth(tabelle, 7, deleteButton.getButton());

        // JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelle);

        // Panel für Aufgabenansicht
        JPanel aufgabenPanel = new JPanel(new BorderLayout());
        aufgabenPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel in die Liste aufnehmen
        panelListe.add(1, aufgabenPanel);
    }


    // --- Hilfsmethode für feste Button-Spaltenbreite ---
    private void fixButtonColumnWidth(JTable table, int colIndex, JButton button) {
        int breite = button.getPreferredSize().width + 40; // Puffer
        TableColumn col = table.getColumnModel().getColumn(colIndex);
        col.setPreferredWidth(breite);
        col.setMaxWidth(breite);
        col.setMinWidth(breite);
    }

    // --- Panel wechseln ---
    public void showPanel(int index) {
        if (index < 0 || index >= panelListe.size()) return;
        if (aktuellesPanel != null) remove(aktuellesPanel);
        aktuellesPanel = panelListe.get(index);
        add(aktuellesPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void refresh() {
        if (neu != null) {
            neu.refresh();  // ruft fireTableDataChanged()
        }
        if (alt != null) {
            alt.refresh();  // ruft fireTableDataChanged()
        }
        if (aktuellesPanel != null) {
            aktuellesPanel.revalidate();
        }
        repaint();
    }

    AufgabenAnsicht getAktuell() {
        return neu;
    }
    
    AufgabenAnsicht getAlt() {
        return alt;
    }


    // Zugriff auf Panels, falls du weitere verwalten willst
    public JPanel getPanel(int index) {
        if (index < 0 || index >= panelListe.size()) return null;
        return panelListe.get(index);
    }
}
