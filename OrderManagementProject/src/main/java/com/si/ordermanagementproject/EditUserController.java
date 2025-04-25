/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.UserSession;
import com.si.pojo.User;
import com.si.services.AuthServices;
import com.si.services.UserServices;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private Label isAdminLb;
    @FXML
    private CheckBox isAdmin;
    @FXML
    private Label passwordLb;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button editPassword;
    @FXML
    private Button resetPassword;

    private final UserSession session = UserSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!session.isLoggedIn()) {
            Utils.getAlert("Bạn chưa đăng nhập").showAndWait();
            ok.getScene().getWindow().hide();
        } else {
            passwordTxt.setDisable(true);
            passwordTxt.setVisible(false);
            passwordLb.setVisible(false);
            resetPassword.setVisible(false);
            resetPassword.setDisable(true);
        }

    }

    public void setUserData(User selectedUser) {
        this.currentUsername = selectedUser.getUsername();
        usernameTxt.setText(selectedUser.getUsername());
        fullNameTxt.setText(selectedUser.getFullName());
        isAdmin.setSelected(selectedUser.getIsAdmin());

        if (!this.currentUsername.equals(session.getUsername())) {
            editPassword.setVisible(false);
            editPassword.setDisable(true);
        }

        if (!session.getIsAdmin()) {
            isAdmin.setVisible(false);
            isAdmin.setDisable(true);
            isAdminLb.setVisible(false);
            passwordLb.setVisible(true);
            passwordTxt.setDisable(false);
            passwordTxt.setVisible(true);
        }

        if (session.getIsAdmin() && !(session.getUsername().equals(this.currentUsername))) {
            resetPassword.setVisible(true);
            resetPassword.setDisable(false);
        }
    }

    public void cancel() {
        cancel.getScene().getWindow().hide();
    }

    public void process() {
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn lưu thay đổi không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UserServices s = new UserServices();
                if (session.getIsAdmin() != true) {
                    if (passwordTxt.getText().isEmpty()) {
                        passwordTxt.requestFocus();
                        throw new IllegalArgumentException("Chưa xác nhận mật khẩu!");
                    }
                    User u = AuthServices.authenticate(currentUsername, passwordTxt.getText());
                    if (u == null) {
                        throw new IllegalArgumentException("Mật khẩu không đúng!");
                    }
                }
                boolean rs = s.updateUser(currentUsername, usernameTxt.getText(), fullNameTxt.getText(), null, isAdmin.isSelected());
                if (rs == true) {
//                    User thisUser = s.findUserByUsername(usernameTxt.getText());
//                    session.login(thisUser);
                    if (!session.getIsAdmin() || session.getUsername().equals(usernameTxt.getText())) {
                        User thisUser = s.findUserByUsername(usernameTxt.getText());
                        session.login(thisUser);
                    }
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
        if (session.getIsAdmin() == true) {
            Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn đặt lại mật khẩu mặc định là 123456Abc@ không?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                UserServices s = new UserServices();
                try {
                    boolean rs = s.updateUser(currentUsername, currentUsername, fullNameTxt.getText(), "123456Abc@", null);
                    if (rs == true) {
                        Utils.getAlert("Cập nhật thành công!").showAndWait();
                        resetPassword.getScene().getWindow().hide();
                    }
                } catch (IllegalArgumentException ex) {
                    Utils.getAlert("LỖI dữ liệu đầu vào: " + ex.getMessage()).show();
                } catch (SQLException ex) {
                    Utils.getAlert("LỖI truy vấn: " + ex.getMessage()).show();
                }
            }
        }
    }

    public void editPasswordHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editPassword.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa mật khẩu");
            stage.setScene(new Scene(root));
            stage.initOwner(ok.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHidden(e -> {
                this.ok.getScene().getWindow().hide();
            });
            stage.show();

        } catch (IOException e) {
            Utils.getAlert(e.getMessage()).show();
        }
    }
}
