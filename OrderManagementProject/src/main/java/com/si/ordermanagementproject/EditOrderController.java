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
        TableColumn<OrderDetail, Integer> colQuantity = new TableColumn<>("Số lượng");
        TableColumn<OrderDetail, Double> colDiscount = new TableColumn<>("Giảm giá");

        colSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        // Cho phép chỉnh sửa
        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        colQuantity.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            Integer newValue = event.getNewValue();
            if (newValue == null || newValue <= 0) {
                Utils.getAlert("Số lượng không hợp lệ! Vui lòng nhập số nguyên > 0.").show();
                tbOrderDetails.refresh();
                return;
            }
            od.setQuantity(newValue);
        });

        colDiscount.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        colDiscount.setOnEditCommit(event -> {
            OrderDetail od = event.getRowValue();
            Double newValue = event.getNewValue();
            if (newValue == null || newValue < 0 || newValue > 1) {
                Utils.getAlert("Giảm giá không hợp lệ! Vui lòng nhập số thực trong khoảng 0 - 1").show();
                tbOrderDetails.refresh();
                return;
            }
            od.setDiscount(newValue);
        });

        tbOrderDetails.getColumns().addAll(colSKU, colQuantity, colDiscount);
    }

    @FXML
    private void handleSave() {
        String shipAddress = txtShipAddress.getText().trim();

        if (shipAddress.isEmpty()) {
            Utils.getAlert("Địa chỉ giao hàng không được để trống").show();
            return;
        }
        this.currentOrder.setShipAddress(txtShipAddress.getText());

        // Cập nhật địa chỉ giao hàng
        boolean isOrderUpdated = os.updateOrder(currentOrder);

        // Cập nhật chi tiết đơn hàng
        boolean isOrderDetailUpdated = os.updateOrderDetail(tbOrderDetails.getItems());
        if (!isOrderUpdated || !isOrderDetailUpdated) {
            Utils.getAlert("Cập nhật không thành công").show();
            return;
        }
        Utils.getAlert("Cập nhật thành công").show();
        Stage stage = (Stage) txtShipAddress.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) txtShipAddress.getScene().getWindow();
        stage.close();
    }
}
