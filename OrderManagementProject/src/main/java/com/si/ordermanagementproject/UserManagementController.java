/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.UserSession;
import com.si.pojo.User;
import com.si.services.UserServices;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author copcopne
 */
public class UserManagementController implements Initializable {

    @FXML
    private TableView<User> table;
    @FXML
    private TextField searchTxt;
    @FXML
    private Button addBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadColumns();
        loadTableData("");
        this.searchTxt.textProperty().addListener((e) -> {
            loadTableData(searchTxt.getText());
        });
    }

    public void loadColumns() {
        TableColumn colId = new TableColumn("id");
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colId.setPrefWidth(50);

        TableColumn colUsername = new TableColumn("Tên người dùng");
        colUsername.setCellValueFactory(new PropertyValueFactory("username"));

        TableColumn colFullName = new TableColumn("Họ và tên");
        colFullName.setCellValueFactory(new PropertyValueFactory("fullName"));

        TableColumn colAdmin = new TableColumn("là Quản trị");
        colAdmin.setCellValueFactory(new PropertyValueFactory("isAdmin"));

        TableColumn<User, Void> colAction1 = new TableColumn<>();
        colAction1.setCellFactory(col -> new TableCell<User, Void>() {
            Button editBtn = new Button("Chỉnh sửa");

            {
                editBtn.setOnAction(evt -> {
                    User selectedUser = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editUser.fxml"));
                        Parent root = fxmlLoader.load();
                        root.setStyle("-fx-border-color: #999999; -fx-border-width: 2px; -fx-border-radius: 5px;");

                        EditUserController controller = fxmlLoader.getController();
                        controller.setUserData(selectedUser);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setOnHidden(e -> {
                            loadTableData("");
                        });
                        stage.show();

                    } catch (IOException e) {
                        Utils.getAlert(e.getMessage()).show();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editBtn);
                }
            }

        });

        TableColumn<User, Void> colAction2 = new TableColumn<>();
        colAction2.setCellFactory(col -> new TableCell<User, Void>() {
            Button delBtn = new Button("Xóa người dùng");

            {
                delBtn.setOnAction(evt -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    if (selectedUser.getId() == UserSession.getInstance().getCurrentUser().getId()) {
                        Alert alert = Utils.getAlert("Bạn không thể xóa tài khoản của chính mình.");
                        alert.show();
                        return;
                    }

                    TableRow currentRow = getTableRow();
                    currentRow.setStyle("-fx-background-color: #ffe6e6;");

                    Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn xóa người dùng này không?");
                    Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        UserServices s = new UserServices();
                        if (s.deleteUser(selectedUser.getUsername()) != true) {
                            Utils.getAlert("Không thể xóa người dùng này.").show();
                            return;
                        }
                        Utils.getAlert("Xóa thành công.").show();
                        loadTableData("");
                    }
                    currentRow.setStyle("");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(delBtn);
                }
            }

        });

        this.table.getColumns().addAll(colId, colUsername, colFullName, colAdmin, colAction1, colAction2);
    }

    public void loadTableData(String kw) {
        UserServices u = new UserServices();
        this.table.setItems(FXCollections.observableList(u.findUsers(kw)));
    }

    public void addHandler() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addUser.fxml"));
            Parent root = fxmlLoader.load();
            root.setStyle("-fx-border-color: #999999; -fx-border-width: 2px; -fx-border-radius: 5px;");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setOnHidden(e -> {
                loadTableData("");
            });
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
