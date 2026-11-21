package use_case.add_expense;

import entity.Expense;
import entity.Group;
import entity.SplitStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * The Add Expense use case: check the input, create the expense, save it.
 */
public class AddExpenseInteractor implements AddExpenseInputBoundary {
    private final AddExpenseDataAccessInterface dataAccessObject;
    private final AddExpenseOutputBoundary presenter;
    private final SplitStrategy splitStrategy;

    public AddExpenseInteractor(AddExpenseDataAccessInterface dataAccessObject,
                                AddExpenseOutputBoundary presenter,
                                SplitStrategy splitStrategy) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
        this.splitStrategy = splitStrategy;
    }

    @Override
    public void execute(AddExpenseInputData input) {
        // 1. Find the group
        Group group = dataAccessObject.getGroup(input.getGroupId());
        if (group == null) {
            presenter.prepareFailView("Group not found.");
            return;
        }

        // 2. Check the description
        String description = input.getDescription().trim();
        if (description.isEmpty()) {
            presenter.prepareFailView("Description cannot be empty.");
            return;
        }

        // 3. Check the amount
        String amountText = input.getAmount().trim();
        if (amountText.isEmpty()) {
            presenter.prepareFailView("Amount cannot be empty.");
            return;
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
        }
        catch (NumberFormatException e) {
            presenter.prepareFailView("Amount must be a number.");
            return;
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);   // round to cents
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            presenter.prepareFailView("Amount must be greater than zero.");
            return;
        }

        // 4. Check the payer
        String payerEmail = input.getPayerEmail();
        if (payerEmail == null) {
            presenter.prepareFailView("Please choose who paid.");
            return;
        }
        if (!group.isMember(payerEmail)) {
            presenter.prepareFailView(payerEmail + " is not a member of this group.");
            return;
        }

        // 5. Check the participants
        List<String> participantEmails = input.getParticipantEmails();
        if (participantEmails.isEmpty()) {
            presenter.prepareFailView("Please choose at least one person involved.");
            return;
        }
        for (String email : participantEmails) {
            if (!group.isMember(email)) {
                presenter.prepareFailView(email + " is not a member of this group.");
                return;
            }
        }

        // 6. Create and save the expense
        Expense expense = new Expense(UUID.randomUUID().toString(), group.getId(), description,
                payerEmail, participantEmails, amount, group.getBaseCurrency(),
                LocalDate.now(), splitStrategy);
        dataAccessObject.saveExpense(expense);

        // 7. Tell the presenter
        AddExpenseOutputData outputData = new AddExpenseOutputData(description, amount,
                group.getBaseCurrency(), payerEmail, expense.getShares());
        presenter.prepareSuccessView(outputData);
    }
}
