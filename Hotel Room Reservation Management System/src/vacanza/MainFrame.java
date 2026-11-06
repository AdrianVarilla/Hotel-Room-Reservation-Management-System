package vacanza;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame implements ActionListener {

    private User currentUser;
    private CardLayout cards = new CardLayout();
    private JPanel cardPanel = new JPanel(cards);
    private JPanel menu = new JPanel(new GridLayout(0, 1, 0, 10));
    private ArrayList<JButton> menuButtons = new ArrayList<>();
    private ArrayList<BasePanel> screens = new ArrayList<>();
    private JButton btnChangePassword = new JButton("Change Password");
    private JButton btnLogout = new JButton("Log Out");

    public MainFrame(User user) {
        currentUser = user;

        setTitle("Vacanza");
        setSize(1100, 650);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Colors.NAVY);
        sidebar.setPreferredSize(new Dimension(220, 100));
        menu.setBackground(Colors.NAVY);
        menu.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        sidebar.add(menu, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);

        // Show the room and user screens only for admins.
        if (currentUser.getRole().equalsIgnoreCase("Admin")) {
            addScreen("Rooms", new RoomPanel(currentUser));
        }
        addScreen("Guests", new GuestPanel(currentUser));
        addScreen("Reservations", new ReservationPanel(currentUser));
        addScreen("Billing", new BillingPanel(currentUser));
        if (currentUser.getRole().equalsIgnoreCase("Admin")) {
            addScreen("Users", new UserPanel(currentUser));
        }
        add(cardPanel, BorderLayout.CENTER);

        showScreen(menuButtons.get(0).getText());
    }

    private void addScreen(String name, BasePanel screen) {
        JButton b = new JButton(name);
        b.setFont(new Font("Dialog", Font.BOLD, 16));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(14, 10, 14, 10)));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBackground(Colors.NAVY);
        b.setForeground(Color.WHITE);
        b.addActionListener(this);

        menu.add(b);
        menuButtons.add(b);
        screens.add(screen);
        cardPanel.add(screen, name);
    }

    private void showScreen(String name) {
        cards.show(cardPanel, name);
        for (int i = 0; i < menuButtons.size(); i++) {
            JButton b = menuButtons.get(i);
            if (b.getText().equals(name)) {
                b.setBackground(Colors.GOLD);
                b.setForeground(Colors.TEXT_DARK);

                BasePanel screen = screens.get(i);
                screen.refresh();
            } else {
                b.setBackground(Colors.NAVY);
                b.setForeground(Color.WHITE);
            }
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Colors.NAVY);
        header.setPreferredSize(new Dimension(100, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Colors.GOLD));

        JLabel logoLabel = new JLabel("VACANZA");
        logoLabel.setFont(new Font("Serif", Font.BOLD, 28));
        logoLabel.setForeground(Colors.GOLD);
        logoLabel.setIconTextGap(12);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        ImageIcon logoIcon = new ImageIcon("src\\images\\logo.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\images\\logo.png"));
        header.add(logoLabel, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 22));
        right.setBackground(Colors.NAVY);

        JLabel userLabel = new JLabel(currentUser.getUsername() + "  |  " + currentUser.getRole());
        userLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        userLabel.setForeground(new Color(230, 210, 150));

        styleButton(btnChangePassword, Colors.GOLD, Colors.TEXT_DARK);
        styleButton(btnLogout, new Color(40, 70, 115), Color.WHITE);
        btnChangePassword.addActionListener(this);
        btnLogout.addActionListener(this);

        right.add(userLabel);
        right.add(btnChangePassword);
        right.add(btnLogout);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private void styleButton(JButton b, Color background, Color textColor) {
        b.setBackground(background);
        b.setForeground(textColor);
        b.setFont(new Font("Dialog", Font.BOLD, 15));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogout) {
            logout();
        } else if (e.getSource() == btnChangePassword) {
            changePassword();
        } else {

            for (JButton b : menuButtons) {
                if (e.getSource() == b) {
                    showScreen(b.getText());
                }
            }
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
                "Log Out", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }

    private void changePassword() {
        String current = JOptionPane.showInputDialog(this, "Enter your current password:");
        if (current == null) {
            return;
        }
        if (!current.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newPass = JOptionPane.showInputDialog(this, "Enter your new password:");
        if (newPass == null) {
            return;
        }
        if (!Validator.isValidPassword(newPass)) {
            JOptionPane.showMessageDialog(this, "The password must have at least 5 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setPassword(newPass);
        if (User_Store.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "The new password could not be saved.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
