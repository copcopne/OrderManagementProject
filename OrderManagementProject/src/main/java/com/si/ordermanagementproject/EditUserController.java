/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.User;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author copcopne
 */
public class EditUserController implements Initializable {

    private String currentUsername;

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField fullNameTxt;
    @FXML
    private CheckBox isAdmin;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button restPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setUserData(User selectedUser) {
        this.currentUsername = selectedUser.getUsername();
        usernameTxt.setText(selectedUser.getUsername());
        fullNameTxt.setText(selectedUser.getFullName());
        isAdmin.setSelected(selectedUser.getIsAdmin());
    }

    public void cancel() {
        cancel.getScene().getWindow().hide();
    }

    public void process() {
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn lưu thay đổi không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserServices s = new UserServices();
            try {
                boolean rs = s.updateUser(currentUsername, usernameTxt.getText(), fullNameTxt.getText(), null, null);
                if (rs == true) {
                    Utils.getAlert("Cập nhật thành công!").showAndWait();
                    ok.getScene().getWindow().hide();
                }
            } catch (IllegalArgumentException ex) {
                Utils.getAlert("LỖI dữ liệu đầu vào: " + ex.getMessage()).show();
            } catch (SQLException ex) {
                Utils.getAlert("LỖI truy vấn: " + ex.getMessage()).show();
            }
        }
    }

    public void reset() {
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn đặt lại mật khẩu mặc định là 123456Abc@ không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            UserServices s = new UserServices();
            try {
                boolean rs = s.updateUser(currentUsername, currentUsername, fullNameTxt.getText(), "123456Abc@", null);
                if (rs == true) {
                    Utils.getAlert("Cập nhật thành công!").showAndWait();
                    restPassword.getScene().getWindow().hide();
                }
            } catch (IllegalArgumentException ex) {
                Utils.getAlert("LỖI dữ liệu đầu vào: " + ex.getMessage()).show();
            } catch (SQLException ex) {
                Utils.getAlert("LỖI truy vấn: " + ex.getMessage()).show();
            }
        }
    }
}
