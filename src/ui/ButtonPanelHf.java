package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import lang.I18n;
import service.Control;

public class ButtonPanelHf extends JPanel implements IAnsicht {
    Control control;
    Hauptfenster hf;
    private FortschrittAnsicht fortschrittPanel;
    private final List<JPanel> panelListe = new ArrayList<>();
    private JPanel aktuellesPanel;

    public ButtonPanelHf(Control control, Hauptfenster hf) {
        this.control = control;
        this.hf = hf;
        setLayout(new BorderLayout());
        initAufgabenAnsicht();
        initLerngruppe();
        showPanel(0);
    }

    private void initLerngruppe() {
        // Button neue Lerngruppe
        JButton buttonNeueLG = new JButton(I18n.t("ui.Lerngruppe.Fenstertitel"));
        buttonNeueLG.addActionListener(e -> {
            new NeueLerngruppe(hf.getCenterPanel(), control);
        });
        JPanel panel = new JPanel();
        panel.add(buttonNeueLG);
        panelListe.add(1, panel);
    }

    private void initAufgabenAnsicht() {
        // Button neue Aufgabe
        JButton buttonNeueAufgabe = new JButton(I18n.t("ui.Main.ButtonNeueAufgabe"));
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(hf.getCenterPanel(), control));
        // Button Modul einplanen
        JButton buttonModulPlanen = new JButton(I18n.t("ui.Main.ButtonModulEinplanen"));
        buttonModulPlanen.addActionListener(e -> new ModulEinplanen(control, hf));
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(buttonNeueAufgabe, 0);
        buttons.add(buttonModulPlanen, 1);
        panelListe.add(0, buttons);
    }

    @Override
    public void refresh() {
        switch (hf.getCenterPanel().getIndex()) {
        case 4:
            showPanel(1);
            break;
        default:
            showPanel(0);
            break;
        }
        revalidate();
        repaint();
    }

    public void showPanel(int index) {
        if (index < 0 || index >= panelListe.size()) return;
        if (aktuellesPanel != null) remove(aktuellesPanel);
        aktuellesPanel = panelListe.get(index);
        add(aktuellesPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
