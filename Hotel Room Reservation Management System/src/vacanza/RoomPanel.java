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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RoomPanel extends BasePanel {

    boolean adding = false;
    String selectedRoomNumber = "";

    JPanel pnlform = new JPanel();
    JLabel lblroomno = new JLabel("Room No.:");
    JLabel lbltype = new JLabel("Room Type:");
    JLabel lblfloor = new JLabel("Floor:");
    JLabel lblrate = new JLabel("Rate per night:");
    JLabel lblstatus = new JLabel("Status:");
    JLabel lblnote = new JLabel("");
    JTextArea txtdetails = new JTextArea();

    JTextField txtroomno = new JTextField();
    String[] types = {"Standard", "Deluxe", "Suite"};
    JComboBox<String> cbotype = new JComboBox<>(types);
    String[] floors = {"1", "2", "3", "4", "5"};
    JComboBox<String> cbofloor = new JComboBox<>(floors);
    JTextField txtrate = new JTextField();
    String[] statuses = {"Available", "Booked"};
    JComboBox<String> cbostatus = new JComboBox<>(statuses);

    JButton btnnew = new JButton("New");
    JButton btnadd = new JButton("Add");
    JButton btnupdate = new JButton("Update");
    JButton btndelete = new JButton("Delete");
    JButton btnclear = new JButton("Clear");

    String[] columns = {"Room No.", "Type", "Floor", "Rate / night", "Status"};
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

    public RoomPanel(User user) {
        super(user);

        this.setLayout(new BorderLayout());
        this.setBackground(Colors.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        this.add(UI.createTitleBar("Rooms", "Room records"), BorderLayout.NORTH);

        pnlform.setLayout(null);
        pnlform.setBackground(Color.WHITE);
        pnlform.setPreferredSize(new java.awt.Dimension(370, 100));
        pnlform.setBorder(BorderFactory.createTitledBorder("Room Information"));

        pnlform.add(lblroomno);
        lblroomno.setBounds(20, 30, 110, 25);
        pnlform.add(txtroomno);
        txtroomno.setBounds(140, 30, 200, 26);
        txtroomno.setEnabled(false);

        pnlform.add(lbltype);
        lbltype.setBounds(20, 68, 110, 25);
        pnlform.add(cbotype);
        cbotype.setBounds(140, 68, 200, 26);
        cbotype.addActionListener(this);

        pnlform.add(lblfloor);
        lblfloor.setBounds(20, 106, 110, 25);
        pnlform.add(cbofloor);
        cbofloor.setBounds(140, 106, 200, 26);
        cbofloor.addActionListener(this);

        pnlform.add(lblrate);
        lblrate.setBounds(20, 144, 110, 25);
        pnlform.add(txtrate);
        txtrate.setBounds(140, 144, 200, 26);

        pnlform.add(lblstatus);
        lblstatus.setBounds(20, 182, 110, 25);
        pnlform.add(cbostatus);
        cbostatus.setBounds(140, 182, 200, 26);

        pnlform.add(txtdetails);
        txtdetails.setBounds(20, 222, 320, 100);
        txtdetails.setEditable(false);
        txtdetails.setLineWrap(true);
        txtdetails.setWrapStyleWord(true);
        txtdetails.setBackground(new Color(250, 247, 238));
        txtdetails.setBorder(BorderFactory.createTitledBorder("Room Details"));

        pnlform.add(btnnew);
        btnnew.setBounds(20, 335, 100, 30);
        pnlform.add(btnadd);
        btnadd.setBounds(135, 335, 100, 30);
        pnlform.add(btnupdate);
        btnupdate.setBounds(250, 335, 100, 30);
        pnlform.add(btndelete);
        btndelete.setBounds(20, 373, 100, 30);
        pnlform.add(btnclear);
        btnclear.setBounds(135, 373, 100, 30);

        pnlform.add(lblnote);
        lblnote.setBounds(20, 410, 330, 25);
        lblnote.setForeground(new Color(150, 100, 0));

        btnnew.addActionListener(this);
        btnadd.addActionListener(this);
        btnupdate.addActionListener(this);
        btndelete.addActionListener(this);
        btnclear.addActionListener(this);

        txtroomno.setToolTipText("Created automatically from the floor");
        cbotype.setToolTipText("Choose the room type. It sets the bed and amenities");
        cbofloor.setToolTipText("Choose the floor (1 to 5). Each floor has 10 rooms. The floor cannot be changed after the room is added");
        txtrate.setToolTipText("Rate per night in pesos");
        cbostatus.setToolTipText("Available rooms can be reserved. A Booked room cannot be reserved");
        txtdetails.setToolTipText("The details of the chosen room type");

        UI.styleTable(table);
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
        txtsearch.setToolTipText("Type a room number, type, floor, rate or status");
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

        ArrayList<Room> rooms = Room_Store.getAllRooms();
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            String allText = (room.getRoomNumber() + " " + room.getRoomType() + " "
                    + room.getFloor() + " " + room.getRate() + " " + room.getStatus()).toLowerCase();

            if (keyword.isEmpty() || allText.contains(keyword)) {

                model.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getFloor(),
                    Validator.peso(room.getRate()),
                    room.getStatus()
                });
            }
        }
        lblcount.setText("Showing " + model.getRowCount() + " of "
                + rooms.size() + " rooms");
    }

    private void clearForm() {
        adding = false;
        selectedRoomNumber = "";
        cbotype.setSelectedIndex(0);
        txtroomno.setText("");
        cbofloor.setSelectedIndex(0);
        txtrate.setText("");
        cbostatus.setSelectedIndex(0);
        txtdetails.setText("");
        table.clearSelection();

        setFormEnabled(false);
        btnadd.setEnabled(false);
        btnupdate.setEnabled(false);
        btndelete.setEnabled(false);

        if (isAdmin()) {
            btnnew.setEnabled(true);
            lblnote.setText("Click New to add a room, or select a room.");
        } else {
            btnnew.setEnabled(false);
            lblnote.setText("Staff can only view the rooms.");
        }
    }

    private void setFormEnabled(boolean enabled) {
        cbotype.setEnabled(enabled);
        cbofloor.setEnabled(enabled);
        txtrate.setEnabled(enabled);
        cbostatus.setEnabled(enabled);
    }

    private void showDetails() {
        Room sample = Room_Store.createSample(cbotype.getSelectedItem().toString());
        txtdetails.setText(sample.getDetails());
    }

    private void showDefaultRate() {
        Room sample = Room_Store.createSample(cbotype.getSelectedItem().toString());
        txtrate.setText(String.format("%.2f", sample.getDefaultRate()));
    }

    private void tableRowClicked() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        Room room = Room_Store.findRoom(model.getValueAt(row, 0).toString());
        if (room == null) {
            loadTable();
            return;
        }

        adding = false;
        selectedRoomNumber = room.getRoomNumber();
        txtroomno.setText(room.getRoomNumber());
        cbotype.setSelectedItem(room.getRoomType());
        cbofloor.setSelectedItem("" + room.getFloor());
        txtrate.setText(String.format("%.2f", room.getRate()));
        cbostatus.setSelectedItem(room.getStatus());
        showDetails();

        if (isAdmin()) {
            setFormEnabled(true);
            cbofloor.setEnabled(false);
            lblnote.setText("");
            btnadd.setEnabled(false);
            btnupdate.setEnabled(true);
            btndelete.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == cbotype) {
            showDetails();
            if (adding) {
                showDefaultRate();
            }
        } else if (source == cbofloor) {
            if (adding) {
                showNewRoomNumber();
            }
        } else if (source == btnsearch || source == txtsearch) {
            loadTable();
            clearForm();
        } else if (source == btnshowall) {
            txtsearch.setText("");
            loadTable();
            clearForm();
        } else if (source == btnnew) {
            newRoom();
        } else if (source == btnadd) {
            addRoom();
        } else if (source == btnupdate) {
            updateRoom();
        } else if (source == btndelete) {
            deleteRoom();
        } else if (source == btnclear) {
            clearForm();
        }
    }

    private void newRoom() {
        if (!isAdmin()) {
            return;
        }
        clearForm();
        adding = true;
        setFormEnabled(true);
        showDetails();
        showDefaultRate();
        showNewRoomNumber();
        btnadd.setEnabled(true);
    }

    private void showNewRoomNumber() {
        int floor = Integer.parseInt(cbofloor.getSelectedItem().toString());
        String number = Room_Store.generateRoomNumber(floor);
        txtroomno.setText(number);
        if (number.isEmpty()) {
            lblnote.setText("Floor " + floor + " is full (" + Room_Store.ROOMS_PER_FLOOR + " rooms).");
        }
    }

    private void addRoom() {
        if (!isAdmin()) {
            showError("Only the Admin can add rooms.");
            return;
        }
        int floor = Integer.parseInt(cbofloor.getSelectedItem().toString());
        String roomNumber = Room_Store.generateRoomNumber(floor);
        if (roomNumber.isEmpty()) {
            showError("Floor " + floor + " is full. Each floor can only have "
                    + Room_Store.ROOMS_PER_FLOOR + " rooms.");
            return;
        }
        double rate = Validator.parseAmount(txtrate.getText());
        if (rate == -1) {
            showError("Please enter a valid rate per night (more than 0, up to 1,000,000).");
            return;
        }
        Room room = Room_Store.createRoom(cbotype.getSelectedItem().toString(), roomNumber,
                floor, cbostatus.getSelectedItem().toString(), rate);
        if (!Room_Store.addRoom(room)) {
            showError("The room could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Room " + room.getRoomNumber() + " was added.");
    }

    private void updateRoom() {
        if (!isAdmin()) {
            showError("Only the Admin can edit rooms.");
            return;
        }
        Room old = Room_Store.findRoom(selectedRoomNumber);
        if (old == null) {
            showError("Please select a room in the table first.");
            return;
        }
        double rate = Validator.parseAmount(txtrate.getText());
        if (rate == -1) {
            showError("Please enter a valid rate per night (more than 0, up to 1,000,000).");
            return;
        }

        String newStatus = cbostatus.getSelectedItem().toString();

        if (newStatus.equals("Available") && Reservation_Store.hasOpenReservationForRoom(old.getRoomNumber())) {
            showError("Room " + old.getRoomNumber() + " has an open reservation (Reserved or Checked In).\n"
                    + "Check out or cancel it first before setting the room to Available.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Save the changes of room " + old.getRoomNumber() + "?",
                "Update Room", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Room room = Room_Store.createRoom(cbotype.getSelectedItem().toString(),
                old.getRoomNumber(), old.getFloor(), newStatus, rate);
        if (!Room_Store.updateRoom(room)) {
            showError("The room could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Room " + room.getRoomNumber() + " was updated.");
    }

    private void deleteRoom() {
        Room room = Room_Store.findRoom(selectedRoomNumber);
        if (room == null) {
            showError("Please select a room in the table first.");
            return;
        }

        // Do not delete a room while it has an open booking.
        if (room.getStatus().equalsIgnoreCase("Booked")) {
            showError("A booked room cannot be deleted.");
            return;
        }

        if (!isAdmin()) {
            showError("Only the Admin can delete a room.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Delete room " + room.getRoomNumber() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        if (!Room_Store.deleteRoom(room.getRoomNumber())) {
            showError("The room could not be deleted.");
            return;
        }
        String deleted = room.getRoomNumber();
        loadTable();
        clearForm();
        showInfo("Room " + deleted + " was deleted.");
    }
}
