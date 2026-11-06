package vacanza;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class AdminCheck {

    // Ask for the admin password before allowing the delete.
    public static boolean confirm(Component parent, User user, String action) {
        if (user == null || !user.getRole().equalsIgnoreCase("Admin")) {
            JOptionPane.showMessageDialog(parent, "Only the Admin can do this.",
                    "Not allowed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JPasswordField txtadminpass = new JPasswordField();
        int choice = JOptionPane.showConfirmDialog(parent, txtadminpass,
                "Enter the Admin password to " + action, JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return false;
        }

        String typed = new String(txtadminpass.getPassword());
        if (!typed.equals(user.getPassword())) {
            JOptionPane.showMessageDialog(parent, "Wrong Admin password. Nothing was deleted.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
