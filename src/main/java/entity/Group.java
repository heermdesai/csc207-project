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
        this.creatorEmail = creatorEmail;
        this.memberEmails = new ArrayList<>();
        this.memberEmails.add(creatorEmail);
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
    public List<String> getMemberEmails(){
        List<String> memberlist = new ArrayList<>();
        for (String each : this.memberEmails){
            memberlist.add(each);
        }
        return memberlist;
    }

    public boolean isMember(String email) {
        return this.getMemberEmails().contains(email);
    }
    public void addMember(String email) {
        if (!isMember(email)) {
            this.memberEmails.add(email);
        }

    }
}