package entity;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final String id;
    private final String name;
    private final String joinCode;
    private final String baseCurrency;
    private final String creatorEmail;
    private final List<String> memberEmails;

    public Group(String id, String name, String joinCode,
                 String baseCurrency, String creatorEmail) {
        this.id = id;
        this.name = name;
        this.joinCode = joinCode;
        this.baseCurrency = baseCurrency;
        this.creatorEmail = normalize(creatorEmail);
        this.memberEmails = new ArrayList<>();
        this.memberEmails.add(this.creatorEmail);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getJoinCode() {
        return this.joinCode;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public String getCreatorEmail() {
        return this.creatorEmail;
    }

    public List<String> getMemberEmails() {
        return new ArrayList<>(this.memberEmails);
    }

    public boolean isMember(String email) {
        return this.memberEmails.contains(normalize(email));
    }

    public void addMember(String email) {
        if (!isMember(email)) {
            this.memberEmails.add(normalize(email));
        }
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }
}