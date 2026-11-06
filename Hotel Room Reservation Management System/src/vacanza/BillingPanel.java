package vacanza;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class BillingPanel extends BasePanel {

    String[] columns = {"Res. No.", "Guest", "Room", "Nights", "Rate / night", "Total", "Paid", "Balance",
        "Pay Status", "Status"};
    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    JTable table = new JTable(model);

    JLabel lblbilled = new JLabel("", SwingConstants.CENTER);
    JLabel lblpaid = new JLabel("", SwingConstants.CENTER);
    JLabel lbldue = new JLabel("", SwingConstants.CENTER);

    JLabel lblsearch = new JLabel("Search:");
    JTextField txtsearch = new JTextField();
    String[] filters = {"All", "Reserved", "Checked In", "Checked Out", "Cancelled"};
    JComboBox<String> cbofilter = new JComboBox<>(filters);
    JButton btnsearch = new JButton("Search");
    JButton btnshowall = new JButton("Show All");
    JLabel lblcount = new JLabel("");
    JButton btnpay = new JButton("Pay Bill");
    JButton btnreceipt = new JButton("View Receipt");
    JButton btnhistory = new JButton("Payment History");
    JButton btndelete = new JButton("Delete Bill");

    public BillingPanel(User user) {
        super(user);

        this.setLayout(new BorderLayout());
        this.setBackground(Colors.BACKGROUND);
        this.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        this.add(UI.createTitleBar("Billing", "Bills, payments and receipts of the reservations"), BorderLayout.NORTH);

        JPanel pnlcards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlcards.setBackground(Colors.BACKGROUND);
        pnlcards.add(createCard("Total Billed", lblbilled));
        pnlcards.add(createCard("Total Paid", lblpaid));
        pnlcards.add(createCard("Balance Due", lbldue));
        pnlcards.setPreferredSize(new Dimension(780, 70));
        JPanel pnlcardswrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlcardswrap.setBackground(Colors.BACKGROUND);
        pnlcardswrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        pnlcardswrap.add(pnlcards);

        JPanel pnlsearch = new JPanel(null);
        pnlsearch.setBackground(Colors.BACKGROUND);
        pnlsearch.setPreferredSize(new Dimension(100, 40));
        pnlsearch.add(lblsearch);
        lblsearch.setBounds(0, 8, 60, 25);
        pnlsearch.add(txtsearch);
        txtsearch.setBounds(60, 6, 180, 28);
        pnlsearch.add(cbofilter);
        cbofilter.setBounds(250, 6, 110, 28);
        pnlsearch.add(btnsearch);
        btnsearch.setBounds(370, 6, 80, 28);
        pnlsearch.add(btnshowall);
        btnshowall.setBounds(458, 6, 95, 28);
        pnlsearch.add(lblcount);
        lblcount.setBounds(565, 8, 250, 25);
        lblcount.setForeground(Color.GRAY);

        JPanel pnlbuttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlbuttons.setBackground(Colors.BACKGROUND);
        pnlbuttons.add(btnpay);
        pnlbuttons.add(btnreceipt);
        pnlbuttons.add(btnhistory);
        pnlbuttons.add(btndelete);

        txtsearch.addActionListener(this);
        cbofilter.addActionListener(this);
        btnsearch.addActionListener(this);
        btnshowall.addActionListener(this);
        btnpay.addActionListener(this);
        btnreceipt.addActionListener(this);
        btnhistory.addActionListener(this);
        btndelete.addActionListener(this);

        txtsearch.setToolTipText("Type a reservation number, guest, room, status or pay status");
        cbofilter.setToolTipText("Show only the bills with this status");
        btnpay.setToolTipText("Receive a cash payment for the selected bill");
        btnhistory.setToolTipText("Show the payments of the selected bill");
        btndelete.setEnabled(isAdmin());

        UI.styleTable(table);
        int[] widths = {75, 140, 55, 55, 95, 100, 100, 100, 80, 95};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JPanel pnlsearchrows = new JPanel(new BorderLayout(0, 6));
        pnlsearchrows.setBackground(Colors.BACKGROUND);
        pnlsearchrows.add(pnlsearch, BorderLayout.NORTH);
        pnlsearchrows.add(pnlbuttons, BorderLayout.CENTER);

        JPanel pnltop = new JPanel(new BorderLayout());
        pnltop.setBackground(Colors.BACKGROUND);
        pnltop.add(pnlcardswrap, BorderLayout.NORTH);
        pnltop.add(pnlsearchrows, BorderLayout.CENTER);

        JPanel pnlcenter = new JPanel(new BorderLayout(0, 10));
        pnlcenter.setBackground(Colors.BACKGROUND);
        pnlcenter.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        pnlcenter.add(pnltop, BorderLayout.NORTH);
        pnlcenter.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(pnlcenter, BorderLayout.CENTER);

        refresh();
    }

    private JPanel createCard(String title, JLabel lblamount) {
        JLabel lbltitle = new JLabel(title, SwingConstants.CENTER);
        lbltitle.setForeground(Color.GRAY);
        lblamount.setFont(new Font("Dialog", Font.BOLD, 20));
        lblamount.setForeground(Colors.NAVY);
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Colors.GOLD, 2));
        card.add(lbltitle);
        card.add(lblamount);
        return card;
    }

    @Override
    public void refresh() {
        loadTable();
    }

    private void loadTable() {
        String keyword = txtsearch.getText().trim().toLowerCase();
        String filter = cbofilter.getSelectedItem().toString();

        String keep = null;
        int selected = table.getSelectedRow();
        if (selected >= 0 && selected < model.getRowCount()) {
            keep = model.getValueAt(selected, 0).toString();
        }
        model.setRowCount(0);

        ArrayList<Reservation> list = Reservation_Store.getAllReservations();
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            String payStatus = Payment_Store.getPayStatus(r);
            String allText = (r.getReservationId() + " " + r.getGuestName() + " " + r.getRoomNumber()
                    + " " + r.getStatus() + " " + payStatus).toLowerCase();

            boolean matchesSearch = keyword.isEmpty() || allText.contains(keyword);
            boolean matchesFilter = filter.equals("All") || r.getStatus().equals(filter);
            if (matchesSearch && matchesFilter) {
                model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getGuestName(),
                    r.getRoomNumber(),
                    r.getNights(),
                    Validator.peso(r.getRate()),
                    Validator.peso(r.getTotal()),
                    Validator.peso(Payment_Store.getTotalPaid(r.getReservationId())),
                    Validator.peso(Payment_Store.getBalance(r)),
                    payStatus,
                    r.getStatus()
                });
            }
        }
        lblcount.setText("Showing " + model.getRowCount() + " of " + list.size() + " bills");
        updateTotals();
        if (keep != null) {
            selectRow(keep);
        }
    }

    private void updateTotals() {
        long billed = 0;
        long paid = 0;
        long due = 0;
        ArrayList<Reservation> list = Reservation_Store.getAllReservations();
        for (int i = 0; i < list.size(); i++) {
            Reservation r = list.get(i);
            if (r.getStatus().equals("Cancelled")) {
                continue;
            }
            billed += Payment_Store.cents(r.getTotal());
            paid += Math.min(Payment_Store.getPaidCents(r.getReservationId()), Payment_Store.cents(r.getTotal()));
            due += Payment_Store.getBalanceCents(r);
        }
        lblbilled.setText(Validator.peso(billed / 100.0));
        lblpaid.setText(Validator.peso(paid / 100.0));
        lbldue.setText(Validator.peso(due / 100.0));
    }

    private void selectRow(String reservationId) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(reservationId)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnsearch || source == txtsearch || source == cbofilter) {
            loadTable();
        } else if (source == btnshowall) {
            txtsearch.setText("");
            cbofilter.setSelectedIndex(0);
            loadTable();
        } else if (source == btnpay) {
            payBill();
        } else if (source == btnreceipt) {
            viewReceipt();
        } else if (source == btnhistory) {
            showHistory();
        } else if (source == btndelete) {
            deleteBill();
        }
    }

    private Reservation getSelectedReservation(String message) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, message, "No selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Reservation r = Reservation_Store.findReservation(model.getValueAt(row, 0).toString());
        if (r == null) {
            refresh();
        }
        return r;
    }

    private void deleteBill() {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(this, "Only the Admin can delete bills.", "Not allowed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Reservation r = getSelectedReservation("Please select a bill in the table first.");
        if (r == null) {
            return;
        }
        if (r.isOpen()) {
            JOptionPane.showMessageDialog(this, "Only a Checked Out or Cancelled bill can be deleted. This one is "
                    + r.getStatus() + ".", "Not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete the bill of reservation " + r.getReservationId() + " (" + r.getGuestName() + ", "
                + Validator.peso(r.getTotal()) + ")?\nThe reservation record and its payments are deleted too. "
                + "This cannot be undone.",
                "Delete Bill", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        String deleted = r.getReservationId();
        if (!Reservation_Store.deleteReservation(deleted)) {
            JOptionPane.showMessageDialog(this, "The bill could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refresh();
        showInfo("Bill " + deleted + " was deleted.");
    }

    private void payBill() {
        Reservation r = getSelectedReservation("Please select a bill in the table first, then press Pay Bill.");
        if (r == null) {
            return;
        }
        if (r.getStatus().equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "A cancelled reservation cannot be paid.",
                    "Not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (Payment_Store.getBalanceCents(r) == 0) {
            JOptionPane.showMessageDialog(this, "Bill " + r.getReservationId() + " is already fully paid.",
                    "Already paid", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        showPayDialog(r);
    }

    private JLabel addInfoRow(JPanel pnl, String label, String value, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 130, 25);
        pnl.add(lbl);
        JLabel val = new JLabel(value);
        val.setBounds(160, y, 320, 25);
        pnl.add(val);
        return val;
    }

    private void showPayDialog(final Reservation r) {
        final String resId = r.getReservationId();
        final JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Pay Bill - Reservation " + resId, true);

        JPanel pnl = new JPanel(null);
        pnl.setBackground(Color.WHITE);
        pnl.setPreferredSize(new Dimension(500, 415));

        addInfoRow(pnl, "Guest:", r.getGuestName(), 15).setFont(new Font("Dialog", Font.BOLD, 13));
        addInfoRow(pnl, "Room:", r.getRoomNumber() + " (" + r.getRoomType() + ")", 43);
        addInfoRow(pnl, "Total bill:", Validator.peso(r.getTotal()), 71);
        addInfoRow(pnl, "Already paid:", Validator.peso(Payment_Store.getTotalPaid(resId)), 99);
        JLabel lblbalance = addInfoRow(pnl, "Balance due:", Validator.peso(Payment_Store.getBalance(r)), 127);
        lblbalance.setFont(new Font("Dialog", Font.BOLD, 15));
        lblbalance.setForeground(new Color(190, 30, 30));

        JPanel line = new JPanel();
        line.setBackground(Color.LIGHT_GRAY);
        line.setBounds(20, 163, 460, 1);
        pnl.add(line);

        JLabel lblmethodtitle = new JLabel("Payment method:");
        lblmethodtitle.setBounds(20, 178, 130, 28);
        pnl.add(lblmethodtitle);
        JLabel lblmethod = new JLabel(" " + Payment_Store.METHOD_CASH );
        lblmethod.setBounds(160, 178, 190, 28);
        lblmethod.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnl.add(lblmethod);

        JLabel lblamounttitle = new JLabel("Amount paid:");
        lblamounttitle.setBounds(20, 220, 130, 28);
        pnl.add(lblamounttitle);
        final JTextField txtamount = new JTextField(String.format("%.2f", Payment_Store.suggestedPayment(r)));
        txtamount.setBounds(160, 220, 190, 28);
        pnl.add(txtamount);

        JLabel lblcashtitle = new JLabel("Cash given:");
        lblcashtitle.setBounds(20, 262, 130, 28);
        pnl.add(lblcashtitle);
        final JTextField txtcash = new JTextField();
        txtcash.setBounds(160, 262, 190, 28);
        pnl.add(txtcash);
        final JLabel lblchange = new JLabel("Change: -");
        lblchange.setFont(new Font("Dialog", Font.BOLD, 13));
        lblchange.setBounds(362, 262, 130, 28);
        pnl.add(lblchange);

        addInfoRow(pnl, "Received by:", currentUser.getUsername(), 304);

        // Update the change amount as the cash amount is entered.
        DocListener changeUpdater = new DocListener() {
            @Override
            void changed() {
                double amount = Validator.parsePayment(txtamount.getText());
                double cash = Validator.parsePayment(txtcash.getText());
                if (amount > 0 && cash > 0 && Payment_Store.cents(cash) >= Payment_Store.cents(amount)) {
                    lblchange.setText("Change: " + Validator.peso(
                            (Payment_Store.cents(cash) - Payment_Store.cents(amount)) / 100.0));
                    lblchange.setForeground(new Color(0, 128, 60));
                } else {
                    lblchange.setText("Change: -");
                    lblchange.setForeground(Color.GRAY);
                }
            }
        };
        txtamount.getDocument().addDocumentListener(changeUpdater);
        txtcash.getDocument().addDocumentListener(changeUpdater);

        JLabel lblnote = new JLabel(Payment_Store.getPaidCents(resId) == 0
                ? "Down payment (50%) is filled in. Cannot be more than the balance."
                : "Amount cannot be more than the balance.");
        lblnote.setFont(new Font("Dialog", Font.ITALIC, 11));
        lblnote.setForeground(Color.GRAY);
        lblnote.setBounds(20, 330, 460, 20);
        pnl.add(lblnote);

        JButton btnconfirm = new JButton("Confirm Payment");
        btnconfirm.setBounds(245, 362, 135, 32);
        pnl.add(btnconfirm);
        JButton btncancel = new JButton("Cancel");
        btncancel.setBounds(388, 362, 92, 32);
        pnl.add(btncancel);

        btnconfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (confirmPayment(dialog, resId, txtamount.getText(), txtcash.getText())) {
                    dialog.dispose();
                }
            }
        });
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.getRootPane().setDefaultButton(btnconfirm);
        dialog.add(pnl);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        txtamount.selectAll();
        dialog.setVisible(true);
    }

    private boolean confirmPayment(JDialog dialog, String resId, String amountText, String cashText) {
        // Check the reservation again before saving the payment.
        Reservation r = Reservation_Store.findReservation(resId);
        if (r == null) {
            JOptionPane.showMessageDialog(dialog, "This reservation does not exist anymore.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            refresh();
            return true;
        }
        if (r.getStatus().equals("Cancelled")) {
            JOptionPane.showMessageDialog(dialog, "A cancelled reservation cannot be paid.", "Not allowed",
                    JOptionPane.ERROR_MESSAGE);
            refresh();
            return true;
        }
        long balance = Payment_Store.getBalanceCents(r);
        if (balance == 0) {
            JOptionPane.showMessageDialog(dialog, "This bill is already fully paid.", "Already paid",
                    JOptionPane.INFORMATION_MESSAGE);
            refresh();
            return true;
        }

        double amount = Validator.parsePayment(amountText);
        if (amount < 0) {
            JOptionPane.showMessageDialog(dialog, "Please type a valid amount paid (numbers only, up to 2 decimals).",
                    "Invalid amount", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (Payment_Store.cents(amount) > balance) {
            JOptionPane.showMessageDialog(dialog, "The amount cannot be more than the balance ("
                    + Validator.peso(balance / 100.0) + ").", "Invalid amount", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double cash = Validator.parsePayment(cashText);
        if (cash < 0) {
            JOptionPane.showMessageDialog(dialog, "Please type the cash given by the guest (numbers only).",
                    "Invalid cash", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (Payment_Store.cents(cash) < Payment_Store.cents(amount)) {
            JOptionPane.showMessageDialog(dialog, "The cash given is less than the amount paid.",
                    "Invalid cash", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Payment payment = new Payment(Payment_Store.generatePaymentId(resId), resId, Payment_Store.METHOD_CASH,
                amount, LocalDate.now(), currentUser.getUsername());
        if (!Payment_Store.addPayment(payment)) {
            JOptionPane.showMessageDialog(dialog, "The payment could not be saved.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        refresh();
        selectRow(resId);
        dialog.dispose();
        double change = (Payment_Store.cents(cash) - Payment_Store.cents(amount)) / 100.0;
        if (change > 0) {
            showInfo("Payment received. Change to give: " + Validator.peso(change));
        }
        showTextDialog("Official Receipt - " + payment.getPaymentId(), buildPaymentReceipt(payment, r));
        return true;
    }

    private abstract class DocListener implements DocumentListener {

        abstract void changed();

        @Override
        public void insertUpdate(DocumentEvent e) {
            changed();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changed();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            changed();
        }
    }

    private void showHistory() {
        final Reservation r = getSelectedReservation("Please select a bill in the table first.");
        if (r == null) {
            return;
        }
        final ArrayList<Payment> list = Payment_Store.getPaymentsOf(r.getReservationId());
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reservation " + r.getReservationId() + " has no payments yet.",
                    "Payment History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] cols = {"Receipt No.", "Date", "Method", "Amount", "Received by"};
        DefaultTableModel hmodel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < list.size(); i++) {
            Payment p = list.get(i);
            hmodel.addRow(new Object[]{p.getPaymentId(), p.getPaymentDate(), p.getMethod(),
                Validator.peso(p.getAmount()), p.getReceivedBy()});
        }
        final JTable htable = new JTable(hmodel);
        UI.styleTable(htable);

        final JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Payment History - Reservation " + r.getReservationId() + " (" + r.getGuestName() + ")", true);
        dialog.setLayout(new BorderLayout(0, 8));
        JScrollPane scroll = new JScrollPane(htable);
        scroll.setPreferredSize(new Dimension(560, 180));
        dialog.add(scroll, BorderLayout.CENTER);

        JLabel lblsummary = new JLabel("  Total bill " + Validator.peso(r.getTotal()) + "   |   Paid "
                + Validator.peso(Payment_Store.getTotalPaid(r.getReservationId())) + "   |   Balance "
                + Validator.peso(Payment_Store.getBalance(r)));
        dialog.add(lblsummary, BorderLayout.NORTH);

        JButton btnview = new JButton("View Receipt");
        JButton btnclose = new JButton("Close");
        btnview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = htable.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(dialog, "Please select a payment first.",
                            "No selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Payment p = list.get(row);
                showTextDialog("Official Receipt - " + p.getPaymentId(), buildPaymentReceipt(p, r));
            }
        });
        btnclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JPanel pnlbuttons = new JPanel();
        pnlbuttons.add(btnview);
        pnlbuttons.add(btnclose);
        dialog.add(pnlbuttons, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void viewReceipt() {
        Reservation r = getSelectedReservation("Please select a reservation in the table first.");
        if (r == null) {
            return;
        }
        if (r.getStatus().equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "A cancelled reservation has no bill.",
                    "No receipt", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        showTextDialog("Receipt - " + r.getReservationId(), buildReceipt(r));
    }

    private String row(String label, String value) {
        return String.format("%-15s: %s\n", label, value);
    }

    private String buildPaymentReceipt(Payment p, Reservation r) {
        String dash = "----------------------------------------\n";

        long before = 0;
        ArrayList<Payment> list = Payment_Store.getPaymentsOf(r.getReservationId());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == p || list.get(i).getPaymentId().equals(p.getPaymentId())) {
                break;
            }
            before += Payment_Store.cents(list.get(i).getAmount());
        }
        long total = Payment_Store.cents(r.getTotal());
        long balance = Math.max(0, total - before - Payment_Store.cents(p.getAmount()));

        String text = "            VACANZA HOTEL\n";
        text += "           Official Receipt\n";
        text += dash;
        text += row("Receipt No.", p.getPaymentId());
        text += row("Date", p.getPaymentDate().toString());
        text += row("Reservation", r.getReservationId());
        text += row("Guest", r.getGuestName());
        text += row("Room", r.getRoomNumber() + " (" + r.getRoomType() + ")");
        text += row("Nights", r.getNights() + " x " + Validator.peso(r.getRate()));
        text += row("Discount", Validator.peso(r.getSubtotal() - r.getTotal()));
        text += row("TOTAL BILL", Validator.peso(r.getTotal()));
        text += dash;
        text += row("PAYMENT", p.getMethod());
        text += row("AMOUNT PAID", Validator.peso(p.getAmount()));
        text += row("PAID BEFORE", Validator.peso(before / 100.0));
        text += row("BALANCE", Validator.peso(balance / 100.0));
        text += row("STATUS", balance == 0 ? "PAID" : "PARTIAL");
        text += row("Received by", p.getReceivedBy());
        return text;
    }

    private String buildReceipt(Reservation r) {
        String line = "==========================================\n";
        String dash = "------------------------------------------\n";
        boolean fullyPaid = Payment_Store.getBalanceCents(r) == 0;
        String text = line;
        text += "              V A C A N Z A\n";
        text += "   Hotel Room Reservation Management\n";
        text += line;
        // Use an official receipt only when the bill is fully paid.
        if (fullyPaid) {
            text += "OFFICIAL RECEIPT\n";
        } else {
            text += "STATEMENT OF ACCOUNT (not yet fully paid)\n";
        }
        text += "Date         : " + LocalDate.now() + "\n";
        text += "Prepared by  : " + currentUser.getUsername() + "\n";
        text += dash;
        text += "Reservation  : " + r.getReservationId() + "\n";
        text += "Guest        : " + r.getGuestName() + " (" + r.getGuestId() + ")\n";
        text += "Room         : " + r.getRoomNumber() + " - " + r.getRoomType() + "\n";

        // Check if the room still exists before showing its details.
        Room room = Room_Store.findRoom(r.getRoomNumber());
        if (room != null) {
            text += "Amenities    : " + room.getAmenities() + "\n";
        }
        text += "Check-in     : " + r.getCheckIn() + "\n";
        text += "Check-out    : " + r.getCheckOut() + "\n";
        text += dash;
        text += "Rate / night : " + Validator.peso(r.getRate()) + "\n";
        text += "Nights       : " + r.getNights() + "\n";
        text += dash;
        if (r.getDiscount() > 0) {
            text += "Subtotal     : " + Validator.peso(r.getSubtotal()) + "\n";
            text += "Returning " + Math.round(r.getDiscount() * 100) + "% : -"
                    + Validator.peso(r.getSubtotal() - r.getTotal()) + "\n";
        }
        text += "TOTAL AMOUNT : " + Validator.peso(r.getTotal()) + "\n";
        text += dash;

        ArrayList<Payment> payments = Payment_Store.getPaymentsOf(r.getReservationId());
        if (payments.isEmpty()) {
            text += "Payments     : none yet\n";
        } else {
            text += "PAYMENTS (cash)\n";
            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                text += p.getPaymentId() + "  " + p.getPaymentDate() + "  " + Validator.peso(p.getAmount()) + "\n";
            }
        }
        text += "AMOUNT PAID  : " + Validator.peso(Payment_Store.getTotalPaid(r.getReservationId())) + "\n";
        text += "BALANCE      : " + Validator.peso(Payment_Store.getBalance(r)) + "\n";
        text += "PAY STATUS   : " + Payment_Store.getPayStatus(r).toUpperCase() + "\n";
        text += line;
        text += "      Thank you for staying with us!\n";
        return text;
    }

    private void showTextDialog(String title, String text) {
        final JTextArea txtreceipt = new JTextArea(text);
        txtreceipt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtreceipt.setEditable(false);
        txtreceipt.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        final JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(txtreceipt), BorderLayout.CENTER);

        JButton btnclose = new JButton("Close");
        btnclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        JPanel pnlbuttons = new JPanel();
        pnlbuttons.add(btnclose);
        dialog.add(pnlbuttons, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
