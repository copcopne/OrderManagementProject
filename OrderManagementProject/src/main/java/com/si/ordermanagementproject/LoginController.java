package com.si.ordermanagementproject;

import com.si.pojo.User;
import com.si.services.AuthServices;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private App mainApp;
    private Stage loginStage;

    @FXML
    private void LoginHandler() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty()) {
            Utils.getAlert("Vui lòng nhập tên đăng nhập!").show();
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            Utils.getAlert("Vui lòng nhập mật khẩu!").show();
            passwordField.requestFocus();
            return;
        }
        
        User user = AuthServices.authenticate(username, password);
        if (user != null) {
            UserSession userSession = UserSession.getInstance();
            userSession.login(user);

            // Lấy cửa sổ login và đóng
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
            mainApp.showMainStage();
        } else {
            Utils.getAlert("Tài khoản hoặc mật khẩu không đúng!").show();
        }
    }
    
    public void setMainApp(App mainApp, Stage loginStage) {
        this.mainApp = mainApp;
        this.loginStage = loginStage;
    }
}
