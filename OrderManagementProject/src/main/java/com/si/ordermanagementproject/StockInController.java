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
    private Button addNewProductBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;
    @FXML
    private Button saveNewProductBtn;
    @FXML
    private Button cancelAddNewProductBtn;

    private javafx.beans.value.ChangeListener<String> comboBoxListener;
    boolean isCreating = false;
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

        currentProducts = s.getProducts(null);
        SKUcb.setEditable(true);
        loadComboBoxData(currentProducts);
        comboBoxListener = (obs, oldText, newText) -> {
            if (newText.isEmpty() || newText.isBlank()) {
                clearInputs();
                toggleInputs();
            }
            List<Product> filtered = s.getProducts(newText, currentProducts);
            loadComboBoxData(filtered);
            SKUcb.getEditor().setText(newText);
        };

        SKUcb.getEditor().textProperty().addListener(comboBoxListener);

        SKUcb.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectHandler();
            }
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

        TableColumn<Product, String> colMinStock = new TableColumn<>("Số lượng nhập");
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
                    if (isCreating) {
                        Utils.getAlert("Vui lòng hoàn thành thêm sản phẩm trước!").showAndWait();
                        return;
                    }
                    TableRow currentRow = getTableRow();
                    Product selectedProduct = (Product) currentRow.getItem();
//                    for (Product p : SKUcb.getItems()) {
//                        if (p.getSKU().equals(selectedProduct.getSKU())) {
//                            SKUcb.getSelectionModel().select(p);
//                            break;
//                        }
//                    }
// Load các sản phẩm đã sửa vào combobox để có thể xử lý
                    loadComboBoxData(updatedProducts);
                    SKUcb.getSelectionModel().select(selectedProduct);

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
                    if (isCreating) {
                        Utils.getAlert("Vui lòng hoàn thành thêm sản phẩm trước!").showAndWait();
                        return;
                    }
                    Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn xóa không?");
                    Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        TableRow currentRow = getTableRow();
                        Product selectedProduct = (Product) currentRow.getItem();
                        Product matched = updatedProducts.stream().filter(p -> p.getSKU().equals(selectedProduct.getSKU())).findFirst().orElse(null);
                        if (matched != null) {
                            updatedProducts.remove(matched);
                            Utils.getAlert("Xóa thành công.").show();
                            if (isEditing) {
                                isEditing = false;
                            }
                            loadTableData();
                            clearInputs();
                            toggleInputs();
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
            Utils.getAlert("Bạn chưa thực hiện nhập hàng.").show();
            return;
        }
        Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn nhập hàng không?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                for (Product product : updatedProducts) {
                    if (s.getProduct(product.getSKU()) != null) {
                        boolean rs = s.addStock(product, importAmounts.get(product));
                        if (rs == false) {
                            Utils.getAlert("Lỗi khi thực hiện nhập hàng với SKU: " + product.getSKU()).showAndWait();
                        }
                    } else {
                        Product newP = new Product(product.getSKU(),
                                product.getProductName(),
                                importAmounts.get(product),
                                product.getUnitPrice(),
                                product.getMinStockLevel());
                        boolean rs = s.addProduct(newP);
                        if (rs == false) {
                            Utils.getAlert("Lỗi khi thực hiện nhập hàng với SKU: " + product.getSKU()).showAndWait();
                        }
                    }
                }
            } catch (Exception e) {
                Utils.getAlert("Lỗi xảy ra khi nhập hàng: " + e.getMessage()).show();
                return;
            }
            Utils.getAlert("Nhập hàng thành công.").showAndWait();
            this.ok.getScene().getWindow().hide();
        }
    }

    public void cancel() {
        // Hủy thực hiện việc nhập hàng, nếu đang trong quá trình nhập thì hiện prompt thông báo
        if (!updatedProducts.isEmpty()) {
            Alert confirm = Utils.getConfirmAlert("Bạn có chắc muốn hủy việc nhập hàng không?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cancel.getScene().getWindow().hide();
            }
            return;
        }
        cancel.getScene().getWindow().hide();
    }

    public void addHandler() {
        // Nhập thêm hàng từ products có sẵn
        Product selectedProduct = SKUcb.getValue();
        try {
            String amt = amountTxt.getText();
            ValidationServices.validateImportQuantity(amt);

            Product product = updatedProducts.stream().filter(p -> p.getSKU().equals(selectedProduct.getSKU())).findFirst().orElse(null);
            if (product == null) {
                updatedProducts.add(selectedProduct);
                importAmounts.put(selectedProduct, Integer.valueOf(amountTxt.getText()));
            } else {
                int currentAmount = importAmounts.get(selectedProduct);
//                importAmounts.put(selectedProduct, Integer.parseInt(amountTxt.getText()) + currentAmount);

// Validate số lượng nhập nếu sản phẩm đã nằm trong danh sách nhập từ trước
                int newAmount = currentAmount + Integer.parseInt(amountTxt.getText());
                ValidationServices.validateImportQuantity(String.valueOf(newAmount));
                importAmounts.put(selectedProduct, newAmount);
            }
            addBtn.setDisable(true);
            toggleInputs();
            loadTableData();

        } catch (IllegalArgumentException e) {
            Utils.getAlert(e.getMessage()).show();
        }
    }

    public void saveEditedProductHandler() {
        if (isEditing) {
            Product selectedProduct = SKUcb.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    String amt = amountTxt.getText();
                    ValidationServices.validateImportQuantity(amt);
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
        } else {
            createHandler();
        }
    }

    public void createHandler() {
        try {
            ValidationServices.validateNewProduct(SKUcb.getEditor().getText(),
                    nameTxt.getText(),
                    priceTxt.getText(),
                    minStockTxt.getText());
            String amt = amountTxt.getText();
            ValidationServices.validateImportQuantity(amt);
        } catch (IllegalArgumentException e) {
            Utils.getAlert(e.getMessage()).show();
            return;
        }

        Product product;
        product = new Product(SKUcb.getEditor().getText(),
                nameTxt.getText(),
                0,
                Float.parseFloat(priceTxt.getText()),
                Integer.parseInt(minStockTxt.getText()));
        updatedProducts.add(product);
        importAmounts.put(product, Integer.valueOf(amountTxt.getText()));
        loadTableData();

        isCreating = false;
        toggleInputs();
        clearInputs();
        SKUcb.getEditor().textProperty().addListener(comboBoxListener);
        loadComboBoxData(currentProducts);
    }

    public void enableCreate() {
        SKUcb.getEditor().textProperty().removeListener(comboBoxListener);
        clearInputs();
        // SKUcb.getItems().clear(); lam fromString bi sai

        isCreating = true;
        stockTxt.setText("0");
        SKUcb.getItems().clear();
        toggleInputs();
        SKUcb.requestFocus();

    }

    public void cancelAddHandler() {
        isCreating = false;
        isEditing = false;
        toggleInputs();
        clearInputs();
        loadComboBoxData(currentProducts);
        SKUcb.getEditor().textProperty().addListener(comboBoxListener);
    }

    public void toggleInputs() {
        if (isCreating) {
            // Khi tạo mới sản phẩm
            addBtn.setDisable(true);
            addNewProductBtn.setDisable(true);
            SKUcb.setEditable(true);
            SKUcb.setDisable(false);
            nameTxt.setDisable(false);
            priceTxt.setDisable(false);
            minStockTxt.setDisable(false);
            amountTxt.setDisable(false);
            saveNewProductBtn.setDisable(false);
            saveNewProductBtn.setVisible(true);
            cancelAddNewProductBtn.setVisible(true);
        } else if (isEditing) {
            // Khi chỉnh sửa sản phẩm
            addBtn.setDisable(true);
            addNewProductBtn.setDisable(true);
            SKUcb.setDisable(true); // Không cho chọn SKU khi đang sửa
            SKUcb.setEditable(false);
            nameTxt.setDisable(true);
            priceTxt.setDisable(true);
            minStockTxt.setDisable(true);
            amountTxt.setDisable(false); // Chỉ cho nhập lại số lượng
            saveNewProductBtn.setDisable(false);
            saveNewProductBtn.setVisible(true);
            cancelAddNewProductBtn.setVisible(true);
        } else {
            // Trạng thái bình thường
            addBtn.setDisable(true);
            addNewProductBtn.setDisable(false);
            SKUcb.setDisable(false);
            SKUcb.setEditable(true);
            nameTxt.setDisable(true);
            priceTxt.setDisable(true);
            minStockTxt.setDisable(true);
            amountTxt.setDisable(true);
            saveNewProductBtn.setDisable(true);
            saveNewProductBtn.setVisible(false);
            cancelAddNewProductBtn.setVisible(false);
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
        amountTxt.setDisable(!(isCreating || isEditing));

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
        if (isCreating != true) {
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
}
