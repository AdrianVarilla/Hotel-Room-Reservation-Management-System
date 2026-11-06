package vacanza;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener {

    JLabel lbllogo = new JLabel();
    JLabel lblname = new JLabel("VACANZA");
    JLabel lblsubtitle = new JLabel("Hotel Room Reservation Management System");
    JLabel lblrole = new JLabel("Sign in as:");
    JLabel lblusername = new JLabel("Username:");
    JLabel lblpassword = new JLabel("Password:");
    JLabel lblerror = new JLabel("");

    JRadioButton rdoadmin = new JRadioButton("Admin", true);
    JRadioButton rdostaff = new JRadioButton("Staff");
    ButtonGroup rolegroup = new ButtonGroup();

    JTextField txtusername = new JTextField();
    JPasswordField txtpassword = new JPasswordField();
    JCheckBox chkshow = new JCheckBox("Show password");
    JButton btnlogin = new JButton("Log In");

    int failedAttempts = 0;
    String lastUsername = "";

    public Login() {

        this.setSize(400, 540);
        this.setTitle("Vacanza - Log In");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Colors.NAVY);
        this.setLayout(null);

        ImageIcon logoIcon = new ImageIcon("src\\images\\logo.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledLogo);
        lbllogo.setIcon(logoIcon);

        Image appIcon = Toolkit.getDefaultToolkit().getImage("src\\images\\logo.png");
        this.setIconImage(appIcon);

        this.add(lbllogo);
        lbllogo.setBounds(150, 20, 100, 100);

        this.add(lblname);
        lblname.setBounds(0, 125, 400, 35);
        lblname.setHorizontalAlignment(SwingConstants.CENTER);
        lblname.setFont(new Font("Serif", Font.BOLD, 30));
        lblname.setForeground(Colors.GOLD);

        this.add(lblsubtitle);
        lblsubtitle.setBounds(0, 160, 400, 20);
        lblsubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblsubtitle.setFont(new Font("Serif", Font.PLAIN, 13));
        lblsubtitle.setForeground(new Color(230, 210, 150));

        this.add(lblrole);
        lblrole.setBounds(60, 205, 90, 20);
        lblrole.setForeground(Color.WHITE);

        this.add(rdoadmin);
        rdoadmin.setBounds(150, 205, 80, 20);
        rdoadmin.setOpaque(false);
        rdoadmin.setForeground(Color.WHITE);

        this.add(rdostaff);
        rdostaff.setBounds(240, 205, 80, 20);
        rdostaff.setOpaque(false);
        rdostaff.setForeground(Color.WHITE);

        rolegroup.add(rdoadmin);
        rolegroup.add(rdostaff);

        this.add(lblusername);
        lblusername.setBounds(60, 245, 100, 20);
        lblusername.setForeground(Color.WHITE);

        this.add(txtusername);
        txtusername.setBounds(60, 270, 280, 30);

        this.add(lblpassword);
        lblpassword.setBounds(60, 310, 100, 20);
        lblpassword.setForeground(Color.WHITE);

        this.add(txtpassword);
        txtpassword.setBounds(60, 335, 280, 30);

        this.add(chkshow);
        chkshow.setBounds(60, 372, 200, 20);
        chkshow.setOpaque(false);
        chkshow.setForeground(Color.WHITE);
        chkshow.addActionListener(this);

        this.add(lblerror);
        lblerror.setBounds(60, 400, 290, 20);
        lblerror.setForeground(new Color(255, 140, 140));

        this.add(btnlogin);
        btnlogin.setBounds(60, 435, 280, 40);
        btnlogin.setBackground(Colors.GOLD);
        btnlogin.setForeground(Colors.TEXT_DARK);
        btnlogin.setFont(new Font("Serif", Font.BOLD, 16));
        btnlogin.addActionListener(this);

        this.getRootPane().setDefaultButton(btnlogin);

        rdoadmin.setToolTipText("Choose Admin if your account is an Admin account");
        rdostaff.setToolTipText("Choose Staff if your account is a Staff account");
        txtusername.setToolTipText("Type your username");
        txtpassword.setToolTipText("Type your password. 3 wrong attempts will lock the account");
        chkshow.setToolTipText("See the password");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chkshow) {

            if (chkshow.isSelected()) {
                txtpassword.setEchoChar((char) 0);
            } else {
                txtpassword.setEchoChar('\u2022');
            }
        }

        if (e.getSource() == btnlogin) {
            login();
        }
    }

    private void login() {
        lblerror.setText("");
        String username = txtusername.getText().trim();
        String password = new String(txtpassword.getPassword());
        String chosenRole = "Staff";
        if (rdoadmin.isSelected()) {
            chosenRole = "Admin";
        }

        if (username.isEmpty() || password.isEmpty()) {
            lblerror.setText("Please enter both username and password.");
            return;
        }

        // Reset the attempt count when the username changes.
        if (!username.equalsIgnoreCase(lastUsername)) {
            failedAttempts = 0;
            lastUsername = username;
        }

        User user = User_Store.findUser(username);

        if (user == null) {
            lblerror.setText("Account not found.");
            return;
        }

        if (user.isLocked()) {
            lblerror.setText("Account is locked. Contact an Admin.");
            return;
        }

        if (!user.getPassword().equals(password)) {
            failedAttempts++;
            if (failedAttempts >= 3) {
                user.setLocked(true);
                User_Store.updateUser(user);
                lblerror.setText("Too many failed attempts. Account locked.");
            } else {
                lblerror.setText("Incorrect password. Attempt " + failedAttempts + " of 3.");
            }
            return;
        }

        // Check that the selected role matches this account.
        if (!user.getRole().equalsIgnoreCase(chosenRole)) {
            lblerror.setText("This account is not registered as " + chosenRole + ".");
            return;
        }

        failedAttempts = 0;
        this.dispose();
        MainFrame main = new MainFrame(user);
        main.setVisible(true);
    }
}
