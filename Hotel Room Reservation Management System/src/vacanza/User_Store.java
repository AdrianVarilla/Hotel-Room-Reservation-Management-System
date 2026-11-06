package vacanza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class User_Store {

    private static ArrayList<User> userList = new ArrayList<>();
    private static final String FILE_NAME = "users.txt";

    public static boolean usernameExists(String username) {
        return findUser(username) != null;
    }

    public static boolean addUser(User user) {
        if (user == null || usernameExists(user.getUsername())) {
            return false;
        }
        userList.add(user);
        return saveToFile();
    }

    public static User findUser(String username) {
        if (username == null) {
            return null;
        }
        for (User u : userList) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                return u;
            }
        }
        return null;
    }

    public static boolean updateUser(User updatedUser) {
        if (updatedUser == null) {
            return false;
        }
        User old = findUser(updatedUser.getUsername());
        if (old == null) {
            return false;
        }
        old.setPassword(updatedUser.getPassword());
        old.setRole(updatedUser.getRole());
        old.setLocked(updatedUser.isLocked());
        return saveToFile();
    }

    public static boolean deleteUser(String username) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equalsIgnoreCase(username)) {
                userList.remove(i);
                return saveToFile();
            }
        }
        return false;
    }

    public static int countAdmins() {
        int count = 0;
        for (User u : userList) {
            if (u.getRole().equalsIgnoreCase("Admin")) {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<User> getAllUsers() {
        return userList;
    }

    public static boolean saveToFile() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (User u : userList) {
                writer.println(u.getUsername() + "," + u.getPassword() + "," + u.getRole() + "," + u.isLocked());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving users.txt: " + e.getMessage());
            return false;
        }
    }

    public static void loadFromFile() {
        userList.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 4) {
                        userList.add(new User(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3])));
                    }
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("Error loading users.txt: " + e.getMessage());
            }
        }

        // Create the default admin account if no admin was loaded.
        boolean hasAdmin = false;
        for (User u : userList) {
            if (u.getRole().equalsIgnoreCase("Admin")) {
                hasAdmin = true;
            }
        }
        if (!hasAdmin) {
            userList.add(new User("admin", "admin123", "Admin", false));
            saveToFile();
        }
    }
}
