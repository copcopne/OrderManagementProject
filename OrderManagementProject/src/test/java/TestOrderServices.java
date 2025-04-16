
import com.si.pojo.Order;
import com.si.pojo.OrderDetail;
import com.si.pojo.OrderStatus;
import com.si.services.OrderServices;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestOrderServices {

    private static final OrderServices os = new OrderServices();

    @Test
    public void testGetOrderAll() {
        List<Order> orders = os.getOrder(null, OrderStatus.ALL);
        Assertions.assertTrue(!orders.isEmpty());
    }

    @Test
    public void testGetOrderByKW() {
        List<Order> orders = os.getOrder("1", OrderStatus.ALL);
        Assertions.assertTrue(!orders.isEmpty());
    }

    @Test
    public void testGetOrderNotFound() {
        List<Order> orders = os.getOrder("khonghople", OrderStatus.ALL);
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(0, orders.size());
    }

    @Test
    public void testGetOrderWithAllStatuses() {
        for (OrderStatus status : new OrderStatus[]{OrderStatus.DANG_GIAO, OrderStatus.DANG_XU_LY, OrderStatus.DA_GIAO}) {
            List<Order> orders = os.getOrder(null, status);
            Assertions.assertNotNull(orders);

            for (Order order : orders) {
                Assertions.assertEquals(status, order.getOrderStatus());
            }
        }
    }

    @Test
    public void getOrderDetaiWithValidID() {
        int orderID = 1;
        List<OrderDetail> details = os.getOrderDetail(orderID);
        Assertions.assertTrue(!details.isEmpty());

        //Kiểm tra khớp với order id
        for (OrderDetail od : details) {
            Assertions.assertEquals(od.getOrderID(), orderID);
        }
    }

    @Test
    public void getOrderDetailWithInvalidOrderID() {
        int orderID = -1;
        List<OrderDetail> details = os.getOrderDetail(orderID);
        Assertions.assertNotNull(details);
        Assertions.assertEquals(0, details.size());
    }

    @Test
    public void getOrderByIdWithValidID() {
        int orderID = 1;
        Order order = os.getOrderById(orderID);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(orderID, order.getOrderId());
    }

    @Test
    public void getOrderByIdWithInvalidID() {
        int orderID = -1;
        Order order = os.getOrderById(orderID);
        Assertions.assertNull(order);
    }

    @Test
    public void updateOrderStatusWithVaildD() {
        int orderID = 1;
        Order original = os.getOrderById(orderID);
        Assertions.assertNotNull(original);
        OrderStatus oldStatus = original.getOrderStatus();

        OrderStatus newStatus = (oldStatus == OrderStatus.DANG_GIAO
                ? OrderStatus.DANG_XU_LY : OrderStatus.DANG_GIAO);
        os.updateOrderStatus(orderID, newStatus);

        Order updated = os.getOrderById(orderID);
        Assertions.assertEquals(newStatus, updated.getOrderStatus());

        os.updateOrderStatus(orderID, oldStatus);
    }

    @Test
    public void updateOrderStatusWithInvaildD() {
        int orderID = -1;
        os.updateOrderStatus(orderID, OrderStatus.DANG_GIAO);
        Order result = os.getOrderById(orderID);
        Assertions.assertNull(result);
    }

    @Test
    public void testUpdateOrder() {
        int orderID = 1;
        Order order = os.getOrderById(orderID);
        Assertions.assertNotNull(order);

        String oldAddress = order.getShipAddress();
        String newAddress = "Địa chỉ test";
        order.setShipAddress(newAddress);

        os.updateOrder(order);
        Order updated = os.getOrderById(orderID);

        Assertions.assertEquals(newAddress, updated.getShipAddress());
        order.setShipAddress(oldAddress);
        os.updateOrder(order);
    }

    @Test
    public void testUpdateOrderDetail() {

        int orderID = 1;
        List<OrderDetail> original = os.getOrderDetail(orderID);
        Assertions.assertFalse(original.isEmpty());

        OrderDetail od = original.get(0);
        double oldPrice = od.getUnitPrice();
        int oldQuantity = od.getQuantity();
        double oldDiscount = od.getDiscount();

        //Chỉnh sửa giá trị
        od.setUnitPrice(oldPrice + 10.0);
        od.setQuantity(oldQuantity + 1);
        od.setDiscount(oldDiscount + 0.05);
        os.updateOrderDetail(List.of(od));

        //Đọc lại
        List<OrderDetail> updated = os.getOrderDetail(orderID);
        OrderDetail updatedOd = updated.get(0);

        //Kiểm tra
        Assertions.assertEquals(oldPrice + 10.0, updatedOd.getUnitPrice());
        Assertions.assertEquals(oldQuantity + 1, updatedOd.getQuantity());
        Assertions.assertEquals(oldDiscount + 0.05, updatedOd.getDiscount());

        //Reset lại về giá trị cũ sau test
        od.setUnitPrice(oldPrice);
        od.setQuantity(oldQuantity);
        od.setDiscount(oldDiscount);
        os.updateOrderDetail(List.of(od));
    }

}
