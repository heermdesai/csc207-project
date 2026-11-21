package use_case.add_expense;

import java.util.List;

/**
 * What the user entered on the Add Expense screen.
 */
public class AddExpenseInputData {
    private final String groupId;
    private final String description;
    private final String amount;
    private final String payerEmail;
    private final List<String> participantEmails;

    public AddExpenseInputData(String groupId, String description, String amount,
                               String payerEmail, List<String> participantEmails) {
        this.groupId = groupId;
        this.description = description;
        this.amount = amount;
        this.payerEmail = payerEmail;
        this.participantEmails = participantEmails;
    }

    String getGroupId() {
        return groupId;
    }

    String getDescription() {
        return description;
    }

    String getAmount() {
        return amount;
    }

    String getPayerEmail() {
        return payerEmail;
    }

    List<String> getParticipantEmails() {
        return participantEmails;
    }
}
