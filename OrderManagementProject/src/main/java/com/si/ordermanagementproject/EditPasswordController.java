/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.UserSession;
import com.si.pojo.User;
import com.si.services.AuthServices;
import com.si.services.UserServices;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author copcopne
 */
public class EditPasswordController implements Initializable {
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void cancel() {
        cancel.getScene().getWindow().hide();
    }

    public void process() {
        String currentUserUsername = UserSession.getInstance().getUsername();
        
        User u = AuthServices.authenticate(currentUserUsername, oldPassword.getText());
        
        if (u == null) {
            Utils.getAlert("Mật khẩu hiện tại không đúng!").show();
            oldPassword.requestFocus();
            return;
        }
        
        String newP = newPassword.getText();
        String confirmP = confirmPassword.getText();
        
        if(newP.isEmpty()){
            Utils.getAlert("Mật khẩu mới không được trống!").show();
            newPassword.requestFocus();
            return;
        }
        if(confirmP.isEmpty()){
            Utils.getAlert("Mật khẩu xác nhận không được trống!").show();
            confirmPassword.requestFocus();
            return;
        }
        if(!newP.equals(confirmP)) {
            Utils.getAlert("Mật khẩu đã nhập không khớp!").show();
            newPassword.requestFocus();
            return;
        }
        
        try {
            AuthServices.isPasswordValid(newP);
        }
        catch (IllegalArgumentException ex) {
            Utils.getAlert(ex.getMessage()).show();
            newPassword.requestFocus();
            return;
        }
        
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn lưu thay đổi không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UserServices s = new UserServices();
                s.updateUser(currentUserUsername, null, null, newP, null);
                
                Utils.getAlert("Cập nhật mật khẩu thành công!").showAndWait();
                this.ok.getScene().getWindow().hide();
                
            } catch (IllegalArgumentException ex) {
                Utils.getAlert("LỖI dữ liệu đầu vào: " + ex.getMessage()).show();
            } catch (SQLException ex) {
                Utils.getAlert("LỖI truy vấn: " + ex.getMessage()).show();
            }
        }
    }
}
