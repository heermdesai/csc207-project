package use_case.create_group;

public class CreateGroupInputData {
    private final String groupName;
    private final String creatorEmail;
    private final String baseCurrency;

    public CreateGroupInputData(String groupName, String creatorEmail, String baseCurrency) {
        this.groupName = groupName;
        this.creatorEmail = creatorEmail;
        this.baseCurrency = baseCurrency;
    }
    public String getGroupName(){
        return this.groupName;
    }
    public String getCreatorEmail(){
        return this.creatorEmail;
    }
    public String getBaseCurrency(){
        return this.baseCurrency;
    }
}

