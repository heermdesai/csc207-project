package use_case.view_balances;

import entity.BalanceCalculator;
import entity.Group;
import entity.Money;
import entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The View Balances Interactor
 */
public class ViewBalancesInteractor implements ViewBalancesInputBoundary {

    private final ViewBalancesDataAccessInterface dataAccess;
    private final ViewBalancesOutputBoundary presenter;

    public ViewBalancesInteractor(ViewBalancesDataAccessInterface dataAccess,
                                  ViewBalancesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewBalancesInputData inputData) {
        if (inputData.getGroupId() == null || inputData.getGroupId().trim().isEmpty()) {
            presenter.prepareFailView("Please select a group.");
            return;
        }

        final Group group = dataAccess.getGroup(inputData.getGroupId());
        if (group == null) {
            presenter.prepareFailView("Group not found.");
            return;
        }

        final User currentUser = dataAccess.getCurrentUser();
        if (currentUser == null || !group.isMember(currentUser)) {
            presenter.prepareFailView("You must be a group member to view its balances.");
            return;
        }

        final Map<User, Money> calculated = new BalanceCalculator(group).calculate();
        final List<ViewBalancesOutputData.BalanceRow> rows = new ArrayList<>();
        boolean allSettledUp = true;
        for (Map.Entry<User, Money> entry : calculated.entrySet()) {
            rows.add(new ViewBalancesOutputData.BalanceRow(
                    entry.getKey().getEmail(),
                    entry.getValue().getAmount(),
                    entry.getValue().getCurrency().getCurrencyCode()));
            if (entry.getValue().getAmount().signum() != 0) {
                allSettledUp = false;
            }
        }
        presenter.prepareSuccessView(new ViewBalancesOutputData(rows, allSettledUp));
    }
}
