package vacanza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Payment_Store {

    public static final String METHOD_CASH = "Cash";

    private static ArrayList<Payment> paymentList = new ArrayList<>();
    private static final String FILE_NAME = "payments.txt";

    public static long cents(double amount) {
        return Math.round(amount * 100);
    }

    public static String generatePaymentId(String reservationId) {
        int highest = 0;
        String prefix = "RCPT-" + reservationId + "-";
        for (Payment p : paymentList) {
            if (p.getReservationId().equals(reservationId) && p.getPaymentId().startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(p.getPaymentId().substring(prefix.length()));
                    if (number > highest) {
                        highest = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return prefix + (highest + 1);
    }

    public static boolean addPayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        paymentList.add(payment);
        if (!saveToFile()) {
            paymentList.remove(payment);
            return false;
        }
        return true;
    }

    public static ArrayList<Payment> getPaymentsOf(String reservationId) {
        ArrayList<Payment> result = new ArrayList<>();
        for (Payment p : paymentList) {
            if (p.getReservationId().equals(reservationId)) {
                result.add(p);
            }
        }
        return result;
    }

    public static long getPaidCents(String reservationId) {
        long total = 0;
        for (Payment p : paymentList) {
            if (p.getReservationId().equals(reservationId)) {
                total += cents(p.getAmount());
            }
        }
        return total;
    }

    public static double getTotalPaid(String reservationId) {
        return getPaidCents(reservationId) / 100.0;
    }

    public static final double DOWN_PAYMENT_RATE = 0.50;

    public static double suggestedPayment(Reservation r) {
        long balance = getBalanceCents(r);
        if (getPaidCents(r.getReservationId()) == 0) {
            long down = (long) Math.ceil(cents(r.getTotal()) * DOWN_PAYMENT_RATE);
            return Math.min(down, balance) / 100.0;
        }
        return balance / 100.0;
    }

    public static long getBalanceCents(Reservation r) {
        long balance = cents(r.getTotal()) - getPaidCents(r.getReservationId());
        return balance < 0 ? 0 : balance;
    }

    public static double getBalance(Reservation r) {
        return getBalanceCents(r) / 100.0;
    }

    public static String getPayStatus(Reservation r) {
        if (r.getStatus().equals("Cancelled")) {
            return "-";
        }
        if (getBalanceCents(r) == 0) {
            return "Paid";
        }
        if (getPaidCents(r.getReservationId()) == 0) {
            return "Unpaid";
        }
        return "Partial";
    }

    public static boolean deleteForReservation(String reservationId) {
        boolean removedAny = false;
        for (int i = paymentList.size() - 1; i >= 0; i--) {
            if (paymentList.get(i).getReservationId().equals(reservationId)) {
                paymentList.remove(i);
                removedAny = true;
            }
        }
        return !removedAny || saveToFile();
    }

    public static boolean saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (Payment p : paymentList) {
                writer.println(p.getPaymentId() + "|" + p.getReservationId() + "|" + p.getMethod()
                        + "|" + p.getAmount() + "|" + p.getPaymentDate() + "|" + p.getReceivedBy());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving payments.txt: " + e.getMessage());
            return false;
        }
    }

    public static void loadFromFile() {
        paymentList.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] p = line.split("\\|", -1);
                if (p.length == 6) {
                    try {
                        double amount = Double.parseDouble(p[3]);
                        LocalDate date = LocalDate.parse(p[4]);
                        paymentList.add(new Payment(p[0], p[1], p[2], amount, date, p[5].trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipped a damaged line in payments.txt: " + line);
                    } catch (DateTimeParseException e) {
                        System.out.println("Skipped a damaged line in payments.txt: " + line);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading payments.txt: " + e.getMessage());
        }
    }
}
