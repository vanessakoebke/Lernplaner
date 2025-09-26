package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import lang.I18n;
import service.Control;

public class Fortschritt extends JFrame implements IAnsicht {
    
    public Fortschritt(Control control) {
        super(I18n.t("ui.Fortschritt.Fortschritt"));
        setSize(900, 600);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }
}
