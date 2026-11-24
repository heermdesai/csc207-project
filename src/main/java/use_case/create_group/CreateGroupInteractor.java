package use_case.create_group;

import entity.Group;

import java.util.Random;
import java.util.UUID;

/**
 * The Create Group use case: check the name, make a unique join code, save the group.
 */
public class CreateGroupInteractor implements CreateGroupInputBoundary {
    private static final String CODE_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final String DEFAULT_CURRENCY = "CAD";

    private final CreateGroupDataAccessInterface groupDataAccess;
    private final CreateGroupOutputBoundary presenter;
    private final Random random = new Random();

    public CreateGroupInteractor(CreateGroupDataAccessInterface groupDataAccess,
                                 CreateGroupOutputBoundary presenter) {
        this.groupDataAccess = groupDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CreateGroupInputData inputData) {
        String name = inputData.getGroupName();

        // Reject empty names
        if (name == null || name.trim().isEmpty()) {
            presenter.prepareFailView("Group name cannot be empty.");
            return;
        }

        // Make a join code; if it's taken, make a new one
        String joinCode = this.generateJoinCode();
        while (this.groupDataAccess.existsByJoinCode(joinCode)) {
            joinCode = this.generateJoinCode();
        }

        // Default to CAD if no currency was chosen
        String currency = inputData.getBaseCurrency();
        if (currency == null || currency.trim().isEmpty()) {
            currency = DEFAULT_CURRENCY;
        }

        // Create the group (the creator becomes the first member) and save it
        Group group = new Group(UUID.randomUUID().toString(), name.trim(), joinCode,
                currency.trim(), inputData.getCreatorEmail());
        this.groupDataAccess.save(group);

        // Pass the result to the presenter
        CreateGroupOutputData outputData = new CreateGroupOutputData(name.trim(), joinCode);
        this.presenter.prepareSuccessView(outputData);
    }

    private String generateJoinCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
        }
        return code.toString();
    }
}