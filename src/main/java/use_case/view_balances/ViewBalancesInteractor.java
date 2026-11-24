package use_case.view_balances;

import entity.BalanceCalculator;
import entity.Group;

import java.math.BigDecimal;
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

        final String currentEmail = dataAccess.getCurrentEmail();
        if (currentEmail == null || !group.isMember(currentEmail)) {
            presenter.prepareFailView("You must be a group member to view its balances.");
            return;
        }

        final Map<String, BigDecimal> calculated = new BalanceCalculator().calculate(
                group.getMemberEmails(),
                dataAccess.getExpenses(group.getId()),
                dataAccess.getSettlements(group.getId()));

        final List<ViewBalancesOutputData.BalanceRow> rows = new ArrayList<>();
        boolean allSettledUp = true;
        for (Map.Entry<String, BigDecimal> entry : calculated.entrySet()) {
            rows.add(new ViewBalancesOutputData.BalanceRow(
                    entry.getKey(),
                    entry.getValue(),
                    group.getBaseCurrency()));
            if (entry.getValue().signum() != 0) {
                allSettledUp = false;
            }
        }
        presenter.prepareSuccessView(new ViewBalancesOutputData(rows, allSettledUp));
    }
}
