package use_case.create_group;

/**
 * What the interactor calls when it's done. The presenter implements this.
 */
public interface CreateGroupOutputBoundary {
    void prepareSuccessView(CreateGroupOutputData outputData);

    void prepareFailView(String errorMessage);
}
