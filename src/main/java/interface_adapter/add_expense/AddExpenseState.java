package interface_adapter.add_expense;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything the Add Expense screen needs to draw itself.
 * groupId and memberEmails are filled in when the user opens a group
 * (by the presenter that shows the group page).
 */
public class AddExpenseState {
    private String groupId = "";
    private List<String> memberEmails = new ArrayList<>();
    private List<String> expenseRows = new ArrayList<>();
    private String errorMessage;
    private String successMessage;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberEmails() {
        return new ArrayList<>(memberEmails);
    }

    public void setMemberEmails(List<String> memberEmails) {
        this.memberEmails = new ArrayList<>(memberEmails);
    }

    public List<String> getExpenseRows() {
        return new ArrayList<>(expenseRows);
    }

    public void setExpenseRows(List<String> expenseRows) {
        this.expenseRows = new ArrayList<>(expenseRows);
    }

    public void addExpenseRow(String row) {
        this.expenseRows.add(row);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
