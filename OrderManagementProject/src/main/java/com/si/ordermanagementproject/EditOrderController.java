package com.si.ordermanagementproject;

import com.si.pojo.Order;
import com.si.pojo.OrderDetail;
import com.si.pojo.OrderStatus;
import com.si.services.OrderServices;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

/**
 *
 * @author ttuan
 */
public class EditOrderController implements Initializable {

    @FXML
    private TextField txtShipAddress;
    @FXML
    private TableView<OrderDetail> tbOrderDetails;
    Order currentOrder;
    OrderServices os = new OrderServices();
    int orderID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbOrderDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadColumns();
        tbOrderDetails.setEditable(true);
    }

    public void loadData(int orderID) {
        this.currentOrder = os.getOrderById(orderID);
        if (this.currentOrder == null) {
            return;
        }
        txtShipAddress.setText(currentOrder.getShipAddress());
        List<OrderDetail> orderDetails = os.getOrderDetail(orderID);
        ObservableList<OrderDetail> data = FXCollections.observableArrayList(orderDetails);
        tbOrderDetails.setItems(data);
    }

    public void loadColumns() {
        TableColumn<OrderDetail, String> colSKU = new TableColumn<>("Mã SKU");
        TableColumn<OrderDetail, Double> colUnitPrice = new TableColumn<>("Đơn giá");
        TableColumn<OrderDetail, Integer> colQuantity = new TableColumn<>("Số lượng");
        TableColumn<OrderDetail, Double> colDiscount = new TableColumn<>("Giảm giá");

        colSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        // Cho phép chỉnh sửa
        colUnitPrice.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        colUnitPrice.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            od.setUnitPrice(event.getNewValue());
        });

        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        colQuantity.setOnEditCommit(event -> {
            OrderDetail detail = event.getRowValue();
            detail.setQuantity(event.getNewValue());
        });

        colDiscount.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        colDiscount.setOnEditCommit(event -> {
            OrderDetail detail = event.getRowValue();
            detail.setDiscount(event.getNewValue());
        });

        tbOrderDetails.getColumns().addAll(colSKU, colUnitPrice, colQuantity, colDiscount);
    }

    @FXML
    private void handleSave() {
        this.currentOrder.setShipAddress(txtShipAddress.getText());

        // Cập nhật địa chỉ giao hàng
        os.updateOrder(currentOrder);

        // Cập nhật chi tiết đơn hàng
        os.updateOrderDetail(tbOrderDetails.getItems());

        Stage stage = (Stage) txtShipAddress.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) txtShipAddress.getScene().getWindow();
        stage.close();
    }
}
