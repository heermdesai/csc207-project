package interface_adapter.create_group;

import use_case.create_group.CreateGroupOutputBoundary;
import use_case.create_group.CreateGroupOutputData;

/**
 * Turns the result of Create Group into what the screen shows.
 */
public class CreateGroupPresenter implements CreateGroupOutputBoundary {
    private final CreateGroupViewModel createGroupViewModel;

    public CreateGroupPresenter(CreateGroupViewModel createGroupViewModel) {
        this.createGroupViewModel = createGroupViewModel;
    }

    @Override
    public void prepareSuccessView(CreateGroupOutputData outputData) {
        CreateGroupState state = createGroupViewModel.getState();
        state.setGroupName(outputData.getGroupName());
        state.setJoinCode(outputData.getJoinCode());
        state.setErrorMessage(null);
        createGroupViewModel.setState(state);
        createGroupViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        CreateGroupState state = createGroupViewModel.getState();
        state.setJoinCode("");
        state.setErrorMessage(errorMessage);
        createGroupViewModel.setState(state);
        createGroupViewModel.firePropertyChanged();
    }
}