package com.si.ordermanagementproject;

import com.si.pojo.Product;
import com.si.services.ProductServices;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InventoryManagementController implements Initializable {

    @FXML
    TableView<Product> tbProducts;
    @FXML
    TextField txtSearch;
    @FXML
    Button importBtn;
    @FXML
    Button deductBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbProducts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadColumns();
        loadData(null);

        ScheduledService<Void> checkService = new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        Platform.runLater(() -> loadData(null));
                        return null;
                    }
                };
            }
        };

        checkService.setPeriod(Duration.seconds(10)); //Kiểm tra định kỳ 5 phút
        checkService.start();

        this.txtSearch.textProperty().addListener(e -> {
            loadData(this.txtSearch.getText());
        });

        importBtn.setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stockIn.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Nhập hàng");
                stage.setScene(new Scene(root));
                stage.initOwner(importBtn.getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.setOnHidden(e -> {
                    loadData("");
                });
                stage.show();

            } catch (IOException e) {
                Utils.getAlert(e.getMessage()).show();
            }
        });
        deductBtn.setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stockOut.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Xuất hàng");
                stage.setScene(new Scene(root));
                stage.initOwner(deductBtn.getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setResizable(false);
                stage.setOnHidden(e -> {
                    loadData("");
                });
                stage.show();

            } catch (IOException e) {
                Utils.getAlert(e.getMessage()).show();
            }
        });
    }

    public void loadData(String kw) {
        ProductServices p = new ProductServices();
        List<Product> products = p.getProducts(kw);
        tbProducts.setItems(FXCollections.observableList(products));
        applyRowStyling();
    }

    public void loadColumns() {
        TableColumn colSKU = new TableColumn("Mã SKU");
        colSKU.setCellValueFactory(new PropertyValueFactory("SKU"));
        TableColumn colProductName = new TableColumn("Tên sản phẩm");
        colProductName.setCellValueFactory(new PropertyValueFactory("ProductName"));
        TableColumn colStockQuanity = new TableColumn("Số lượng");
        colStockQuanity.setCellValueFactory(new PropertyValueFactory("StockQuantity"));
        TableColumn colUnitPrice = new TableColumn("Đơn giá");
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("UnitPrice"));
        TableColumn colMinStockLevel = new TableColumn("Tồn kho tối thiểu");
        colMinStockLevel.setCellValueFactory(new PropertyValueFactory("MinStockLevel"));

        this.tbProducts.getColumns().addAll(colSKU, colProductName, colStockQuanity, colUnitPrice, colMinStockLevel);
    }

    public void applyRowStyling() {
        tbProducts.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product p, boolean bln) {
                super.updateItem(p, bln);
                if (!bln && p != null && p.getStockQuantity() < p.getMinStockLevel()) {
                    setStyle("-fx-background-color: #ff9999;");
                }
            }

        });
    }
}
