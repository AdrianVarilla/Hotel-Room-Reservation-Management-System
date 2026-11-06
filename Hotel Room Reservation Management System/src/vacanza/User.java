package vacanza;

public class User {

    private String username;
    private String password;
    private String role;
    private boolean locked;

    public User(String username, String password, String role, boolean locked) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = locked;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
