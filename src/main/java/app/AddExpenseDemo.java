package app;

import data_access.InMemoryExpenseDataAccessObject;
import entity.EqualSplitStrategy;
import entity.Group;
import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.add_expense.AddExpensePresenter;
import interface_adapter.add_expense.AddExpenseState;
import interface_adapter.add_expense.AddExpenseViewModel;
import use_case.add_expense.AddExpenseInteractor;
import view.AddExpenseView;

import javax.swing.JFrame;

/**
 * Temporary: opens just the Add Expense screen with a made-up group,
 * so it can be tried before the group page exists. Delete once it's wired into AppBuilder.
 */
public class AddExpenseDemo {
    public static void main(String[] args) {
        // A made-up group, since Create Group isn't finished yet
        InMemoryExpenseDataAccessObject dataAccessObject = new InMemoryExpenseDataAccessObject();
        Group group = new Group("g1", "Roommates", "ABC123", "CAD", "alice@mail.com");
        group.addMember("bob@mail.com");
        group.addMember("chen@mail.com");
        dataAccessObject.addGroup(group);

        // Build the pieces and connect them (what AppBuilder will do)
        AddExpenseViewModel viewModel = new AddExpenseViewModel();
        AddExpenseView view = new AddExpenseView(viewModel);
        AddExpensePresenter presenter = new AddExpensePresenter(viewModel);
        AddExpenseInteractor interactor = new AddExpenseInteractor(dataAccessObject, presenter,
                new EqualSplitStrategy());
        view.setAddExpenseController(new AddExpenseController(interactor));

        // What the group page will do when someone opens a group
        AddExpenseState state = viewModel.getState();
        state.setGroupId(group.getId());
        state.setMemberEmails(group.getMemberEmails());
        viewModel.setState(state);
        viewModel.firePropertyChanged();

        JFrame frame = new JFrame("Splitter: Add Expense");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setSize(720, 480);
        frame.setVisible(true);
    }
}
