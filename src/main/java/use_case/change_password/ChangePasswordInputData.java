package use_case.change_password;

/**
 * The input data for the Change Password Use Case.
 */
public class ChangePasswordInputData {

    private final String password;
    private final String email;

    public ChangePasswordInputData(String password, String email) {
        this.password = password;
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    String getEmail() {
        return email;
    }

}
