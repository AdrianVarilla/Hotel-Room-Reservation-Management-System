package vacanza;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Reservation_Store {

    private static ArrayList<Reservation> reservationList = new ArrayList<>();
    private static final String FILE_NAME = "reservations.txt";

    // Use the highest saved reservation number to get the next ID.
    public static String generateReservationId() {
        int highest = 0;
        for (Reservation r : reservationList) {
            try {
                int number = Integer.parseInt(r.getReservationId());
                if (number > highest) {
                    highest = number;
                }
            } catch (NumberFormatException e) {
            }
        }
        return String.format("%03d", highest + 1);
    }

    public static boolean addReservation(Reservation reservation) {
        if (reservation == null || findReservation(reservation.getReservationId()) != null) {
            return false;
        }
        reservationList.add(reservation);
        return saveToFile();
    }

    public static Reservation findReservation(String reservationId) {
        if (reservationId == null) {
            return null;
        }
        for (Reservation r : reservationList) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public static boolean updateStatus(String reservationId, String status) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) {
            return false;
        }
        reservation.setStatus(status);
        return saveToFile();
    }

    public static boolean hasActiveReservation(String guestId) {
        for (Reservation r : reservationList) {
            if (r.getGuestId().equals(guestId) && r.isOpen()) {
                return true;
            }
        }
        return false;
    }

    public static int countCompletedStays(String guestId) {
        int count = 0;
        for (Reservation r : reservationList) {
            if (r.getGuestId().equals(guestId) && r.getStatus().equals("Checked Out")) {
                count++;
            }
        }
        return count;
    }

    // One completed stay is enough to qualify for the returning guest discount.
    public static boolean isReturningGuest(String guestId) {
        return countCompletedStays(guestId) > 0;
    }

    public static int countClosedByGuest(String guestId) {
        int count = 0;
        for (Reservation r : reservationList) {
            if (r.getGuestId().equals(guestId) && !r.isOpen()) {
                count++;
            }
        }
        return count;
    }

    public static boolean deleteReservation(String reservationId) {
        for (int i = 0; i < reservationList.size(); i++) {
            Reservation r = reservationList.get(i);
            if (r.getReservationId().equals(reservationId)) {
                if (r.isOpen()) {
                    return false;
                }
                reservationList.remove(i);
                if (!saveToFile()) {
                    return false;
                }
                // Remove the related payments too, in case this reservation ID is reused.
                return Payment_Store.deleteForReservation(reservationId);
            }
        }
        return false;
    }

    public static int deleteHistoryOfGuest(String guestId) {
        int removed = 0;
        ArrayList<String> removedIds = new ArrayList<>();
        for (int i = reservationList.size() - 1; i >= 0; i--) {
            Reservation r = reservationList.get(i);
            if (r.getGuestId().equals(guestId) && !r.isOpen()) {
                reservationList.remove(i);
                removedIds.add(r.getReservationId());
                removed++;
            }
        }
        if (removed > 0 && !saveToFile()) {
            return -1;
        }
        for (String id : removedIds) {
            if (!Payment_Store.deleteForReservation(id)) {
                return -1;
            }
        }
        return removed;
    }

    // Check for an open booking before making the room available again.
    public static boolean hasOpenReservationForRoom(String roomNumber) {
        for (Reservation r : reservationList) {
            if (r.getRoomNumber().equals(roomNumber) && r.isOpen()) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Reservation> getAllReservations() {
        return reservationList;
    }

    public static boolean saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (Reservation r : reservationList) {
                writer.println(r.getReservationId() + "|" + r.getGuestId() + "|" + r.getGuestName()
                        + "|" + r.getRoomNumber() + "|" + r.getRoomType() + "|" + r.getRate()
                        + "|" + r.getCheckIn() + "|" + r.getNights() + "|" + r.getStatus()
                        + "|" + r.getCreatedBy() + "|" + r.getDiscount());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving reservations.txt: " + e.getMessage());
            return false;
        }
    }

    public static void loadFromFile() {
        reservationList.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] p = line.split("\\|", -1);
                if (p.length == 10 || p.length == 11) {
                    try {
                        double rate = Double.parseDouble(p[5]);
                        LocalDate checkIn = LocalDate.parse(p[6]);
                        int nights = Integer.parseInt(p[7]);
                        if (p.length == 10) {
                            reservationList.add(new Reservation(p[0], p[1], p[2], p[3], p[4],
                                    rate, checkIn, nights, p[8], p[9]));
                        } else {
                            reservationList.add(new Reservation(p[0], p[1], p[2], p[3], p[4],
                                    rate, checkIn, nights, p[8], p[9], Double.parseDouble(p[10])));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipped a damaged line in reservations.txt: " + line);
                    } catch (DateTimeParseException e) {
                        System.out.println("Skipped a damaged line in reservations.txt: " + line);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading reservations.txt: " + e.getMessage());
        }
    }
}
