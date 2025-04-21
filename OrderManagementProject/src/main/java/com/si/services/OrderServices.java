package com.si.services;

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
            String sql = "SELECT * FROM orderdetails WHERE OrderID = " + orderID;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                OrderDetail od = new OrderDetail(rs.getInt("orderID"),
                        rs.getString("SKU"),
                        rs.getDouble("unitPrice"),
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

    public void updateOrder(Order order) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "UPDATE orders SET ShipAddress = ? WHERE OrderID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getShipAddress());
            stmt.setInt(2, order.getOrderId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateOrderDetail(List<OrderDetail> orderDetails) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "UPDATE orderdetails SET UnitPrice = ?, Quantity = ?, Discount = ? WHERE orderID = ? AND SKU = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (OrderDetail od : orderDetails) {
                stmt.setDouble(1, od.getUnitPrice());
                stmt.setInt(2, od.getQuantity());
                stmt.setDouble(3, od.getDiscount());
                stmt.setInt(4, od.getOrderID());
                stmt.setString(5, od.getSKU());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
