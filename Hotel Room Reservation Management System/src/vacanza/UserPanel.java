package vacanza;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class UserPanel extends BasePanel {

    String selectedUsername = "";

    JPanel pnlform = new JPanel();
    JLabel lblusername = new JLabel("Username:");
    JLabel lblpassword = new JLabel("Password:");
    JLabel lblrole = new JLabel("Role:");
    JLabel lblnote = new JLabel("");

    JTextField txtusername = new JTextField();
    JPasswordField txtpassword = new JPasswordField();
    String[] roles = {"Staff", "Admin"};
    JComboBox<String> cborole = new JComboBox<>(roles);

    JButton btnadd = new JButton("Add User");
    JButton btnreset = new JButton("Reset Password");
    JButton btnunblock = new JButton("Unblock");
    JButton btndelete = new JButton("Delete");
    JButton btnclear = new JButton("Clear");

    String[] columns = {"Username", "Role", "Status"};
    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);
    JLabel lblcount = new JLabel("");

    public UserPanel(User user) {
        super(user);

        this.setLayout(new BorderLayout());
        this.setBackground(Colors.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        this.add(UI.createTitleBar("Users", "Admin and Staff accounts"), BorderLayout.NORTH);

        pnlform.setLayout(null);
        pnlform.setBackground(Color.WHITE);
        pnlform.setPreferredSize(new java.awt.Dimension(370, 100));
        pnlform.setBorder(BorderFactory.createTitledBorder("Account Information"));

        pnlform.add(lblusername);
        lblusername.setBounds(20, 30, 110, 25);
        pnlform.add(txtusername);
        txtusername.setBounds(140, 30, 200, 26);

        pnlform.add(lblpassword);
        lblpassword.setBounds(20, 68, 110, 25);
        pnlform.add(txtpassword);
        txtpassword.setBounds(140, 68, 200, 26);

        pnlform.add(lblrole);
        lblrole.setBounds(20, 106, 110, 25);
        pnlform.add(cborole);
        cborole.setBounds(140, 106, 200, 26);

        pnlform.add(btnadd);
        btnadd.setBounds(20, 155, 150, 30);
        pnlform.add(btnreset);
        btnreset.setBounds(190, 155, 150, 30);
        pnlform.add(btnunblock);
        btnunblock.setBounds(20, 193, 150, 30);
        pnlform.add(btndelete);
        btndelete.setBounds(190, 193, 150, 30);
        pnlform.add(btnclear);
        btnclear.setBounds(20, 231, 150, 30);

        pnlform.add(lblnote);
        lblnote.setBounds(20, 272, 330, 40);
        lblnote.setForeground(new Color(150, 100, 0));

        btnadd.addActionListener(this);
        btnreset.addActionListener(this);
        btnunblock.addActionListener(this);
        btndelete.addActionListener(this);
        btnclear.addActionListener(this);

        txtusername.setToolTipText("3 to 20 letters or numbers ");
        txtpassword.setToolTipText("At least 5 characters, no spaces and no commas");
        cborole.setToolTipText("Admin can manage everything.\nStaff can only use reservations, guests and billing");

        UI.styleTable(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableRowClicked();
            }
        });

        lblcount.setForeground(Color.GRAY);
        JPanel pnlinfo = new JPanel(new BorderLayout());
        pnlinfo.setBackground(Colors.BACKGROUND);
        pnlinfo.setPreferredSize(new java.awt.Dimension(100, 30));
        pnlinfo.add(lblcount, BorderLayout.CENTER);

        JPanel pnlright = new JPanel(new BorderLayout(0, 10));
        pnlright.setBackground(Colors.BACKGROUND);
        pnlright.add(pnlinfo, BorderLayout.NORTH);
        pnlright.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlcenter = new JPanel(new BorderLayout(20, 0));
        pnlcenter.setBackground(Colors.BACKGROUND);
        pnlcenter.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        pnlcenter.add(pnlform, BorderLayout.WEST);
        pnlcenter.add(pnlright, BorderLayout.CENTER);
        this.add(pnlcenter, BorderLayout.CENTER);

        refresh();
    }

    @Override
    public void refresh() {
        loadTable();
        clearForm();
    }

    private void loadTable() {
        model.setRowCount(0);
        ArrayList<User> users = User_Store.getAllUsers();
        int locked = 0;
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String status = "Active";
            if (u.isLocked()) {
                status = "Locked";
                locked++;
            }
            model.addRow(new Object[]{u.getUsername(), u.getRole(), status});
        }
        lblcount.setText(users.size() + " accounts, " + locked + " locked");
    }

    private void clearForm() {
        selectedUsername = "";
        txtusername.setText("");
        txtpassword.setText("");
        cborole.setSelectedIndex(0);
        table.clearSelection();

        txtusername.setEnabled(true);
        cborole.setEnabled(true);
        btnadd.setEnabled(isAdmin());
        btnreset.setEnabled(false);
        btnunblock.setEnabled(false);
        btndelete.setEnabled(false);
        lblnote.setText("Type a username, password and role, then click Add User.");
    }

    private void tableRowClicked() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        User u = User_Store.findUser(model.getValueAt(row, 0).toString());
        if (u == null) {
            refresh();
            return;
        }
        selectedUsername = u.getUsername();
        txtusername.setText(u.getUsername());
        txtpassword.setText("");
        cborole.setSelectedItem(u.getRole());

        txtusername.setEnabled(false);
        cborole.setEnabled(false);
        btnadd.setEnabled(false);
        btnreset.setEnabled(true);
        btnunblock.setEnabled(u.isLocked());

        btndelete.setEnabled(!u.getUsername().equalsIgnoreCase(currentUser.getUsername()));

        if (u.isLocked()) {
            lblnote.setText(u.getUsername() + " is locked.");
        } else if (u.getUsername().equalsIgnoreCase(currentUser.getUsername())) {
            lblnote.setText("This is your own account. It cannot be deleted.");
        } else {
            lblnote.setText("To change, type a new one and click Reset Password.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnadd) {
            addUser();
        } else if (source == btnreset) {
            resetPassword();
        } else if (source == btnunblock) {
            unblockUser();
        } else if (source == btndelete) {
            deleteUser();
        } else if (source == btnclear) {
            clearForm();
        }
    }

    private void addUser() {
        if (!isAdmin()) {
            showError("Only the Admin can add accounts.");
            return;
        }
        String username = txtusername.getText().trim();
        String password = new String(txtpassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter the username and the password.");
            return;
        }
        if (!Validator.isValidUsername(username)) {
            showError("The username must have 3 to 20 letters, numbers or underscore.");
            return;
        }
        if (User_Store.usernameExists(username)) {
            showError("The username " + username + " is already used.");
            return;
        }
        if (!Validator.isValidPassword(password)) {
            showError("The password must have at least 5 characters, with no spaces and no commas.");
            return;
        }

        User user = new User(username, password, cborole.getSelectedItem().toString(), false);
        if (!User_Store.addUser(user)) {
            showError("The account could not be saved.");
            return;
        }
        refresh();
        showInfo("Account " + username + " was added.");
    }

    private void resetPassword() {
        User u = User_Store.findUser(selectedUsername);
        if (u == null) {
            showError("Please select an account in the table first.");
            return;
        }
        String password = new String(txtpassword.getPassword());
        if (!Validator.isValidPassword(password)) {
            showError("Type the new password first (at least 5 characters, no spaces and no commas).");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Change the password of " + u.getUsername() + "?", "Reset Password",
                JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        u.setPassword(password);
        if (!User_Store.updateUser(u)) {
            showError("The password could not be saved.");
            return;
        }
        refresh();
        showInfo("The password of " + u.getUsername() + " was changed.");
    }

    private void unblockUser() {
        User u = User_Store.findUser(selectedUsername);
        if (u == null) {
            showError("Please select an account in the table first.");
            return;
        }
        if (!u.isLocked()) {
            showError("This account is not locked.");
            return;
        }
        u.setLocked(false);
        if (!User_Store.updateUser(u)) {
            showError("The account could not be saved.");
            return;
        }
        refresh();
        showInfo(u.getUsername() + " was unblocked. The user can sign in again.");
    }

    private void deleteUser() {
        User u = User_Store.findUser(selectedUsername);
        if (u == null) {
            showError("Please select an account in the table first.");
            return;
        }
        if (u.getUsername().equalsIgnoreCase(currentUser.getUsername())) {
            showError("You cannot delete your own account.");
            return;
        }
        // Keep at least one admin account in the system.
        if (u.getRole().equalsIgnoreCase("Admin") && User_Store.countAdmins() <= 1) {
            showError("You cannot delete the last Admin account.");
            return;
        }
        if (!AdminCheck.confirm(this, currentUser, "delete the account " + u.getUsername())) {
            return;
        }
        String deleted = u.getUsername();
        if (!User_Store.deleteUser(deleted)) {
            showError("The account could not be deleted.");
            return;
        }
        refresh();
        showInfo("Account " + deleted + " was deleted.");
    }
}
