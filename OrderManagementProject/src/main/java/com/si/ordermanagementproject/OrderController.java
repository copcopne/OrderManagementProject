package com.si.ordermanagementproject;

import com.si.pojo.Order;
import com.si.pojo.OrderDetail;
import com.si.pojo.OrderStatus;
import com.si.services.OrderServices;
import com.si.services.ProductServices;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OrderController implements Initializable{
    @FXML private TableView<Order> tbOrder;
    @FXML private TableView<OrderDetail> tbOrderDetail;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<OrderStatus> cbStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tbOrder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadData(null,OrderStatus.ALL);
        loadColumns();
        
        cbStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        cbStatus.setValue(OrderStatus.ALL);
        
        this.txtSearch.textProperty().addListener((obs,oldText,newText) -> {
            loadData(newText,this.cbStatus.getValue());
        });
        this.cbStatus.valueProperty().addListener((obs, oldStatus, newStatus) -> {
            loadData(txtSearch.getText(), newStatus);
        });
    }
    
    public void loadData(String kw, OrderStatus s){
        OrderServices o = new OrderServices();
        List<Order> orders = o.getOrder(kw,s);
        tbOrder.setItems(FXCollections.observableList(orders));
        
    }
    public void loadColumns(){
    TableColumn colOrderId = new TableColumn<>("ID order");
    TableColumn colCustomerId = new TableColumn<>("ID khách hàng");
    TableColumn colOrderDate = new TableColumn<>("Ngày đặt");
    TableColumn colShipAddress = new TableColumn<>("Địa chỉ giao");
    TableColumn colOrderStatus = new TableColumn<>("Trạng thái đơn");

    colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    colShipAddress.setCellValueFactory(new PropertyValueFactory<>("shipAddress"));
    colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
    
    // Thêm cột chứa nút xem chi tiết 
    TableColumn<Order, Void> colViewDetail = new TableColumn<>("");
    colViewDetail.setCellFactory(param -> new TableCell<>() {
        private final Button btnDetail = new Button("Xem chi tiết");

        {
            btnDetail.setOnAction(event -> {
                Order order = getTableView().getItems().get(getIndex());
                showOrderDetails(order.getOrderId());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : btnDetail);
        }
    });
    
    //thêm cột chứa nút xác nhận
    TableColumn<Order, Void> colConfirm = new TableColumn<>("");
    colConfirm.setCellFactory(param -> new TableCell<>() {
        private final Button btnConfirm = new Button("Xác nhận");
        OrderServices o = new OrderServices();
        {
            btnConfirm.setOnAction(event -> {
                Order order = getTableView().getItems().get(getIndex());
                if(order.getOrderStatus()==OrderStatus.DANG_XU_LY){
                    ProductServices p = new ProductServices();
                    int orderID = order.getOrderId();
                    if(p.isStockAvailable(orderID)){
                        p.deductStock(orderID);
                        o.updateOrderStatus(orderID, OrderStatus.DANG_GIAO);
                        loadData(txtSearch.getText(), cbStatus.getValue());
                    }
                }
            });
            
        }
        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || getTableView().getItems().get(getIndex()).getOrderStatus() != OrderStatus.DANG_XU_LY) {
                setGraphic(null);  // Ẩn nút nếu trạng thái không phải "Đang xử lý"
            } else {
                setGraphic(btnConfirm);
            }
    }
});

    tbOrder.getColumns().addAll(colOrderId, colCustomerId, colOrderDate, colShipAddress, colOrderStatus, colViewDetail, colConfirm);
}
    public void showOrderDetails(int orderID) {
    Stage stage = new Stage();
    stage.setTitle("Chi tiết đơn hàng #" + orderID);
    
    TableView<OrderDetail> tableView = new TableView<>();
    TableColumn skuCol = new TableColumn<>("SKU");
    TableColumn unitPriceCol = new TableColumn<>("Đơn giá");
    TableColumn quantityCol = new TableColumn<>("Số lượng");
    TableColumn discountCol = new TableColumn<>("Giảm giá");
    skuCol.setCellValueFactory(new PropertyValueFactory<>("SKU"));
    
    tableView.getColumns().addAll(skuCol, unitPriceCol, quantityCol, discountCol);
    unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));

    // Lấy dử liệu của Order Detail
    OrderServices os = new OrderServices();
    List<OrderDetail> orderDetails = os.getOrderDetail(orderID);
    ObservableList<OrderDetail> data = FXCollections.observableArrayList(orderDetails);
    tableView.setItems(data);

    // Tạo Scene và hiển thị
    Scene scene = new Scene(tableView, 600, 400);
    stage.setScene(scene);
    stage.show();
    }
}
    
