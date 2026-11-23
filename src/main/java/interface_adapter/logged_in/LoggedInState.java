package interface_adapter.logged_in;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {

    private String email = "";

    public LoggedInState(LoggedInState copy) {
        email = copy.email;
    }

    public LoggedInState() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
