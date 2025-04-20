package com.si.services;

import com.si.pojo.JdbcUtils;
import com.si.pojo.Product;
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
public class ProductServices {
    
    public List<Product> getProducts(String kw){
            List<Product> results = new ArrayList<>();
            try(Connection conn = JdbcUtils.getConn()) {
            String sql;
            PreparedStatement stm;
            
            if(kw == null || kw.isEmpty()){
                sql = "SELECT * FROM Products";
                stm = conn.prepareStatement(sql);
            }
            else
            {
                sql = "SELECT * FROM Products WHERE SKU LIKE CONCAT('%', ?, '%') OR ProductName LIKE CONCAT('%', ?, '%')";
                stm = conn.prepareStatement(sql);
                stm.setString(1, kw);
                stm.setString(2, kw);
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Product q = new Product(rs.getString("SKU")
                        ,rs.getString("ProductName")
                        ,rs.getInt("StockQuantity")
                        ,rs.getFloat("UnitPrice")
                        ,rs.getInt("MinStockLevel"));
                
                results.add(q);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductServices.class.getName()).log(Level.SEVERE, null, ex);
        }
            return results;
    }
    
    public boolean isStockAvailable(int orderId) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT orderdetails.SKU, SUM(Quantity) as OrderedQty, StockQuantity "
                       + "FROM orderdetails JOIN products ON orderdetails.SKU = products.SKU "
                       + "WHERE OrderID = ? GROUP BY orderdetails.SKU ";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, orderId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int orderedQty = rs.getInt("OrderedQty");
                int stock = rs.getInt("StockQuantity");
                if (orderedQty > stock) {
                    return false; // Không đủ kho
                }
            }
            return true; // Đủ kho
        } catch (SQLException ex) {
            Logger.getLogger(ProductServices.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void deductStock(int orderId){
        try (Connection conn = JdbcUtils.getConn()){
            conn.setAutoCommit(false);
            String sql = "UPDATE products "
                    + "JOIN orderdetails ON products.SKU = orderdetails.SKU "
                    + "SET products.StockQuantity = products.StockQuantity - orderdetails.Quantity "
                    + "WHERE orderdetails.OrderID = ? AND products.StockQuantity >= orderdetails.Quantity ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ProductServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
