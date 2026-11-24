package use_case.create_group;

public class CreateGroupOutputData {
    private final String groupName;
    private final String joinCode;

    public CreateGroupOutputData(String groupName, String joinCode) {
        this.groupName = groupName;
        this.joinCode = joinCode;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getJoinCode() {
        return this.joinCode;
    }
}

