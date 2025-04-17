package com.si.ordermanagementproject;

import com.si.pojo.UserSession;
import com.si.pojo.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class PrimaryController {
    @FXML private Label nameLb;
    
    public void initialize() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        nameLb.setText("Chào mừng, " + currentUser.getFullName());
    }
}
