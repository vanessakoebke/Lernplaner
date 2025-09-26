package ui;

import javax.swing.table.AbstractTableModel;

import lang.I18n;
import model.*;
import service.AufgabenManager;
import service.Control;

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

    private Control control;
    

    public AufgabenAnsicht(Control control) {
        this.control = control;
    }

    @Override
    public int getRowCount() {
        return control.getAm().getAufgabenListe().size();
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
        Aufgabe a = control.getAm().getAufgabenListe().get(rowIndex);
        switch (columnIndex) {
            case 0: return a.getModul();
            case 1: return a.getTitel();
            case 2: return a.getBeschreibung() != null ? a.getBeschreibung() : "";
            case 3: return a.getStart() != null ? a.getStart().format(control.getEinstellungen().getDatumsformat()) : "";
            case 4: return a.getEnde() != null ? a.getEnde().format(control.getEinstellungen().getDatumsformat()) : "";
            case 5: return a.getStatus();
            default: return ""; // Platzhalter für Button-Spalten
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 5 && aValue instanceof Status) {
            Aufgabe aufgabe = control.getAm().getAufgabenListe().get(rowIndex);
            aufgabe.setStatus((Status) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void addAufgabe(Aufgabe aufgabe) {
        control.getAm().addAufgabe(aufgabe);
        int row = getRowCount() - 1;
        fireTableRowsInserted(row, row);
    }
    
    public void updateAufgabe(Aufgabe aufgabe, int id, int row) {
        control.getAm().updateAufgabe(aufgabe, id);
        fireTableRowsUpdated(row, row);
    }

    public Aufgabe getAufgabe(int row) {
        return control.getAm().getAufgabenListe().get(row);
    }

    public void removeAufgabe(int row, int id) {
        // Zuerst die Aufgabe aus der Liste holen
        Aufgabe zuLoeschen = control.getAm().getAufgabenListe()
                                   .stream()
                                   .filter(a -> a.getId() != null && a.getId() == id)
                                   .findFirst()
                                   .orElse(null);

        if (zuLoeschen != null) {
            // Aus der Liste entfernen
            control.getAm().getAufgabenListe().remove(zuLoeschen);
            // Aus der DB löschen
            control.getDb().deleteAufgabe(zuLoeschen);
            // TableModel informieren
            fireTableRowsDeleted(row, row);
        }
    }

    @Override
    public void refresh() {
        fireTableDataChanged();
        
    }
}
