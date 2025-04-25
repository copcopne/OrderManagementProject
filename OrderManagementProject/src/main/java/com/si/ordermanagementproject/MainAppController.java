package com.si.ordermanagementproject;

import com.si.pojo.UserSession;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainAppController implements Initializable {

    @FXML
    private StackPane mainContent;

    @FXML
    private VBox menuVBox;

    @FXML
    private VBox menuHeader;

    @FXML
    private VBox menuGroupContainer;

    @FXML
    private VBox menuFooter;

    @FXML
    private Label contentLb;

    private final Map<String, String> menuToFxmlMap = new LinkedHashMap<>();

    private final Map<String, Parent> loadedViews = new LinkedHashMap<>();

    private final UserSession session = UserSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contentLb.setText("Chào mừng, " + session.getUsername());
        setupMenu();
        preloadViews();
    }

    private void setupMenu() {
        addSection("Nhập/Xuất hàng hóa", Map.of(
                "Nhập hàng hóa", "/com/si/ordermanagementproject/stockIn.fxml",
                "Xuất hàng hóa", "/com/si/ordermanagementproject/stockOut.fxml"
        ));

        addSection("Theo dõi tồn kho", Map.of(
                "Bảng tồn kho", "/com/si/ordermanagementproject/inventorymanagement.fxml"
        ));

        addSection("Quản lý đơn đặt hàng", Map.of(
                "Bảng đơn hàng", "/com/si/ordermanagementproject/ordermanagement.fxml"
        ));

        if (session.getIsAdmin() == true) {
            addFooterButton("Quản lý tài khoản", "/com/si/ordermanagementproject/userManagement.fxml");
        }

        addFooterButton("Chỉnh sửa thông tin cá nhân", "/com/si/ordermanagementproject/editUser.fxml");
        addFooterButton("Đăng xuất", "");
    }

    private void preloadViews() {
        menuToFxmlMap.forEach((name, path) -> {
            try {
                Parent view = FXMLLoader.load(getClass().getResource(path));
                loadedViews.put(name, view);
            } catch (IOException e) {
                System.err.println("Không thể load FXML: " + path);
                e.printStackTrace();
            }
        });
    }

    private void addSection(String title, Map<String, String> items) {
        Label sectionLabel = new Label(title);
        sectionLabel.getStyleClass().add("menu-section-title");

        VBox sectionBox = new VBox(sectionLabel);
        sectionBox.setSpacing(5);
        items.forEach((name, path) -> {
            Button btn = new Button(name);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("menu-button");
            btn.setOnAction(e -> switchView(name));
            sectionBox.getChildren().add(btn);
            menuToFxmlMap.put(name, path);
        });

        menuGroupContainer.getChildren().add(sectionBox);
    }

    private void addFooterButton(String name, String path) {
        Button btn = new Button(name);
        btn.getStyleClass().add("menu-button");
        btn.setMaxWidth(Double.MAX_VALUE);

        switch (name) {
            case "Đăng xuất":
                btn.setOnAction(e -> confirmLogout());
                break;
            case "Chỉnh sửa thông tin cá nhân":
                btn.setOnAction(e -> editAccount());
                break;
            default:
                if (session.getIsAdmin() == true) {
                    btn.setOnAction(e -> switchView(name));
                    menuToFxmlMap.put(name, path);
                }
                break;
        }

        menuFooter.getChildren().add(btn);
    }

    private void confirmLogout() {
        Alert alert = Utils.getConfirmAlert("Bạn có chắc chắn muốn đăng xuất không?");
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Utils.getAlert("Đăng xuất thành công!").showAndWait();
                session.logout();
                try {
                    // Đóng cửa sổ chính hiện tại
                    Stage currentStage = (Stage) mainContent.getScene().getWindow();
                    currentStage.close();

                    // Mở lại cửa sổ login
                    LoginStage.showLoginStage(new Stage(), new App());

                } catch (IOException ex) {
                    ex.printStackTrace();
            }
            }
        });
    }

    private void editAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/si/ordermanagementproject/editUser.fxml"));
            Parent root = loader.load();

            EditUserController controller = loader.getController();
            controller.setUserData(session.getCurrentUser());

            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa tài khoản cá nhân");
            stage.setScene(new Scene(root));
            stage.initOwner(mainContent.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void switchView(String name) {
        Parent view = loadedViews.get(name);
        if (view != null) {
            mainContent.getChildren().setAll(view);
            contentLb.setText(name);
            fadeIn(view);
        } else {
            System.err.println("Không tìm thấy view cho: " + name);
        }
    }

    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
