package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addHomeView()
                .addCreateGroupView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addCreateGroupUseCase()
                .build();

        application.setMinimumSize(new java.awt.Dimension(1100, 720));
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
