/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

import com.si.pojo.JdbcUtils;
import com.si.pojo.Order;
import com.si.pojo.OrderStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ttuan
 */
public class OrderServices {
    public List<Order> getOrder(String kw){
            List<Order> results = new ArrayList<>();
            try(Connection conn = JdbcUtils.getConn()) {
            String sql;
            PreparedStatement stm;
            
            if(kw == null || kw.isEmpty()){
                sql = "SELECT * FROM orders";
                stm = conn.prepareStatement(sql);
            }
            else
            {
                sql = "SELECT * FROM orders WHERE OrderID LIKE CONCAT('%', ?, '%') OR CustomerID LIKE CONCAT('%', ?, '%')";
                stm = conn.prepareStatement(sql);
                stm.setString(1, kw);
                stm.setString(2, kw);
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Order o = new Order(rs.getInt("orderId")
                        ,rs.getInt("customerID")
                        ,rs.getDate("orderDate")
                        ,rs.getString("shipAddress")
                        ,OrderStatus.converToEnum(rs.getString("OrderStatus")));
                results.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrderServices.class.getName()).log(Level.SEVERE, null, ex);
        }
            return results;
    }
}
