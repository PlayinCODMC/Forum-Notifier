package com.cldfire.spigotnotifier2;

import com.cldfire.spigotnotifier2.util.LangUtils;
import com.cldfire.spigotnotifier2.util.PropertyUtils;
import com.cldfire.spigotnotifier2.util.LangUtils.Locale;

import com.cldfire.spigotnotifier2.view.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

public class MainApp extends Application { // Project started April 1st, 2016

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStatView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StatView.fxml"));
            AnchorPane statView = loader.load();

            rootLayout.setCenter(statView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        try {
            // Load login view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LoginView.fxml"));
            AnchorPane loginView = loader.load();

            // Set the stat view into the center of root layout.
            rootLayout.setCenter(loginView);

            LoginViewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        LangUtils.loadLocale(Locale.EN_US);
        PropertyUtils.loadSettings();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(LangUtils.translate("window.title"));

        System.out.println(System.getProperty("user.home"));

        initRootLayout();
        showLoginView();
    }
}
