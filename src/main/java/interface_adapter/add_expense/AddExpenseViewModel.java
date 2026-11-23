package interface_adapter.add_expense;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Expense View. It never calls the controller:
 * View -> Controller -> Interactor -> Presenter -> ViewModel -> View.
 */
public class AddExpenseViewModel extends ViewModel<AddExpenseState> {
    public static final String VIEW_NAME = "add expense";

    public AddExpenseViewModel() {
        super(VIEW_NAME);
        setState(new AddExpenseState());
    }
}
