package use_case.add_expense;

/**
 * What the controller calls. The interactor implements this.
 */
public interface AddExpenseInputBoundary {
    void execute(AddExpenseInputData inputData);
}
