package com.si.services;

import com.si.ordermanagementproject.Utils;
import com.si.pojo.JdbcUtils;
import com.si.pojo.Order;
import com.si.pojo.OrderDetail;
import com.si.pojo.OrderStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ttuan
 */
public class OrderServices {

    public List<Order> getOrder(String kw, OrderStatus s) {
        List<Order> results = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM orders WHERE 1=1"
                    + (s == OrderStatus.ALL ? "" : " AND OrderStatus = ?")
                    + (kw == null || kw.isEmpty() ? "" : " AND (OrderID LIKE CONCAT('%', ?, '%') OR CustomerID LIKE CONCAT('%', ?, '%'))");
            PreparedStatement stm = conn.prepareStatement(sql);
            int paramIndex = 0;

            if (s != null && s != OrderStatus.ALL) {
                stm.setString(++paramIndex, s.toString());
            }

            if (kw != null && !kw.isEmpty()) {
                stm.setString(++paramIndex, kw);
                stm.setString(++paramIndex, kw);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Order o = new Order(rs.getInt("orderId"),
                        rs.getInt("customerID"),
                        rs.getDate("orderDate"),
                        rs.getString("shipAddress"),
                        OrderStatus.converToEnum(rs.getString("OrderStatus"))
                );
                results.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    public List<OrderDetail> getOrderDetail(int orderID) {
        List<OrderDetail> results = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM orderdetails WHERE OrderID = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, orderID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                OrderDetail od = new OrderDetail(rs.getInt("orderID"),
                        rs.getString("SKU"),
                        rs.getInt("quantity"),
                        rs.getDouble("discount")
                );
                results.add(od);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE orders SET OrderStatus = ? WHERE OrderID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus.toString());
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Order getOrderById(int orderId) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM orders WHERE OrderID = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, orderId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("orderId"),
                        rs.getInt("customerID"),
                        rs.getDate("orderDate"),
                        rs.getString("shipAddress"),
                        OrderStatus.converToEnum(rs.getString("OrderStatus"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean updateOrder(Order order) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "UPDATE orders SET ShipAddress = ? WHERE OrderID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getShipAddress());
            stmt.setInt(2, order.getOrderId());
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào bị ảnh hưởng không
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateOrderDetail(List<OrderDetail> orderDetails) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "UPDATE orderdetails SET Quantity = ?, Discount = ? WHERE orderID = ? AND SKU = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (OrderDetail od : orderDetails) {
                stmt.setInt(1, od.getQuantity());
                stmt.setDouble(2, od.getDiscount());
                stmt.setInt(3, od.getOrderID());
                stmt.setString(4, od.getSKU());
                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            for (int res : result) {
                if (res == Statement.EXECUTE_FAILED) {
                    return false; // Nếu có ít nhất một câu lệnh không thành công
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
