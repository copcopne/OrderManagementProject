/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.Product;
import com.si.services.ProductServices;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author copcopne
 */
public class StockInController implements Initializable {

    @FXML
    private ComboBox<Product> SKUcb;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField stockTxt;
    @FXML
    private TextField minStockTxt;
    @FXML
    private TextField amountTxt;
    @FXML
    private TableView<Product> table;
    @FXML
    private Button addNewBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button saveBtn;

    boolean isCreating = false;
    List<Product> currentProducts = new ArrayList<>();
    List<Product> updatedProducts = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameTxt.setDisable(true);
        priceTxt.setDisable(true);
        stockTxt.setDisable(true);
        minStockTxt.setDisable(true);
        amountTxt.setDisable(true);
        saveBtn.setDisable(true);
        saveBtn.setVisible(false);
        loadColumns();

        ProductServices p = new ProductServices();
        currentProducts = p.getProducts(null);
        SKUcb.getItems().addAll(currentProducts);
        loadComboBoxData();

    }

    public void loadComboBoxData() {
        SKUcb.getItems().addAll(currentProducts);
    }

    public void loadColumns() {
        TableColumn colId = new TableColumn("SKU");
        colId.setCellValueFactory(new PropertyValueFactory("SKU"));
        colId.setPrefWidth(200);

        TableColumn colName = new TableColumn("Tên SP");
        colName.setCellValueFactory(new PropertyValueFactory("productName"));
        colName.setPrefWidth(300);

        TableColumn colPrice = new TableColumn("Đơn giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colPrice.setPrefWidth(100);

        TableColumn colQuantity = new TableColumn("Số lượng nhập");
        colQuantity.setCellValueFactory(new PropertyValueFactory("stockQuantity"));
        colQuantity.setPrefWidth(150);

        TableColumn colMinStock = new TableColumn("Tồn kho tối thiểu");
        colMinStock.setCellValueFactory(new PropertyValueFactory("MinStockLevel"));
        colMinStock.setPrefWidth(150);

        this.table.getColumns().addAll(colId, colName, colPrice, colQuantity, colMinStock);

    }

    public void loadTableData() {
        this.table.setItems(FXCollections.observableArrayList(updatedProducts));
    }

    public void process() {
        // Xử lý nhập hàng vào database
    }

    public void cancel() {
        // Hủy thực hiện việc nhập hàng, nếu đang trong quá trình nhập thì hiện prompt thông báo
    }

    public void addHandler() {
        // Nhập thêm hàng từ products có sẵn
    }

    public void createHandler() {
        Product product = new Product(SKUcb.getEditor().getText(),
                nameTxt.getText(),
                Integer.parseInt(amountTxt.getText()),
                Float.parseFloat(priceTxt.getText()),
                Integer.parseInt(minStockTxt.getText()));
        updatedProducts.add(product);
        loadTableData();

        isCreating = false;
        toggleInputs();
        clearInputs();
        loadComboBoxData();
    }

    public void enableCreate() {
        clearInputs();
        SKUcb.getItems().clear();

        isCreating = true;
        stockTxt.setText("0");
        toggleInputs();
        SKUcb.requestFocus();

    }

    public void toggleInputs() {
        SKUcb.setEditable(isCreating);
        nameTxt.setDisable(!isCreating);
        priceTxt.setDisable(!isCreating);
        minStockTxt.setDisable(!isCreating);
        amountTxt.setDisable(!isCreating);
        saveBtn.setDisable(!isCreating);
        saveBtn.setVisible(isCreating);
    }

    public void clearInputs() {
        amountTxt.setDisable(true);
        SKUcb.setValue(null);
        SKUcb.getEditor().clear();
        nameTxt.setText("");
        priceTxt.setText("");
        stockTxt.setText("");
        minStockTxt.setText("");
        amountTxt.setText("");
    }

    public void editProduct() {
        // Lấy product ở hàng được ấn nút edit truyền vào setInputs() và request focus vào ô số lượng nhập
    }

    public void setInputs(Product selectedProduct, Integer amount) {
        nameTxt.setText(selectedProduct.getProductName());
        priceTxt.setText(String.valueOf(selectedProduct.getUnitPrice()));
        stockTxt.setText(String.valueOf(selectedProduct.getStockQuantity()));
        minStockTxt.setText(String.valueOf(selectedProduct.getMinStockLevel()));
        if (amount != null)
            amountTxt.setText(String.valueOf(amount));
    }

    public void selectHandler() {
        if (isCreating != true) {
            try {
                Product selectedProduct = SKUcb.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    amountTxt.setDisable(false);
                    setInputs(selectedProduct, null);
                }
            } catch (ClassCastException ex) {
                System.out.println(ex.getMessage());
                clearInputs();
            }
        }
    }
}
