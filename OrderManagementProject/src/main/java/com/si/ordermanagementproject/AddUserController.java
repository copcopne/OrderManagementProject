/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author copcopne
 */
public class AddUserController implements Initializable {

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField fullNameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField confirmPasswordTxt;
    @FXML
    private CheckBox isAdmin;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void process() {
        if (usernameTxt.getText().isEmpty()) {
            Utils.getAlert("Vui lòng nhập tên người dùng!").show();
            usernameTxt.requestFocus();
            return;
        }
        if (fullNameTxt.getText().isEmpty()) {
            Utils.getAlert("Vui lòng nhập họ và tên!").show();
            fullNameTxt.requestFocus();
            return;
        }
        if (passwordTxt.getText().isEmpty()) {
            Utils.getAlert("Vui lòng nhập mật khẩu!").show();
            passwordTxt.requestFocus();
            return;
        }
        if (confirmPasswordTxt.getText().isEmpty()) {
            Utils.getAlert("Vui lòng nhập lại mật khẩu!").show();
            confirmPasswordTxt.requestFocus();
            return;
        }

        UserServices s = new UserServices();

        User temp = s.findUserByUsername(usernameTxt.getText());
        if (temp != null) {
            Utils.getAlert("Tên người dùng đã tồn tại trong hệ thống!").show();
            usernameTxt.requestFocus();
            return;
        }
        if (!passwordTxt.getText().equals(confirmPasswordTxt.getText())) {
            passwordTxt.requestFocus();
            Utils.getAlert("Mật khẩu đã nhập không khớp!").show();
            return;
        }
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn tạo không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean rs = s.createUser(fullNameTxt.getText(), usernameTxt.getText(),
                        passwordTxt.getText(), isAdmin.isSelected());
                if (rs == true) {
                    Utils.getAlert("Tạo thành công!").showAndWait();
                    ok.getScene().getWindow().hide();
                }
            } catch (IllegalArgumentException ex) {
                Utils.getAlert("LỖI dữ liệu đầu vào: " + ex.getMessage()).show();
            } catch (SQLException ex) {
                Utils.getAlert("LỖI truy vấn: " + ex.getMessage()).show();
            }
        }
    }

    public void cancel() {
        cancel.getScene().getWindow().hide();
    }
}
