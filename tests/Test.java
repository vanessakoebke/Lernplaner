import java.awt.*;

import javax.swing.*;

import util.FALoader;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        DefaultListModel listModel = new DefaultListModel<>();
        JList liste = new JList<>(listModel);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setPreferredSize(new Dimension (400, 300));
        
        JScrollPane scrollpanel = new JScrollPane(liste,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JButton("1"), BorderLayout.NORTH);
//        buttonPanel.add(new JButton("2"), BorderLayout.SOUTH);
        JButton button = new JButton("\uf044");
        button.setPreferredSize(new Dimension(24, 24));
        button.setFont(FALoader.loadFontAwesome());
        button.setFocusPainted(true);
        button.setBorderPainted(false);
        buttonPanel.add(button, BorderLayout.SOUTH);
        
        
        gbc.gridx=0;
        gbc.gridy = 0;
        frame.add(scrollpanel, gbc);
        
        gbc.gridx = 1;
        frame.add(buttonPanel, gbc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
