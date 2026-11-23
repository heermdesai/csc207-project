package interface_adapter.add_expense;

import use_case.add_expense.AddExpenseOutputBoundary;
import use_case.add_expense.AddExpenseOutputData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Turns the result into text for the screen and updates the view model.
 */
public class AddExpensePresenter implements AddExpenseOutputBoundary {
    private final AddExpenseViewModel addExpenseViewModel;

    public AddExpensePresenter(AddExpenseViewModel addExpenseViewModel) {
        this.addExpenseViewModel = addExpenseViewModel;
    }

    @Override
    public void prepareSuccessView(AddExpenseOutputData outputData) {
        // e.g. "Dinner: 60.00 CAD paid by a@mail.com (a@mail.com 30.00, b@mail.com 30.00)"
        List<String> shareTexts = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> share : outputData.getShares().entrySet()) {
            shareTexts.add(share.getKey() + " " + share.getValue());
        }
        String row = outputData.getDescription() + ": " + outputData.getAmount() + " "
                + outputData.getCurrency() + " paid by " + outputData.getPayerEmail()
                + " (" + String.join(", ", shareTexts) + ")";

        AddExpenseState state = addExpenseViewModel.getState();
        state.addExpenseRow(row);
        state.setErrorMessage(null);
        state.setSuccessMessage("Added " + outputData.getDescription() + ".");
        addExpenseViewModel.setState(state);
        addExpenseViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddExpenseState state = addExpenseViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        addExpenseViewModel.setState(state);
        addExpenseViewModel.firePropertyChanged();
    }
}
