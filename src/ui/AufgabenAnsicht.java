package ui;

import javax.swing.table.AbstractTableModel;

import lang.I18n;
import model.*;
import service.AufgabenManager;

public class AufgabenAnsicht extends AbstractTableModel implements IAnsicht {

    private final String[] spaltenTitel = {
            I18n.t("model.Aufgabentyp.Modul"),
            I18n.t("ui.Common.Aufgabenname"),
            I18n.t("ui.Common.Beschreibung"),
            I18n.t("ui.Common.Start"),
            I18n.t("ui.Common.Faelligkeit"),
            I18n.t("ui.Common.Status"),
            "", // Bearbeiten
            ""  // Löschen
    };

    private AufgabenManager aufgabenManager;
    private Einstellungen einstellungen;
    

    public AufgabenAnsicht(AufgabenManager aufgabenManager, Einstellungen einstellungen) {
        this.aufgabenManager = aufgabenManager;
        this.einstellungen = einstellungen;
    }

    @Override
    public int getRowCount() {
        return aufgabenManager.getAufgabenListe().size();
    }

    @Override
    public int getColumnCount() {
        return spaltenTitel.length;
    }

    @Override
    public String getColumnName(int column) {
        return spaltenTitel[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 5) return Status.class; // Status-Enum
        if (columnIndex >= 6) return Object.class;  // Buttons
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5 || columnIndex >= 6; // Status + Buttons editierbar
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Aufgabe a = aufgabenManager.getAufgabenListe().get(rowIndex);
        switch (columnIndex) {
            case 0: return a.getModul();
            case 1: return a.getTitel();
            case 2: return a.getBeschreibung() != null ? a.getBeschreibung() : "";
            case 3: return a.getStart() != null ? a.getStart().format(einstellungen.getDatumsformat()) : "";
            case 4: return a.getEnde() != null ? a.getEnde().format(einstellungen.getDatumsformat()) : "";
            case 5: return a.getStatus();
            default: return ""; // Platzhalter für Button-Spalten
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 5 && aValue instanceof Status) {
            Aufgabe aufgabe = aufgabenManager.getAufgabenListe().get(rowIndex);
            aufgabe.setStatus((Status) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void addAufgabe(Aufgabe aufgabe) {
        aufgabenManager.addAufgabe(aufgabe);
        int row = getRowCount() - 1;
        fireTableRowsInserted(row, row);
    }
    
    public void updateAufgabe(Aufgabe aufgabe, int id, int row) {
        aufgabenManager.updateAufgabe(aufgabe, id);
        fireTableRowsUpdated(row, row);
    }

    public Aufgabe getAufgabe(int row) {
        return aufgabenManager.getAufgabenListe().get(row);
    }

    public void removeAufgabe(int row, int id) {
        aufgabenManager.getAufgabenListe().removeIf(a -> a.getId() == id);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public void refresh() {
        fireTableDataChanged();
        
    }
}
