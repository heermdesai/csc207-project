package interface_adapter.create_group;

import use_case.create_group.CreateGroupInputBoundary;
import use_case.create_group.CreateGroupInputData;

/**
 * Takes what the view collected and starts the Create Group use case.
 */
public class CreateGroupController {
    private final CreateGroupInputBoundary createGroupInteractor;

    public CreateGroupController(CreateGroupInputBoundary createGroupInteractor) {
        this.createGroupInteractor = createGroupInteractor;
    }

    public void execute(String groupName, String creatorEmail, String baseCurrency) {
        CreateGroupInputData inputData = new CreateGroupInputData(groupName, creatorEmail, baseCurrency);
        createGroupInteractor.execute(inputData);
    }
}
