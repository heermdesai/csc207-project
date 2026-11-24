package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalTime;
import java.util.Locale;

/**
 * The home screen shown after logging in.
 */
public class HomeView extends JPanel implements PropertyChangeListener {

    private static final int SIDEBAR_WIDTH = 248;
    private static final int GAP = 20;
    private static final int NOON = 12;
    private static final int EVENING = 17;
    private static final Color SIDEBAR_MUTED = new Color(140, 139, 133);
    private static final Color SIDEBAR_HOVER = new Color(40, 40, 40);
    private static final Color SIDEBAR_CARD = new Color(32, 32, 32);

    private final LoggedInViewModel loggedInViewModel;
    private final JLabel greeting = AuthLayout.text(" ", Font.BOLD, 28f, Theme.INK);
    private final JLabel emailLabel = AuthLayout.text(" ", Font.PLAIN, 12f, SIDEBAR_MUTED);
    private final JPanel groupsSlot = slot("Groups you create or join will show up here.");
    private final JPanel balancesSlot = slot("Who owes whom will show up here.");
    private final JPanel expensesSlot = slot("Expenses added to your groups will show up here.");
    private LogoutController logoutController;
    private CreateGroupView createGroupView;

    public HomeView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        loggedInViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Theme.CANVAS);
        add(sidebar(), BorderLayout.WEST);
        add(mainArea(), BorderLayout.CENTER);
    }

    private JPanel sidebar() {
        final JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Theme.INK);
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(28, 20, 20, 20));
        sidebar.add(AuthLayout.logo(), BorderLayout.NORTH);

        final JPanel middle = AuthLayout.formColumn();
        middle.setBorder(BorderFactory.createEmptyBorder(40, 4, 0, 0));
        middle.add(AuthLayout.text("YOUR GROUPS", Font.BOLD, 11f, SIDEBAR_MUTED));
        middle.add(Box.createVerticalStrut(10));
        middle.add(AuthLayout.wrapped("No groups yet.", Font.PLAIN, 13f, Theme.BRAND_TEXT, SIDEBAR_WIDTH - 48));
        sidebar.add(middle, BorderLayout.CENTER);

        final RoundedPanel account = new RoundedPanel(new BorderLayout(0, 12), 20, SIDEBAR_CARD, null);
        account.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        final JPanel who = AuthLayout.formColumn();
        who.add(AuthLayout.text("Signed in as", Font.BOLD, 13f, Color.WHITE));
        who.add(Box.createVerticalStrut(4));
        who.add(emailLabel);
        account.add(who, BorderLayout.NORTH);
        final SecondaryButton logOut = new SecondaryButton("Log out", Color.WHITE, SIDEBAR_MUTED, SIDEBAR_HOVER);
        logOut.addActionListener(evt -> logoutController.execute());
        account.add(logOut, BorderLayout.SOUTH);
        sidebar.add(account, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel mainArea() {
        final JPanel main = new JPanel(new BorderLayout(0, 28));
        main.setBackground(Theme.CANVAS);
        main.setBorder(BorderFactory.createEmptyBorder(36, 40, 36, 40));

        final JPanel header = AuthLayout.formColumn();
        header.add(greeting);
        header.add(Box.createVerticalStrut(6));
        header.add(AuthLayout.text("Here's where things stand across your groups.", Font.PLAIN, 14f, Theme.MUTED));
        final JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(header, BorderLayout.CENTER);
        final PrimaryButton newGroup = new PrimaryButton("+ New group");
        newGroup.setPreferredSize(new Dimension(150, 40));
        newGroup.addActionListener(evt -> openCreateGroup());
        final JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        actions.add(newGroup, BorderLayout.NORTH);
        top.add(actions, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);

        final JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.6;
        c.insets = new Insets(0, 0, GAP, GAP);
        grid.add(card("Your groups", groupsSlot), c);

        c.gridx = 1;
        c.weightx = 0.4;
        c.insets = new Insets(0, 0, GAP, 0);
        grid.add(card("Balances", balancesSlot), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(card("Recent expenses", expensesSlot), c);

        main.add(grid, BorderLayout.CENTER);
        return main;
    }

    private static JPanel card(String title, JPanel content) {
        final RoundedPanel card = new RoundedPanel(new BorderLayout(0, 14), 24, Theme.SURFACE, Theme.BORDER);
        card.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        card.add(AuthLayout.text(title, Font.BOLD, 16f, Theme.INK), BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private static JPanel slot(String placeholder) {
        final JPanel slot = new JPanel(new BorderLayout());
        slot.setOpaque(false);
        slot.add(AuthLayout.text(placeholder, Font.PLAIN, 13f, Theme.MUTED), BorderLayout.NORTH);
        return slot;
    }

    private static void fill(JPanel slot, JComponent content) {
        slot.removeAll();
        slot.add(content, BorderLayout.CENTER);
        slot.revalidate();
        slot.repaint();
    }

    public void setGroupsContent(JComponent content) {
        fill(groupsSlot, content);
    }

    public void setBalancesContent(JComponent content) {
        fill(balancesSlot, content);
    }

    public void setRecentExpensesContent(JComponent content) {
        fill(expensesSlot, content);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoggedInState state = loggedInViewModel.getState();
        greeting.setText(timeOfDayGreeting(LocalTime.now()) + ", " + displayName(state.getEmail()));
        emailLabel.setText(state.getEmail());
        emailLabel.setToolTipText(state.getEmail());
    }

    static String timeOfDayGreeting(LocalTime time) {
        if (time.getHour() < NOON) {
            return "Good morning";
        }
        if (time.getHour() < EVENING) {
            return "Good afternoon";
        }
        return "Good evening";
    }

    static String displayName(String email) {
        if (email == null || email.isBlank()) {
            return "there";
        }
        final String first = email.split("@")[0].split("[._\\-+]")[0];
        if (first.isEmpty()) {
            return "there";
        }
        return first.substring(0, 1).toUpperCase(Locale.ROOT) + first.substring(1);
    }

    public String getViewName() {
        return loggedInViewModel.getViewName();
    }

    private void openCreateGroup() {
        if (createGroupView == null) {
            return;
        }
        createGroupView.prepare(loggedInViewModel.getState().getEmail());
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "New group",
                JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(createGroupView);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void setCreateGroupView(CreateGroupView createGroupView) {
        this.createGroupView = createGroupView;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
