package vacanza;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ReservationPanel extends BasePanel {

    boolean loading = false;
    String selectedReservationId = "";

    JPanel pnlform = new JPanel();
    JLabel lblresno = new JLabel("Reservation No.:");
    JLabel lblguest = new JLabel("Guest:");
    JLabel lblroom = new JLabel("Room:");
    JLabel lblcheckin = new JLabel("Check-in date:");
    JLabel lblnights = new JLabel("Nights:");
    JLabel lblcheckout = new JLabel("Check-out date:");
    JLabel lbltotal = new JLabel("Total amount:");
    JLabel lblnote = new JLabel("");

    JTextField txtresno = new JTextField();
    JComboBox<String> cboguest = new JComboBox<>();
    JComboBox<String> cboroom = new JComboBox<>();
    JTextArea txtdetails = new JTextArea();
    JTextField txtcheckin = new JTextField();
    JSpinner spnnights = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
    JTextField txtcheckout = new JTextField();
    JTextField txttotal = new JTextField();

    JButton btnnew = new JButton("New");
    JButton btnbook = new JButton("Book");
    JButton btncheckin = new JButton("Check In");
    JButton btncheckout = new JButton("Check Out");
    JButton btncancel = new JButton("Cancel Res.");
    JButton btnclear = new JButton("Clear");
    JButton btndelete = new JButton("Delete");

    String[] columns = {"Res. No.", "Guest", "Room", "Type", "Check-in", "Check-out", "Nights", "Total", "Status"};
    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);

    JLabel lblsearch = new JLabel("Search:");
    JTextField txtsearch = new JTextField();
    String[] filters = {"All", "Reserved", "Checked In", "Checked Out", "Cancelled"};
    JComboBox<String> cbofilter = new JComboBox<>(filters);
    JButton btnsearch = new JButton("Search");
    JButton btnshowall = new JButton("Show All");
    JLabel lblcount = new JLabel("");

    public ReservationPanel(User user) {
        super(user);

        this.setLayout(new BorderLayout());
        this.setBackground(Colors.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        this.add(UI.createTitleBar("Reservations", "Book a room, check in, check out or cancel"), BorderLayout.NORTH);

        pnlform.setLayout(null);
        pnlform.setBackground(Color.WHITE);
        pnlform.setPreferredSize(new java.awt.Dimension(400, 100));
        pnlform.setBorder(BorderFactory.createTitledBorder("Reservation Information"));

        pnlform.add(lblresno);
        lblresno.setBounds(20, 25, 120, 25);
        pnlform.add(txtresno);
        txtresno.setBounds(150, 25, 220, 26);
        txtresno.setEnabled(false);

        pnlform.add(lblguest);
        lblguest.setBounds(20, 59, 120, 25);
        pnlform.add(cboguest);
        cboguest.setBounds(150, 59, 220, 26);
        cboguest.addActionListener(this);

        pnlform.add(lblroom);
        lblroom.setBounds(20, 93, 120, 25);
        pnlform.add(cboroom);
        cboroom.setBounds(150, 93, 220, 26);
        cboroom.addActionListener(this);

        pnlform.add(txtdetails);
        txtdetails.setBounds(20, 124, 350, 108);
        txtdetails.setEditable(false);
        txtdetails.setLineWrap(true);
        txtdetails.setWrapStyleWord(true);
        txtdetails.setBackground(new Color(250, 247, 238));
        txtdetails.setBorder(BorderFactory.createTitledBorder("Room Details"));

        pnlform.add(lblcheckin);
        lblcheckin.setBounds(20, 238, 120, 25);
        pnlform.add(txtcheckin);
        txtcheckin.setBounds(150, 238, 220, 26);

        pnlform.add(lblnights);
        lblnights.setBounds(20, 270, 120, 25);
        pnlform.add(spnnights);
        spnnights.setBounds(150, 270, 220, 26);

        pnlform.add(lblcheckout);
        lblcheckout.setBounds(20, 302, 120, 25);
        pnlform.add(txtcheckout);
        txtcheckout.setBounds(150, 302, 220, 26);
        txtcheckout.setEnabled(false);

        pnlform.add(lbltotal);
        lbltotal.setBounds(20, 334, 120, 25);
        pnlform.add(txttotal);
        txttotal.setBounds(150, 334, 220, 26);
        txttotal.setEnabled(false);

        pnlform.add(btnnew);
        btnnew.setBounds(20, 376, 105, 30);
        pnlform.add(btnbook);
        btnbook.setBounds(135, 376, 105, 30);
        pnlform.add(btnclear);
        btnclear.setBounds(250, 376, 105, 30);
        pnlform.add(btncheckin);
        btncheckin.setBounds(20, 412, 105, 30);
        pnlform.add(btncheckout);
        btncheckout.setBounds(135, 412, 105, 30);
        pnlform.add(btncancel);
        btncancel.setBounds(250, 412, 105, 30);

        pnlform.add(btndelete);
        btndelete.setBounds(20, 448, 105, 30);

        pnlform.add(lblnote);
        lblnote.setBounds(20, 486, 350, 25);
        lblnote.setForeground(new Color(150, 100, 0));

        btnnew.addActionListener(this);
        btnbook.addActionListener(this);
        btncheckin.addActionListener(this);
        btncheckout.addActionListener(this);
        btncancel.addActionListener(this);
        btnclear.addActionListener(this);
        btndelete.addActionListener(this);

        KeyAdapter typing = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                computeTotal();
            }
        };
        txtcheckin.addKeyListener(typing);

        spnnights.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                computeTotal();
            }
        });

        txtresno.setToolTipText("The reservation number is created automatically");
        cboguest.setToolTipText("Choose the guest. Returning guests are marked. A new guest must be added in the Guests screen first");
        cboroom.setToolTipText("Only the available rooms are listed");
        txtcheckin.setToolTipText("Format: yyyy-MM-dd, cannot be in the past");
        spnnights.setToolTipText("Number of nights");
        txtcheckout.setToolTipText("Check-in date plus the nights");

        UI.styleTable(table);
        int[] widths = {75, 140, 55, 75, 85, 85, 55, 100, 85};
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
        txtsearch.setBounds(60, 6, 180, 28);
        txtsearch.setToolTipText("Type a reservation number, guest, room or date, ");
        pnlsearch.add(cbofilter);
        cbofilter.setBounds(250, 6, 110, 28);
        cbofilter.setToolTipText("Show only the reservations with this status");
        pnlsearch.add(btnsearch);
        btnsearch.setBounds(370, 6, 80, 28);
        pnlsearch.add(btnshowall);
        btnshowall.setBounds(458, 6, 95, 28);
        pnlsearch.add(lblcount);
        lblcount.setBounds(565, 8, 250, 25);
        lblcount.setForeground(Color.GRAY);

        txtsearch.addActionListener(this);
        cbofilter.addActionListener(this);
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
        String filter = cbofilter.getSelectedItem().toString();
        model.setRowCount(0);

        ArrayList<Reservation> list = Reservation_Store.getAllReservations();
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            String allText = (r.getReservationId() + " " + r.getGuestName() + " " + r.getRoomNumber()
                    + " " + r.getRoomType() + " " + r.getCheckIn() + " " + r.getCheckOut()
                    + " " + r.getStatus()).toLowerCase();

            boolean matchesSearch = keyword.isEmpty() || allText.contains(keyword);
            boolean matchesFilter = filter.equals("All") || r.getStatus().equals(filter);
            if (matchesSearch && matchesFilter) {
                model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getGuestName(),
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.getCheckIn(),
                    r.getCheckOut(),
                    r.getNights(),
                    Validator.peso(r.getTotal()),
                    r.getStatus()
                });
            }
        }
        lblcount.setText("Showing " + model.getRowCount() + " of " + list.size());
    }

    private void loadCombos() {
        loading = true;
        cboguest.removeAllItems();
        for (Guest g : Guest_Store.getAllGuests()) {

            String mark = "";
            if (Reservation_Store.isReturningGuest(g.getGuestId())) {
                mark = " (Returning)";
            }
            cboguest.addItem(g.getGuestId() + " - " + g.getFullName() + mark);
        }
        cboroom.removeAllItems();
        for (Room r : Room_Store.getAvailableRooms()) {
            cboroom.addItem(r.getRoomNumber() + " - " + r.getRoomType());
        }
        loading = false;
    }

    private void clearForm() {
        selectedReservationId = "";
        loadCombos();
        txtresno.setText("");
        txtcheckin.setText("");
        spnnights.setValue(1);
        txtcheckout.setText("");
        txttotal.setText("");
        txtdetails.setText("");
        table.clearSelection();

        setFormEnabled(false);
        btnnew.setEnabled(true);
        btnbook.setEnabled(false);
        btncheckin.setEnabled(false);
        btncheckout.setEnabled(false);
        btncancel.setEnabled(false);
        btndelete.setEnabled(false);
        lblnote.setText("Click New to make a reservation, or select one.");
    }

    private void setFormEnabled(boolean enabled) {
        cboguest.setEnabled(enabled);
        cboroom.setEnabled(enabled);
        txtcheckin.setEnabled(enabled);
        spnnights.setEnabled(enabled);
    }

    private String idOf(JComboBox<String> combo) {
        Object selected = combo.getSelectedItem();
        if (selected == null) {
            return "";
        }
        String text = selected.toString();
        int index = text.indexOf(" - ");
        if (index < 0) {
            return text;
        }
        return text.substring(0, index);
    }

    private void showGuestInfo() {
        if (loading || !cboguest.isEnabled() || !btnbook.isEnabled()) {
            return;
        }
        String guestId = idOf(cboguest);
        int stays = Reservation_Store.countCompletedStays(guestId);
        if (stays == 0) {
            lblnote.setText("Fill in the form, then click Book.");
            return;
        }
        lblnote.setText("Returning guest: " + stays + " stay(s) - 10% discount");
    }

    private double discountFor(String guestId) {
        if (Reservation_Store.isReturningGuest(guestId)) {
            return Reservation.RETURNING_DISCOUNT;
        }
        return 0;
    }

    private LocalDate readDate() {
        try {
            return LocalDate.parse(txtcheckin.getText().trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private void computeTotal() {
        if (loading || !spnnights.isEnabled()) {
            return;
        }
        Room room = Room_Store.findRoom(idOf(cboroom));
        if (room == null) {
            txtdetails.setText("");
            txtcheckout.setText("");
            txttotal.setText("");
            return;
        }
        double discount = discountFor(idOf(cboguest));
        String discountText = "";
        if (discount > 0) {
            discountText = "\nReturning guest discount: " + Math.round(discount * 100) + "%";
        }
        txtdetails.setText("Rate: " + Validator.peso(room.getRate()) + " per night" + discountText
                + "\n" + room.getDetails());

        int nights = (Integer) spnnights.getValue();
        LocalDate checkIn = readDate();

        txttotal.setText(Validator.peso(room.computeTotal(nights) * (1 - discount)));
        if (checkIn == null) {
            txtcheckout.setText("");
        } else {
            txtcheckout.setText(checkIn.plusDays(nights).toString());
        }
    }

    private void tableRowClicked() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        Reservation r = Reservation_Store.findReservation(model.getValueAt(row, 0).toString());
        if (r == null) {
            loadTable();
            return;
        }

        selectedReservationId = r.getReservationId();
        loading = true;
        cboguest.removeAllItems();
        cboguest.addItem(r.getGuestId() + " - " + r.getGuestName());
        cboroom.removeAllItems();
        cboroom.addItem(r.getRoomNumber() + " - " + r.getRoomType());
        loading = false;

        txtresno.setText(r.getReservationId());
        txtcheckin.setText(r.getCheckIn().toString());
        spnnights.setValue(r.getNights());
        txtcheckout.setText(r.getCheckOut().toString());
        txttotal.setText(Validator.peso(r.getTotal()));

        String text = "Status: " + r.getStatus() + " (reserved by " + r.getCreatedBy() + ")"
                + "\nRate: " + Validator.peso(r.getRate()) + " per night";
        if (r.getDiscount() > 0) {
            text = text + "\nReturning guest discount: " + Math.round(r.getDiscount() * 100) + "%";
        }
        Room room = Room_Store.findRoom(r.getRoomNumber());
        if (room != null) {
            text = text + "\n" + room.getDetails();
        }
        txtdetails.setText(text);

        setFormEnabled(false);
        btnbook.setEnabled(false);

        btncheckin.setEnabled(r.getStatus().equals("Reserved"));
        btncancel.setEnabled(r.getStatus().equals("Reserved"));
        btncheckout.setEnabled(r.getStatus().equals("Checked In"));

        btndelete.setEnabled(isAdmin() && !r.isOpen());
        if (!r.isOpen() && !isAdmin()) {
            lblnote.setText("This reservation is " + r.getStatus() + ". Only the Admin can delete it.");
        } else if (r.getStatus().equals("Reserved")) {
            lblnote.setText("Click Check In when the guest arrives.");
        } else if (r.getStatus().equals("Checked In")) {
            lblnote.setText("The guest is staying. Click Check Out when leaving.");
        } else {
            lblnote.setText("This reservation is " + r.getStatus() + ".");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == cboroom) {
            computeTotal();
        } else if (source == cboguest) {
            showGuestInfo();
            computeTotal();
        } else if (source == btnsearch || source == txtsearch || source == cbofilter) {
            loadTable();
        } else if (source == btnshowall) {
            txtsearch.setText("");
            cbofilter.setSelectedIndex(0);
            loadTable();
        } else if (source == btnnew) {
            newReservation();
        } else if (source == btnbook) {
            bookRoom();
        } else if (source == btncheckin) {
            checkIn();
        } else if (source == btncheckout) {
            checkOut();
        } else if (source == btncancel) {
            cancelReservation();
        } else if (source == btnclear) {
            clearForm();
        } else if (source == btndelete) {
            deleteRecord();
        }
    }

    private void deleteRecord() {
        if (!isAdmin()) {
            showError("Only the Admin can delete reservation records.");
            return;
        }
        Reservation r = Reservation_Store.findReservation(selectedReservationId);
        if (r == null) {
            showError("Please select a reservation in the table first.");
            return;
        }
        if (r.isOpen()) {
            showError("Only a Checked Out or Cancelled reservation can be deleted. This one is " + r.getStatus() + ".");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete reservation " + r.getReservationId() + " of " + r.getGuestName() + " (" + r.getStatus() + ")?\n"
                + "Its bill and receipt will be deleted too. This cannot be undone.",
                "Delete Record", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        String deleted = r.getReservationId();
        if (!Reservation_Store.deleteReservation(deleted)) {
            showError("The record could not be deleted.");
            return;
        }
        loadTable();
        clearForm();
        showInfo("Reservation " + deleted + " was deleted.");
    }

    private void newReservation() {
        clearForm();

        if (Guest_Store.getAllGuests().isEmpty()) {
            lblnote.setText("There are no guests yet. Add a guest first.");
            showError("There are no guests yet. Please add a guest in the Guests screen first.");
            return;
        }
        if (Room_Store.getAvailableRooms().isEmpty()) {
            lblnote.setText("There are no available rooms.");
            showError("There are no available rooms right now.");
            return;
        }
        txtresno.setText(Reservation_Store.generateReservationId());
        txtcheckin.setText(LocalDate.now().toString());
        spnnights.setValue(1);
        setFormEnabled(true);
        btnbook.setEnabled(true);
        lblnote.setText("Fill in the form, then click Book.");
        computeTotal();
        showGuestInfo();
    }

    private void bookRoom() {

        Guest guest = Guest_Store.findGuest(idOf(cboguest));
        if (guest == null) {
            showError("Please choose a guest.");
            return;
        }
        Room room = Room_Store.findRoom(idOf(cboroom));
        if (room == null) {
            showError("Please choose a room.");
            return;
        }

        if (!room.getStatus().equalsIgnoreCase("Available")) {
            showError("Room " + room.getRoomNumber() + " is no longer available.");
            clearForm();
            return;
        }
        if (txtcheckin.getText().trim().isEmpty()) {
            showError("Please enter the check-in date.");
            return;
        }
        LocalDate checkIn = readDate();
        if (checkIn == null) {
            showError("The check-in date is not valid.");
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            showError("The check-in date cannot be in the past.");
            return;
        }
        if (checkIn.isAfter(LocalDate.now().plusYears(1))) {
            showError("The check-in date cannot be more than 1 year from today.");
            return;
        }

        try {
            spnnights.commitEdit();
        } catch (ParseException ex) {
            showError("Please enter a valid number of nights (1 to 60).");
            return;
        }
        int nights = (Integer) spnnights.getValue();

        double discount = discountFor(guest.getGuestId());
        double total = room.computeTotal(nights) * (1 - discount);
        String message = "Book room " + room.getRoomNumber() + " (" + room.getRoomType() + ") for "
                + guest.getFullName() + "\n" + checkIn + " to " + checkIn.plusDays(nights)
                + " (" + nights + " nights)\nTotal: " + Validator.peso(total);
        if (discount > 0) {
            message = message + "\nReturning guest: " + Math.round(discount * 100) + "% discount included";
        }
        int choice = JOptionPane.showConfirmDialog(this, message,
                "Confirm Reservation", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Reservation reservation = new Reservation(Reservation_Store.generateReservationId(),
                guest.getGuestId(), guest.getFullName(), room.getRoomNumber(), room.getRoomType(),
                room.getRate(), checkIn, nights, "Reserved", currentUser.getUsername(), discount);

        if (!Room_Store.setRoomStatus(room.getRoomNumber(), "Booked")) {
            showError("The room could not be updated.");
            return;
        }
        if (!Reservation_Store.addReservation(reservation)) {

            Room_Store.setRoomStatus(room.getRoomNumber(), "Available");
            showError("The reservation could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Reservation " + reservation.getReservationId()
                + " was saved.\nTotal: " + Validator.peso(reservation.getTotal()));
    }

    private Reservation getSelected(String requiredStatus, String action) {
        Reservation r = Reservation_Store.findReservation(selectedReservationId);
        if (r == null) {
            showError("Please select a reservation in the table first.");
            return null;
        }
        if (!r.getStatus().equals(requiredStatus)) {
            showError("Only a " + requiredStatus + " reservation can be " + action
                    + ". This one is " + r.getStatus() + ".");
            return null;
        }
        return r;
    }

    private void checkIn() {
        Reservation r = getSelected("Reserved", "checked in");
        if (r == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Check in " + r.getGuestName() + " to room " + r.getRoomNumber() + "?\n"
                + "Stay: " + r.getCheckIn() + " to " + r.getCheckOut() + " (" + r.getNights() + " nights)",
                "Confirm Check In", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        if (!Reservation_Store.updateStatus(r.getReservationId(), "Checked In")) {
            showError("The check in could not be saved.");
            return;
        }
        loadTable();
        clearForm();
        JOptionPane.showMessageDialog(this, r.getGuestName() + " is now checked in to room "
                + r.getRoomNumber() + ".");
    }

    private void checkOut() {
        Reservation r = getSelected("Checked In", "checked out");
        if (r == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Check out " + r.getGuestName() + " from room " + r.getRoomNumber() + "?\n"
                + "Total amount: " + Validator.peso(r.getTotal())
                + (Payment_Store.getBalanceCents(r) > 0
                ? "\nBalance still due: " + Validator.peso(Payment_Store.getBalance(r)) + " (not fully paid yet)" : ""),
                "Confirm Check Out", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        if (!Reservation_Store.updateStatus(r.getReservationId(), "Checked Out")) {
            showError("The check out could not be saved.");
            return;
        }

        Room_Store.setRoomStatus(r.getRoomNumber(), "Available");
        loadTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Check out done. Total amount: " + Validator.peso(r.getTotal())
                + "\nYou can print the receipt in the Billing screen.");
    }

    private void cancelReservation() {
        Reservation r = getSelected("Reserved", "cancelled");
        if (r == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel reservation " + r.getReservationId() + " of " + r.getGuestName() + "?"
                + (Payment_Store.getPaidCents(r.getReservationId()) > 0
                ? "\nThis reservation already has payments of " + Validator.peso(Payment_Store.getTotalPaid(r.getReservationId()))
                + ".\nA cancelled bill cannot be paid, so please give the money back to the guest." : ""),
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        if (!Reservation_Store.updateStatus(r.getReservationId(), "Cancelled")) {
            showError("The cancellation could not be saved.");
            return;
        }
        Room_Store.setRoomStatus(r.getRoomNumber(), "Available");
        loadTable();
        clearForm();
        showInfo("Reservation " + r.getReservationId() + " was cancelled.");
    }
}
