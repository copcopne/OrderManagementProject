package com.si.ordermanagementproject;

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
//    private User currentUser;
    @FXML
    private void LoginHandler() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("admin".equals(username) && "123456".equals(password)) {
            // Xử lý đăng nhập
            // ...
            
            // Thêm người dùng vào session
            UserSession userSession = UserSession.getInstance();
            userSession.login(username);
            
            // Lấy cửa sổ login và đóng
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
            mainApp.showMainStage();
        }
        else {
            Utils.getAlert("Tài khoản hoặc mật khẩu không đúng!").show();
        }
    }
    public void setMainApp(App mainApp, Stage loginStage) {
        this.mainApp = mainApp;
        this.loginStage = loginStage;
    }
}
