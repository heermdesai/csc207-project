package data_access;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 LogoutUserDataAccessInterface {

    private static final String HEADER = "email,password";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) throws IOException {

        csvFile = new File(csvPath);
        headers.put("email", 0);
        headers.put("password", 1);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String email = String.valueOf(col[headers.get("email")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final User user = userFactory.create(email, password);
                    accounts.put(email, user);
                }
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                final String line = String.format("%s,%s",
                        user.getEmail(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getEmail(), user);
        this.save();
    }

    @Override
    public User get(String email) {
        return accounts.get(email);
    }

    @Override
    public boolean existsByEmail(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getEmail(), user);
        save();
    }

    private String currentEmail;

    @Override
    public String getCurrentEmail() {
        return currentEmail;
    }

    @Override
    public void setCurrentEmail(String email) {
        this.currentEmail = email;
    }
}
