package com.si.ordermanagementproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class App extends Application {

    protected static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        LoginStage.showLoginStage(primaryStage, this);
    }

    public void showMainStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainApp.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets()
                .add(getClass()
                        .getResource("/com/si/ordermanagementproject/Styles/MainApp.css")
                        .toExternalForm());
        primaryStage.setTitle("Quản lý đơn hàng");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(eh -> {
            Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn thoát không?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            } else {
                eh.consume();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
