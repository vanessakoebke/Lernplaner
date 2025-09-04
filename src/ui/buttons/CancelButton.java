package ui.buttons;

import javax.swing.*;
import java.awt.*;

import lang.I18n;

public class CancelButton extends JButton {
    public CancelButton() {
    super(I18n.t("Common.ButtonAbbrechen"));
    this.addActionListener(e -> {
        // Das übergeordnete Fenster suchen
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    });
    }
}
