package vacanza;

import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class BasePanel extends JPanel implements ActionListener {

    protected User currentUser;

    public BasePanel(User user) {
        currentUser = user;
    }

    public abstract void refresh();

    // Keep the common screen helpers here so we can reuse them.
    protected boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equalsIgnoreCase("Admin");
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
