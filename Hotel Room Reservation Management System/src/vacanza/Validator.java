package vacanza;

public class Validator {

    // Replace | in the input so it does not split a saved field.
    public static String clean(String text) {
        if (text == null) {
            return "";
        }
        text = text.replace("|", "/");
        text = text.replace("\n", " ");
        return text.trim();
    }

    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isValidName(String name) {
        if (name == null || name.length() < 2 || name.length() > 30) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '.' && c != '\'' && c != '-') {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        if (phone.length() != 11) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.contains(" ")) {
            return false;
        }
        int at = email.indexOf("@");
        int dot = email.lastIndexOf(".");
        return at > 0 && dot > at + 1 && dot < email.length() - 1;
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.length() < 3 || username.length() > 20) {
            return false;
        }
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 5 || password.length() > 30) {
            return false;
        }
        return !password.contains(" ") && !password.contains(",") && !password.contains("|");
    }

    public static int parseInt(String text, int min, int max) {
        try {
            int number = Integer.parseInt(text.trim());
            if (number < min || number > max) {
                return -1;
            }
            return number;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double parseAmount(String text) {
        try {
            double amount = Double.parseDouble(text.replace(",", "").trim());
            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0 || amount > 1000000) {
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Allow small payments, but return -1 when the amount is invalid.
    public static double parsePayment(String text) {
        if (text == null) {
            return -1;
        }
        String clean = text.replace(",", "").trim();
        if (!clean.matches("\\d{1,7}(\\.\\d{1,2})?")) {
            return -1;
        }
        double amount = Double.parseDouble(clean);
        if (amount <= 0 || amount > 1000000) {
            return -1;
        }
        return amount;
    }

    public static String peso(double amount) {
        return "₱" + String.format("%,.2f", amount);
    }
}
