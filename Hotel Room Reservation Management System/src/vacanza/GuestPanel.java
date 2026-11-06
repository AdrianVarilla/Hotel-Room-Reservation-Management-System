package vacanza;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GuestPanel extends BasePanel {

    String selectedGuestId = "";

    JPanel pnlform = new JPanel();
    JLabel lblid = new JLabel("Guest ID:");
    JLabel lblname = new JLabel("Full Name:");
    JLabel lbladdress = new JLabel("Address:");
    JLabel lblcontact = new JLabel("Contact No.:");
    JLabel lblemail = new JLabel("Email:");
    JLabel lblnote = new JLabel("");

    JTextField txtid = new JTextField();
    JTextField txtname = new JTextField();
    JTextArea txtaddress = new JTextArea();
    JScrollPane scrolladdress = new JScrollPane(txtaddress);
    JTextField txtcontact = new JTextField();
    JTextField txtemail = new JTextField();

    JButton btnnew = new JButton("New");
    JButton btnadd = new JButton("Add");
    JButton btnupdate = new JButton("Update");
    JButton btndelete = new JButton("Delete");
    JButton btnclear = new JButton("Clear");
    JButton btnhistory = new JButton("Del. History");

    String[] columns = {"Guest ID", "Full Name", "Address", "Contact No.", "Email", "Stays", "Type"};
    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);

    JLabel lblsearch = new JLabel("Search:");
    JTextField txtsearch = new JTextField();
    JButton btnsearch = new JButton("Search");
    JButton btnshowall = new JButton("Show All");
    JLabel lblcount = new JLabel("");

    public GuestPanel(User user) {
        super(user);

        this.setLayout(new BorderLayout());
        this.setBackground(Colors.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        this.add(UI.createTitleBar("Guests", "Guest records"), BorderLayout.NORTH);

        pnlform.setLayout(null);
        pnlform.setBackground(Color.WHITE);
        pnlform.setPreferredSize(new java.awt.Dimension(370, 100));
        pnlform.setBorder(BorderFactory.createTitledBorder("Guest Information"));

        pnlform.add(lblid);
        lblid.setBounds(20, 30, 110, 25);
        pnlform.add(txtid);
        txtid.setBounds(140, 30, 200, 26);
        txtid.setEnabled(false);

        pnlform.add(lblname);
        lblname.setBounds(20, 68, 110, 25);
        pnlform.add(txtname);
        txtname.setBounds(140, 68, 200, 26);

        pnlform.add(lbladdress);
        lbladdress.setBounds(20, 106, 110, 25);
        pnlform.add(scrolladdress);
        scrolladdress.setBounds(140, 106, 200, 70);
        txtaddress.setLineWrap(true);
        txtaddress.setWrapStyleWord(true);

        pnlform.add(lblcontact);
        lblcontact.setBounds(20, 188, 110, 25);
        pnlform.add(txtcontact);
        txtcontact.setBounds(140, 188, 200, 26);

        pnlform.add(lblemail);
        lblemail.setBounds(20, 226, 110, 25);
        pnlform.add(txtemail);
        txtemail.setBounds(140, 226, 200, 26);

        pnlform.add(btnnew);
        btnnew.setBounds(20, 275, 100, 30);
        pnlform.add(btnadd);
        btnadd.setBounds(135, 275, 100, 30);
        pnlform.add(btnupdate);
        btnupdate.setBounds(250, 275, 100, 30);
        pnlform.add(btndelete);
        btndelete.setBounds(20, 313, 100, 30);
        pnlform.add(btnclear);
        btnclear.setBounds(135, 313, 100, 30);
        pnlform.add(btnhistory);
        btnhistory.setBounds(250, 313, 100, 30);

        pnlform.add(lblnote);
        lblnote.setBounds(20, 350, 330, 25);
        lblnote.setForeground(new Color(150, 100, 0));

        btnnew.addActionListener(this);
        btnadd.addActionListener(this);
        btnupdate.addActionListener(this);
        btndelete.addActionListener(this);
        btnclear.addActionListener(this);
        btnhistory.addActionListener(this);

        txtid.setToolTipText("The guest ID is created automatically");
        txtname.setToolTipText("Example: Juan D. Cruz");
        txtaddress.setToolTipText("Example: 12 Rizal St., Brgy. Poblacion, Tagum City");
        txtcontact.setToolTipText("11 digits only. Example: 09171234567");
        txtemail.setToolTipText("Optional");

        UI.styleTable(table);
        int[] widths = {75, 160, 350, 105, 130, 50, 75};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableRowClicked();
            }
        });
        JScrollPane scroll = new JScrollPane(table);

        JPanel pnlsearch = new JPanel(null);
        pnlsearch.setBackground(Colors.BACKGROUND);
        pnlsearch.setPreferredSize(new java.awt.Dimension(100, 40));

        pnlsearch.add(lblsearch);
        lblsearch.setBounds(0, 8, 60, 25);
        pnlsearch.add(txtsearch);
        txtsearch.setBounds(60, 6, 220, 28);
        txtsearch.setToolTipText("Type a guest ID, name, address, contact or email ");
        pnlsearch.add(btnsearch);
        btnsearch.setBounds(290, 6, 90, 28);
        pnlsearch.add(btnshowall);
        btnshowall.setBounds(390, 6, 100, 28);
        pnlsearch.add(lblcount);
        lblcount.setBounds(505, 8, 250, 25);
        lblcount.setForeground(Color.GRAY);

        txtsearch.addActionListener(this);
        btnsearch.addActionListener(this);
        btnshowall.addActionListener(this);

        JPanel pnlright = new JPanel(new BorderLayout(0, 10));
        pnlright.setBackground(Colors.BACKGROUND);
        pnlright.add(pnlsearch, BorderLayout.NORTH);
        pnlright.add(scroll, BorderLayout.CENTER);

        JPanel pnlcenter = new JPanel(new BorderLayout(20, 0));
        pnlcenter.setBackground(Colors.BACKGROUND);
        pnlcenter.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        pnlcenter.add(pnlform, BorderLayout.WEST);
        pnlcenter.add(pnlright, BorderLayout.CENTER);
        this.add(pnlcenter, BorderLayout.CENTER);

        loadTable();
        clearForm();
    }

    @Override
    public void refresh() {
        loadTable();
        clearForm();
    }

    private void loadTable() {
        String keyword = txtsearch.getText().trim().toLowerCase();
        model.setRowCount(0);

        ArrayList<Guest> guests = Guest_Store.getAllGuests();
        for (int i = 0; i < guests.size(); i++) {
            Guest guest = guests.get(i);
            int stays = Reservation_Store.countCompletedStays(guest.getGuestId());
            String type = "New";
            if (stays > 0) {
                type = "Returning";
            }

            String allText = (guest.getGuestId() + " " + guest.getFullName() + " " + guest.getAddress()
                    + " " + guest.getContactNumber() + " " + guest.getEmail() + " " + type).toLowerCase();

            if (keyword.isEmpty() || allText.contains(keyword)) {
                model.addRow(new Object[]{
                    guest.getGuestId(),
                    guest.getFullName(),
                    guest.getAddress(),
                    guest.getContactNumber(),
                    guest.getEmail(),
                    stays,
                    type
                });
            }
        }
        lblcount.setText("Showing " + model.getRowCount() + " of " + guests.size() + " guests");
    }

    private void clearForm() {
        selectedGuestId = "";
        txtid.setText("");
        txtname.setText("");
        txtaddress.setText("");
        txtcontact.setText("");
        txtemail.setText("");
        table.clearSelection();

        setFormEnabled(false);
        btnadd.setEnabled(false);
        btnupdate.setEnabled(false);
        btndelete.setEnabled(false);
        btnhistory.setEnabled(false);
        btnnew.setEnabled(true);
        lblnote.setText("Click New to add a guest, or select a guest.");
    }

    private void setFormEnabled(boolean enabled) {
        txtname.setEnabled(enabled);
        txtaddress.setEnabled(enabled);
        txtcontact.setEnabled(enabled);
        txtemail.setEnabled(enabled);
    }

    private void tableRowClicked() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        Guest guest = Guest_Store.findGuest(model.getValueAt(row, 0).toString());
        if (guest == null) {
            loadTable();
            return;
        }

        selectedGuestId = guest.getGuestId();
        txtid.setText(guest.getGuestId());
        txtname.setText(guest.getFullName());
        txtaddress.setText(guest.getAddress());
        txtcontact.setText(guest.getContactNumber());
        txtemail.setText(guest.getEmail());

        setFormEnabled(true);
        btnadd.setEnabled(false);
        btnupdate.setEnabled(true);

        btndelete.setEnabled(isAdmin());
        btnhistory.setEnabled(isAdmin());
        int stays = Reservation_Store.countCompletedStays(guest.getGuestId());
        String info = "";
        if (stays > 0) {
            info = "Returning guest: " + stays + " stay(s). ";
        }
        if (!isAdmin()) {
            lblnote.setText(info + "Only the Admin can delete.");
        } else {
            lblnote.setText(info);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnsearch || source == txtsearch) {
            loadTable();
            clearForm();
        } else if (source == btnshowall) {
            txtsearch.setText("");
            loadTable();
            clearForm();
        } else if (source == btnnew) {
            newGuest();
        } else if (source == btnadd) {
            addGuest();
        } else if (source == btnupdate) {
            updateGuest();
        } else if (source == btndelete) {
            deleteGuest();
        } else if (source == btnclear) {
            clearForm();
        } else if (source == btnhistory) {
            deleteHistory();
        }
    }

    private void newGuest() {
        clearForm();
        txtid.setText(Guest_Store.generateGuestId());
        setFormEnabled(true);
        btnadd.setEnabled(true);
        lblnote.setText("Fill in the guest information, then click Add.");
        txtname.requestFocus();
    }

    private boolean inputsAreValid(String guestId) {
        String name = Validator.clean(txtname.getText());
        String address = Validator.clean(txtaddress.getText());
        String contact = Validator.clean(txtcontact.getText());
        String email = Validator.clean(txtemail.getText());

        if (name.isEmpty()) {
            showError("Please enter the full name of the guest.");
            return false;
        }
        if (!Validator.isValidName(name)) {
            showError("The name can only have letters, spaces, dot, apostrophe and dash (2 to 50 characters).");
            return false;
        }
        if (address.length() < 5) {
            showError("Please enter the complete address.");
            return false;
        }
        if (address.length() > 120) {
            showError("The address is too long.");
            return false;
        }
        if (contact.isEmpty()) {
            showError("Please enter the contact number.");
            return false;
        }
        if (!Validator.isValidPhone(contact)) {
            showError("The contact number must be 11 digits. Example: 09171234567");
            return false;
        }
        if (!email.isEmpty() && !Validator.isValidEmail(email)) {
            showError("Please enter a valid email, for example juan@email.com or leave it empty.");
            return false;
        }
        if (Guest_Store.isDuplicate(name, contact, guestId)) {
            showError("This guest is already in the records.");
            return false;
        }
        return true;
    }

    private void addGuest() {
        if (!inputsAreValid(txtid.getText())) {
            return;
        }
        Guest guest = new Guest(txtid.getText(), Validator.clean(txtname.getText()),
                Validator.clean(txtaddress.getText()), Validator.clean(txtcontact.getText()),
                Validator.clean(txtemail.getText()));
        if (!Guest_Store.addGuest(guest)) {
            showError("The guest could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Guest " + guest.getGuestId() + " was added.");
    }

    private void updateGuest() {
        Guest old = Guest_Store.findGuest(selectedGuestId);
        if (old == null) {
            showError("Please select a guest in the table first.");
            return;
        }
        if (!inputsAreValid(old.getGuestId())) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Save the changes of guest " + old.getGuestId() + "?",
                "Update Guest", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        Guest guest = new Guest(old.getGuestId(), Validator.clean(txtname.getText()),
                Validator.clean(txtaddress.getText()), Validator.clean(txtcontact.getText()),
                Validator.clean(txtemail.getText()));
        if (!Guest_Store.updateGuest(guest)) {
            showError("The guest could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Guest " + guest.getGuestId() + " was updated.");
    }

    private void deleteHistory() {
        if (!isAdmin()) {
            showError("Only the Admin can delete the history of a guest.");
            return;
        }
        Guest guest = Guest_Store.findGuest(selectedGuestId);
        int removed = Reservation_Store.deleteHistoryOfGuest(guest.getGuestId());
        if (removed < 0) {
            showError("The history could not be deleted.");
            return;
        }
        loadTable();
        clearForm();
        showInfo(removed + " record(s) of " + guest.getFullName() + " were deleted.");
    }

    private void deleteGuest() {

        Guest guest = Guest_Store.findGuest(selectedGuestId);
        if (guest == null) {
            showError("Please select a guest in the table first.");
            return;
        }

        if (Reservation_Store.hasActiveReservation(guest.getGuestId())) {
            showError("This guest has an open reservation. Check out or cancel it first.");
            return;
        }

        if (!isAdmin()) {
            showError("Only the Admin can delete a guest.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Delete guest " + guest.getFullName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        String deleted = guest.getGuestId();
        if (!Guest_Store.deleteGuest(deleted)) {
            showError("The guest could not be deleted.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Guest " + deleted + " was deleted.");
    }
}
