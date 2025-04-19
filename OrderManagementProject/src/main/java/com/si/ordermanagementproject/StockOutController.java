/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.si.ordermanagementproject;

import com.si.pojo.Product;
import com.si.services.ProductServices;
import com.si.services.ValidationServices;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author copcopne
 */
public class StockOutController implements Initializable {

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
    private Button addBtn;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button saveEditProductBtn;
    @FXML
    private Button cancelEditNewProductBtn;

    boolean isEditing = false;
    List<Product> currentProducts = new ArrayList<>();
    List<Product> updatedProducts = new ArrayList<>();
    Map<Product, Integer> importAmounts = new HashMap<>();
    ProductServices s = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new ProductServices();
        toggleInputs();
        loadColumns();
        addBtn.setDisable(true);
        SKUcb.setEditable(true);

        currentProducts = s.getProducts(null);
        loadComboBoxData(currentProducts);
        SKUcb.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty() || newText.isBlank()) {
                clearInputs();
                toggleInputs();
            }
            List<Product> filtered = s.getProducts(newText, currentProducts);
            loadComboBoxData(filtered);

            SKUcb.getEditor().setText(newText);
        });

        SKUcb.setConverter(new javafx.util.StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product == null ? "" : product.getSKU();
            }

            @Override
            public Product fromString(String string) {
                for (Product p : SKUcb.getItems()) {
                    if (p.getSKU().equals(string)) {
                        return p;
                    }
                }
                return null;
            }
        });

    }

    public void loadComboBoxData(List<Product> products) {
        SKUcb.getItems().clear();
        SKUcb.getItems().addAll(products);
    }

    public void loadColumns() {
        TableColumn colId = new TableColumn("SKU");
        colId.setCellValueFactory(new PropertyValueFactory("SKU"));
        colId.setPrefWidth(100);

        TableColumn colName = new TableColumn("Tên SP");
        colName.setCellValueFactory(new PropertyValueFactory("productName"));
        colName.setPrefWidth(300);

        TableColumn colPrice = new TableColumn("Đơn giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colPrice.setPrefWidth(75);

        TableColumn colQuantity = new TableColumn("Tồn kho");
        colQuantity.setCellValueFactory(new PropertyValueFactory("stockQuantity"));
        colQuantity.setPrefWidth(75);

        TableColumn<Product, String> colMinStock = new TableColumn<>("Số lượng xuất ra");
        colMinStock.setCellValueFactory(product -> {
            Integer amount = importAmounts.get(product.getValue());
            return new SimpleStringProperty(amount != null ? amount.toString() : "0");
        });
        colMinStock.setPrefWidth(100);

        TableColumn<Product, Void> colAction1 = new TableColumn<>();
        colAction1.setCellFactory(col -> new TableCell<Product, Void>() {
            Button editBtn = new Button("Chỉnh sửa");

            {
                editBtn.setOnAction(evt -> {
                    TableRow currentRow = getTableRow();
                    Product selectedProduct = (Product) currentRow.getItem();
                    for (Product p : SKUcb.getItems()) {
                        if (p.getSKU().equals(selectedProduct.getSKU())) {
                            SKUcb.getSelectionModel().select(p);
                            break;
                        }
                    }
                    Integer amount = importAmounts.get(selectedProduct);
                    setInputs(selectedProduct, amount);
                    editProduct();

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

        TableColumn<Product, Void> colAction2 = new TableColumn<>();
        colAction2.setCellFactory(col -> new TableCell<Product, Void>() {
            Button delBtn = new Button("Xóa");

            {
                delBtn.setOnAction(evt -> {
                    Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn xóa không?");
                    Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        TableRow currentRow = getTableRow();
                        Product selectedProduct = (Product) currentRow.getItem();
                        Product matched = updatedProducts.stream().filter(p -> p.getSKU().equals(selectedProduct.getSKU())).findFirst().orElse(null);
                        if (matched != null) {
                            updatedProducts.remove(matched);
                            Utils.getAlert("Xóa thành công.").show();
                            loadTableData();
                        }
                    }
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

        this.table.getColumns().addAll(colId, colName, colPrice, colQuantity, colMinStock, colAction1, colAction2);

    }

    public void loadTableData() {
        clearInputs();
        this.table.getItems().clear();
        this.table.setItems(FXCollections.observableArrayList(updatedProducts));
    }

    public void process() {
        if (updatedProducts.isEmpty()) {
            Utils.getAlert("Bạn chưa thực hiện xuất hàng.").show();
            return;
        }
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn xuất hàng không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                for (Product product : updatedProducts) {
                    if (s.getProduct(product.getSKU()) != null) {
                        boolean rs = s.deductStock(product, importAmounts.get(product));
                        if (rs == false) {
                            Utils.getAlert("Lỗi khi thực hiện xuất hàng với SKU: " + product.getSKU()).showAndWait();
                        }
                    }
                }
            } catch (Exception e) {
                Utils.getAlert("Lỗi xảy ra khi xuất hàng: " + e.getMessage()).show();
                return;
            }
            Utils.getAlert("Xuất hàng thành công.").showAndWait();
            this.ok.getScene().getWindow().hide();
        }
    }

    public void cancel() {
        // Hủy thực hiện việc xuất hàng, nếu đang trong quá trình nhập thì hiện prompt thông báo
        if (!updatedProducts.isEmpty()) {
            Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn hủy việc xuất hàng không?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cancel.getScene().getWindow().hide();
            }
            return;
        }
        cancel.getScene().getWindow().hide();
    }

    public void validateAmount(Integer finalAmount) throws IllegalArgumentException {
        String raw = amountTxt.getText();
        if (raw.isBlank()) {
            amountTxt.requestFocus();
            throw new IllegalArgumentException("Số lượng hàng cần xuất không được để trống.");
        }

        int inputNum;
        try {
            inputNum = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            amountTxt.requestFocus();
            throw new NumberFormatException("Số lượng xuất hàng phải là số nguyên hợp lệ.");
        }

        int toCheck = (finalAmount != null) ? finalAmount : inputNum;

        int stock = SKUcb.getSelectionModel().getSelectedItem().getStockQuantity();

        if (toCheck <= 0) {
            amountTxt.requestFocus();
            throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0.");
        }
        if (toCheck > stock) {
            amountTxt.requestFocus();
            throw new IllegalArgumentException("Số lượng xuất vượt quá tồn kho (" + stock + ").");
        }
    }

    public void deductHandler() {
        // Xuất hàng
        Product selectedProduct = SKUcb.getSelectionModel().getSelectedItem();
        int stock = selectedProduct.getStockQuantity();
        try {
            String currentAmount = amountTxt.getText();
            ValidationServices.validateExportQuantity(currentAmount, stock);
        } catch (IllegalArgumentException e) {
            Utils.getAlert(e.getMessage()).show();
            return;
        }
        Product product = updatedProducts.stream().filter(p -> p.getSKU().equals(selectedProduct.getSKU())).findFirst().orElse(null);
        if (product == null) {
            updatedProducts.add(selectedProduct);
            importAmounts.put(selectedProduct, Integer.valueOf(amountTxt.getText()));
        } else {
            try {
                int currentAmount = importAmounts.get(selectedProduct);
                int newAmount = currentAmount + Integer.parseInt(amountTxt.getText());
                ValidationServices.validateExportQuantity(newAmount, stock);
                importAmounts.put(selectedProduct, newAmount);
            } catch (IllegalArgumentException e) {
                Utils.getAlert("Lỗi: " + e.getMessage()).show();
                return;
            }
        }
        addBtn.setDisable(true);
        toggleInputs();
        loadTableData();
    }

    public void saveEditedProductHandler() {
        if (isEditing) {
            Product selectedProduct = SKUcb.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    validateAmount(null);
                } catch (IllegalArgumentException e) {
                    Utils.getAlert(e.getMessage()).show();
                    return;
                }
                int newAmount = Integer.parseInt(amountTxt.getText());
                importAmounts.put(selectedProduct, newAmount);
                isEditing = false;
                toggleInputs();
                clearInputs();
                loadTableData();
            }
        }
    }

    public void cancelEditHandler() {
        isEditing = false;
        toggleInputs();
        clearInputs();
        loadComboBoxData(currentProducts);
    }

    public void toggleInputs() {
        if (isEditing) {
            // Khi chỉnh sửa sản phẩm
            addBtn.setDisable(true);
            SKUcb.setDisable(true);
            SKUcb.setEditable(false);
            nameTxt.setDisable(true);
            priceTxt.setDisable(true);
            minStockTxt.setDisable(true);
            amountTxt.setDisable(false); // Chỉ cho nhập lại số lượng
            saveEditProductBtn.setDisable(false);
            saveEditProductBtn.setVisible(true);
            cancelEditNewProductBtn.setVisible(true);
        } else {
            // Trạng thái bình thường
            addBtn.setDisable(true);
            SKUcb.setDisable(false);
            SKUcb.setEditable(true);
            nameTxt.setDisable(true);
            priceTxt.setDisable(true);
            minStockTxt.setDisable(true);
            amountTxt.setDisable(true);
            saveEditProductBtn.setDisable(true);
            saveEditProductBtn.setVisible(false);
            cancelEditNewProductBtn.setVisible(false);
        }

    }

    public void clearInputs() {
        SKUcb.setValue(null);
        SKUcb.getEditor().clear();
        nameTxt.setText("");
        priceTxt.setText("");
        stockTxt.setText("");
        minStockTxt.setText("");
        amountTxt.setText("");
        amountTxt.setDisable(!isEditing);

    }

    public void editProduct() {
        isEditing = true;
        toggleInputs();
        amountTxt.requestFocus();
    }

    public void setInputs(Product selectedProduct, Integer amount) {
        nameTxt.setText(selectedProduct.getProductName());
        priceTxt.setText(String.valueOf(selectedProduct.getUnitPrice()));
        stockTxt.setText(String.valueOf(selectedProduct.getStockQuantity()));
        minStockTxt.setText(String.valueOf(selectedProduct.getMinStockLevel()));
        if (amount != null) {
            amountTxt.setText(String.valueOf(amount));
        } else {
            amountTxt.setText("");
        }
    }

    public void selectHandler() {
        try {
            Product selectedProduct = SKUcb.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                amountTxt.setDisable(false);
                addBtn.setDisable(false);
                setInputs(selectedProduct, null);
            }
        } catch (ClassCastException ex) {
            System.out.println(ex.getMessage());
            clearInputs();
        }
    }
}
