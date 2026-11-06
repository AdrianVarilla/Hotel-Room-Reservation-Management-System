package vacanza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Guest_Store {

    private static ArrayList<Guest> guestList = new ArrayList<>();
    private static final String FILE_NAME = "guests.txt";

    public static String generateGuestId() {
        String prefix = "10-";
        int highest = 0;
        for (Guest g : guestList) {
            String id = g.getGuestId();
            if (id.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(id.substring(prefix.length()));
                    if (number > highest) {
                        highest = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return prefix + String.format("%03d", highest + 1);
    }

    public static boolean addGuest(Guest guest) {
        if (guest == null || findGuest(guest.getGuestId()) != null) {
            return false;
        }
        guestList.add(guest);
        return saveToFile();
    }

    public static Guest findGuest(String guestId) {
        if (guestId == null) {
            return null;
        }
        for (Guest g : guestList) {
            if (g.getGuestId().equals(guestId)) {
                return g;
            }
        }
        return null;
    }

    // Check the name and contact number to avoid duplicate guests.
    public static boolean isDuplicate(String fullName, String contactNumber, String exceptGuestId) {
        for (Guest g : guestList) {
            boolean sameGuest = exceptGuestId != null && g.getGuestId().equals(exceptGuestId);
            if (!sameGuest && g.getFullName().equalsIgnoreCase(Validator.clean(fullName))
                    && g.getContactNumber().equals(Validator.clean(contactNumber))) {
                return true;
            }
        }
        return false;
    }

    public static boolean updateGuest(Guest updatedGuest) {
        if (updatedGuest == null) {
            return false;
        }
        for (int i = 0; i < guestList.size(); i++) {
            if (guestList.get(i).getGuestId().equals(updatedGuest.getGuestId())) {
                guestList.set(i, updatedGuest);
                return saveToFile();
            }
        }
        return false;
    }

    public static boolean deleteGuest(String guestId) {
        for (int i = 0; i < guestList.size(); i++) {
            if (guestList.get(i).getGuestId().equals(guestId)) {
                guestList.remove(i);
                return saveToFile();
            }
        }
        return false;
    }

    public static ArrayList<Guest> getAllGuests() {
        return guestList;
    }

    // We separate the saved fields with | so addresses can include commas.
    public static boolean saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (Guest g : guestList) {
                writer.println(g.getGuestId() + "|" + g.getFullName() + "|" + g.getAddress()
                        + "|" + g.getContactNumber() + "|" + g.getEmail());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving guests.txt: " + e.getMessage());
            return false;
        }
    }

    public static void loadFromFile() {
        guestList.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] p = line.split("\\|", -1);

                if (p.length == 5) {
                    guestList.add(new Guest(p[0], p[1], p[2], p[3], p[4]));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading guests.txt: " + e.getMessage());
        }
    }
}
