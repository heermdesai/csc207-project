package use_case.create_group;

/**
 * What the controller calls. The interactor implements this.
 */
public interface CreateGroupInputBoundary {
    void execute(CreateGroupInputData inputData);
}