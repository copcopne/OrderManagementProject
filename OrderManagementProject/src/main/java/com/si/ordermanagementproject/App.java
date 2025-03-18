package com.si.ordermanagementproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Quản lý đơn hàng");
        primaryStage.setResizable(false);

        // Gọi cửa sổ đăng nhập
        showLoginStage();
    }

    // Hiển thị cửa sổ đăng nhập
    private void showLoginStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        Stage loginStage = new Stage();
        loginStage.setTitle("Đăng nhập");
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setResizable(false);
        loginStage.setScene(new Scene(root));

        // Truyền tham chiếu của cửa sổ chính vào controller đăng nhập
        LoginController loginController = loader.getController();
        loginController.setMainApp(this, loginStage);

        loginStage.show();
    }

    // Hiển thị cửa sổ chính sau khi đăng nhập thành công
    public void showMainStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
        
    public static void main(String[] args) {
        launch();
    }
    
}
