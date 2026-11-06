package vacanza;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class UI {

    public static JPanel createTitleBar(String title, String subtitle) {
        JLabel lbltitle = new JLabel(title);
        lbltitle.setFont(new Font("Serif", Font.BOLD, 28));
        lbltitle.setForeground(Colors.NAVY);

        JLabel lblsub = new JLabel(subtitle);
        lblsub.setFont(new Font("Serif", Font.PLAIN, 14));
        lblsub.setForeground(Color.GRAY);

        JPanel pnltitles = new JPanel(new GridLayout(2, 1));
        pnltitles.setBackground(Colors.BACKGROUND);
        pnltitles.add(lbltitle);
        pnltitles.add(lblsub);

        JPanel goldline = new JPanel();
        goldline.setBackground(Colors.GOLD);
        goldline.setPreferredSize(new Dimension(100, 3));

        JPanel pnltop = new JPanel(new BorderLayout(0, 10));
        pnltop.setBackground(Colors.BACKGROUND);
        pnltop.add(pnltitles, BorderLayout.CENTER);
        pnltop.add(goldline, BorderLayout.SOUTH);
        return pnltop;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        table.getTableHeader().setBackground(Colors.NAVY);
        table.getTableHeader().setForeground(Colors.GOLD);
    }
}
