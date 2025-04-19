package com.si.ordermanagementproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Quản lý đơn hàng");
        primaryStage.setResizable(false);

        // Gọi cửa sổ đăng nhập từ LoginStage
        LoginStage.showLoginStage(primaryStage, this);
    }

    // Hiển thị cửa sổ chính sau khi đăng nhập thành công
    public void showMainStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userManagement.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}
