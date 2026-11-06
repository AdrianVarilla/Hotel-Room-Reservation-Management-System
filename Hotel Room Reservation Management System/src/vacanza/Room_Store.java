package vacanza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Room_Store {

    private static ArrayList<Room> roomList = new ArrayList<>();
    private static final String FILE_NAME = "rooms.txt";
    public static final int ROOMS_PER_FLOOR = 10;
    public static final int FLOORS = 5;

    public static Room createRoom(String type, String roomNumber, int floor, String status, double rate) {
        if (type.equals("Deluxe")) {
            return new DeluxeRoom(roomNumber, floor, status, rate);
        } else if (type.equals("Suite")) {
            return new SuiteRoom(roomNumber, floor, status, rate);
        } else {
            return new StandardRoom(roomNumber, floor, status, rate);
        }
    }

    public static Room createSample(String type) {
        return createRoom(type, "000", 1, "Available", 1);
    }

    // Use the first unused room number on this floor.
    public static String generateRoomNumber(int floor) {
        if (floor < 1 || floor > FLOORS) {
            return "";
        }
        for (int i = 1; i <= ROOMS_PER_FLOOR; i++) {
            String number = "" + (floor * 100 + i);
            if (findRoom(number) == null) {
                return number;
            }
        }
        return "";
    }

    public static boolean addRoom(Room room) {
        roomList.add(room);
        return saveToFile();
    }

    public static Room findRoom(String roomNumber) {
        for (Room r : roomList) {
            if (r.getRoomNumber().equals(roomNumber)) {
                return r;
            }
        }
        return null;
    }

    public static boolean updateRoom(Room updatedRoom) {
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNumber().equals(updatedRoom.getRoomNumber())) {
                roomList.set(i, updatedRoom);
                return saveToFile();
            }
        }
        return false;
    }

    public static boolean setRoomStatus(String roomNumber, String status) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            return false;
        }
        room.setStatus(status);
        return saveToFile();
    }

    public static boolean deleteRoom(String roomNumber) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            return false;
        }
        roomList.remove(room);
        return saveToFile();
    }

    public static ArrayList<Room> getAllRooms() {
        return roomList;
    }

    public static ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> available = new ArrayList<>();
        for (Room r : roomList) {
            if (r.getStatus().equals("Available")) {
                available.add(r);
            }
        }
        return available;
    }

    public static boolean saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (Room r : roomList) {
                writer.println(r.getRoomNumber() + "|" + r.getRoomType() + "|" + r.getFloor()
                        + "|" + r.getStatus() + "|" + r.getRate());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving rooms.txt: " + e.getMessage());
            return false;
        }
    }

    public static void loadFromFile() {
        roomList.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] p = line.split("\\|");

                if (p.length == 5) {
                    try {
                        roomList.add(createRoom(p[1], p[0], Integer.parseInt(p[2]), p[3], Double.parseDouble(p[4])));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipped a damaged line in rooms.txt: " + line);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading rooms.txt: " + e.getMessage());
        }
    }
}
