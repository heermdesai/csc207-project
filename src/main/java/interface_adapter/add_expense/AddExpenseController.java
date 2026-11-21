package interface_adapter.add_expense;

import use_case.add_expense.AddExpenseInputBoundary;
import use_case.add_expense.AddExpenseInputData;

import java.util.List;

/**
 * Takes what the view collected and starts the Add Expense use case.
 */
public class AddExpenseController {
    private final AddExpenseInputBoundary addExpenseInteractor;

    public AddExpenseController(AddExpenseInputBoundary addExpenseInteractor) {
        this.addExpenseInteractor = addExpenseInteractor;
    }

    public void execute(String groupId, String description, String amount,
                        String payerEmail, List<String> participantEmails) {
        AddExpenseInputData inputData = new AddExpenseInputData(
                groupId, description, amount, payerEmail, participantEmails);
        addExpenseInteractor.execute(inputData);
    }
}
