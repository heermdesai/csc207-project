package use_case.logout;

public class LogoutOutputData {

    private final String email;

    public LogoutOutputData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
