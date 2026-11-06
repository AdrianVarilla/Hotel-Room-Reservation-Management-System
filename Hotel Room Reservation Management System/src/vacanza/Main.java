package vacanza;

public class Main {

    public static void main(String[] args) {
        // Load our saved records before opening the login screen.
        Room_Store.loadFromFile();
        Guest_Store.loadFromFile();
        Reservation_Store.loadFromFile();
        Payment_Store.loadFromFile();
        User_Store.loadFromFile();

        Login lgnform = new Login();
        lgnform.setVisible(true);
    }
}
