/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.ordermanagementproject;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author copcopne
 */
public class LoginStage {

    public static void showLoginStage(Stage primaryStage, App mainApp) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginStage.class.getResource("login.fxml"));
        Parent root = loader.load();
        Stage loginStage = new Stage();
        loginStage.setTitle("Đăng nhập");
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setResizable(false);
        loginStage.setScene(new Scene(root));

        LoginController loginController = loader.getController();
        
        loginController.setMainApp(mainApp, loginStage);
        

        loginStage.show();
    }
}
