package ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

class Buttonzelle extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private final java.util.List<JButton> buttonliste;

    public Buttonzelle(java.util.List<JButton> buttonliste) {
        this.buttonliste = buttonliste;
    }

    // Hilfsfunktion: Panel neu aufbauen
    private JPanel createPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true); // Hintergrund sichtbar
        for (JButton b : buttonliste) {
            JButton copy = new JButton(b.getText());
            copy.setIcon(b.getIcon());
            copy.setFont(b.getFont());
            for (ActionListener al : b.getActionListeners()) {
                copy.addActionListener(al);
            }
            copy.setFocusPainted(false);
            copy.setBorderPainted(false);
            copy.setContentAreaFilled(false);
            panel.add(copy);
        }
        return panel;
    }

    // Renderer
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = createPanel();
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    // Editor
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return createPanel();
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
