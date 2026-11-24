package app;

import entity.EqualSplitStrategy;
import entity.Expense;
import entity.Group;
import entity.Settlement;
import interface_adapter.view_balances.ViewBalancesController;
import interface_adapter.view_balances.ViewBalancesPresenter;
import interface_adapter.view_balances.ViewBalancesState;
import interface_adapter.view_balances.ViewBalancesViewModel;
import use_case.view_balances.ViewBalancesDataAccessInterface;
import use_case.view_balances.ViewBalancesInteractor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary: opens just the View Balances screen with a made-up group,
 * so it can be tried before the group page exists. Delete once it's wired into AppBuilder.
 *
 * There is no real View Balances screen yet, so a small throwaway display is built here.
 * Pick who you are with the drop-down: members see the balances, a stranger gets the error.
 */
public class ViewBalancesDemo {

    private static final String ALICE = "alice@mail.com";
    private static final String BOB = "bob@mail.com";
    private static final String CHEN = "chen@mail.com";
    private static final String STRANGER = "stranger@mail.com";

    /** Stands in for the real storage: one made-up group plus whoever is "logged in". */
    private static class DemoStorage implements ViewBalancesDataAccessInterface {
        private final Group group;
        private final List<Expense> expenses = new ArrayList<>();
        private final List<Settlement> settlements = new ArrayList<>();
        private String currentEmail;

        DemoStorage(Group group) {
            this.group = group;
        }

        @Override
        public Group getGroup(String groupId) {
            return group.getId().equals(groupId) ? group : null;
        }

        @Override
        public List<Expense> getExpenses(String groupId) {
            return expenses;
        }

        @Override
        public List<Settlement> getSettlements(String groupId) {
            return settlements;
        }

        @Override
        public String getCurrentEmail() {
            return currentEmail;
        }
    }

    public static void main(String[] args) {
        // A made-up group, since Create Group isn't wired to this screen yet
        Group group = new Group("g1", "Roommates", "ABC123", "CAD", ALICE);
        group.addMember(BOB);
        group.addMember(CHEN);

        DemoStorage storage = new DemoStorage(group);
        storage.currentEmail = ALICE;
        // Alice pays 90.00 for everyone: each share is 30.00
        storage.expenses.add(new Expense("e1", "g1", "Groceries", ALICE, List.of(ALICE, BOB, CHEN),
                new BigDecimal("90.00"), "CAD", LocalDate.of(2026, 1, 5), new EqualSplitStrategy()));
        // Bob pays 30.00 for himself and Chen: each share is 15.00
        storage.expenses.add(new Expense("e2", "g1", "Internet", BOB, List.of(BOB, CHEN),
                new BigDecimal("30.00"), "CAD", LocalDate.of(2026, 1, 8), new EqualSplitStrategy()));
        // Chen has already paid Alice back 20.00
        storage.settlements.add(new Settlement("s1", "g1", CHEN, ALICE,
                new BigDecimal("20.00"), LocalDate.of(2026, 1, 10)));

        // Build the pieces and connect them (what AppBuilder will do)
        ViewBalancesViewModel viewModel = new ViewBalancesViewModel();
        ViewBalancesPresenter presenter = new ViewBalancesPresenter(viewModel);
        ViewBalancesInteractor interactor = new ViewBalancesInteractor(storage, presenter);
        ViewBalancesController controller = new ViewBalancesController(interactor);

        // A throwaway display that listens to the view model, like the real view will
        JLabel title = new JLabel("Balances: " + group.getName());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel status = new JLabel(" ");
        JLabel error = new JLabel(" ");
        error.setForeground(new Color(196, 64, 48));
        JTextArea lines = new JTextArea(8, 30);
        lines.setEditable(false);
        lines.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));

        viewModel.addPropertyChangeListener(evt -> {
            ViewBalancesState state = viewModel.getState();
            lines.setText(String.join("\n", state.getBalanceLines()));
            error.setText(state.getError() == null ? " " : state.getError());
            status.setText(state.getError() != null ? " "
                    : state.isAllSettledUp() ? "Everyone is settled up." : "Positive = owed money, negative = owes money.");
        });

        // Lets you pretend to be a different logged-in person
        JComboBox<String> viewAs = new JComboBox<>(new String[] {ALICE, BOB, CHEN, STRANGER});
        viewAs.addActionListener(evt -> {
            storage.currentEmail = (String) viewAs.getSelectedItem();
            controller.execute(group.getId());
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(new JLabel("View as:"));
        panel.add(viewAs);
        panel.add(Box.createVerticalStrut(12));
        panel.add(error);
        panel.add(lines);
        panel.add(Box.createVerticalStrut(8));
        panel.add(status);
        viewAs.setMaximumSize(viewAs.getPreferredSize());
        for (Component part : panel.getComponents()) {
            ((JComponent) part).setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        // What the group page will do when someone opens a group's balances
        controller.execute(group.getId());

        JFrame frame = new JFrame("Splitter: View Balances");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(720, 480);
        frame.setVisible(true);
    }
}
