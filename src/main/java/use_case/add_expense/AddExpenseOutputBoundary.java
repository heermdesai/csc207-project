package use_case.add_expense;

/**
 * What the interactor calls when it's done. The presenter implements this.
 */
public interface AddExpenseOutputBoundary {
    void prepareSuccessView(AddExpenseOutputData outputData);

    void prepareFailView(String errorMessage);
}
