package interface_adapter.create_group;

import interface_adapter.ViewModel;

/**
 * The View Model for the Create Group screen.
 */
public class CreateGroupViewModel extends ViewModel<CreateGroupState> {
    public static final String VIEW_NAME = "create group";

    public CreateGroupViewModel() {
        super(VIEW_NAME);
        setState(new CreateGroupState());
    }
}