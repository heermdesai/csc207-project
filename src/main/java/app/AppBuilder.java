package app;

import data_access.FileUserDataAccessObject;
import data_access.FileGroupDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_group.CreateGroupController;
import interface_adapter.create_group.CreateGroupPresenter;
import interface_adapter.create_group.CreateGroupViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.create_group.CreateGroupInputBoundary;
import use_case.create_group.CreateGroupInteractor;
import use_case.create_group.CreateGroupOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.CreateGroupView;
import view.HomeView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package
    final FileUserDataAccessObject userDataAccessObject = createFileDataAccessObject();

    private FileUserDataAccessObject createFileDataAccessObject() {
        try {
            return new FileUserDataAccessObject("users.csv", userFactory);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private HomeView homeView;
    private CreateGroupViewModel createGroupViewModel;
    private CreateGroupView createGroupView;
    private final FileGroupDataAccessObject groupDataAccessObject = new FileGroupDataAccessObject("groups.json");
    private LoginView loginView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addHomeView() {
        loggedInViewModel = new LoggedInViewModel();
        homeView = new HomeView(loggedInViewModel);
        cardPanel.add(homeView, homeView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }


    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        homeView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addCreateGroupView() {
        createGroupViewModel = new CreateGroupViewModel();
        createGroupView = new CreateGroupView(createGroupViewModel);
        homeView.setCreateGroupView(createGroupView);
        return this;
    }

    public AppBuilder addCreateGroupUseCase() {
        final CreateGroupOutputBoundary createGroupPresenter = new CreateGroupPresenter(createGroupViewModel);
        final CreateGroupInputBoundary createGroupInteractor =
                new CreateGroupInteractor(groupDataAccessObject, createGroupPresenter);
        createGroupView.setCreateGroupController(new CreateGroupController(createGroupInteractor));
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Splitter");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }


}
