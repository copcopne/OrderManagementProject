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
                        ,rs.getInt("StockQuanity")
                        ,rs.getFloat("UnitPrice")
                        ,rs.getInt("MinStockLevel"));
                
                results.add(q);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductServices.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("Found " + results.size() + " products.");
            return results;
    }
}
