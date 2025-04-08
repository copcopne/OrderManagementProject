package com.si.ordermanagementproject;

import com.si.pojo.Order;
import com.si.pojo.OrderDetail;
import com.si.pojo.OrderStatus;
import com.si.services.OrderServices;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
        TableColumn colOrderId = new TableColumn("ID order");
        TableColumn colCustomerId = new TableColumn("ID khách hàng");
        TableColumn colOrderDate = new TableColumn("Ngày đặt");
        TableColumn colShipAddress = new TableColumn("Địa chỉ giao");
        TableColumn colOrderStatus = new TableColumn("Trạng thái đơn");
        
        colOrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory("customerId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory("orderDate"));
        colShipAddress.setCellValueFactory(new PropertyValueFactory("shipAddress"));
        colOrderStatus.setCellValueFactory(new PropertyValueFactory("orderStatus"));
        tbOrder.getColumns().addAll(colOrderId, colCustomerId, colOrderDate, colShipAddress, colOrderStatus);
    }
    
}
