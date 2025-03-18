package com.si.ordermanagementproject;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
//    private User currentUser;
    @FXML
    private void LoginHandler() throws IOException {
        App.setRoot("login");
    }
}
