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

    public List<Product> getProducts(String kw) {
        List<Product> results = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql;
            PreparedStatement stm;

            if (kw == null || kw.isEmpty()) {
                sql = "SELECT * FROM Products";
                stm = conn.prepareStatement(sql);
            } else {
                sql = "SELECT * FROM Products WHERE SKU LIKE CONCAT('%', ?, '%') OR ProductName LIKE CONCAT('%', ?, '%')";
                stm = conn.prepareStatement(sql);
                stm.setString(1, kw);
                stm.setString(2, kw);
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Product q = new Product(rs.getString("SKU"),
                         rs.getString("ProductName"),
                         rs.getInt("StockQuantity"),
                         rs.getFloat("UnitPrice"),
                         rs.getInt("MinStockLevel"));

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

    public void deductStock(int orderId) {
        try (Connection conn = JdbcUtils.getConn()) {
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

    public void deductStock(Product p, int num) {

    }

    public Product getProduct(String SKU) {
        Product product = null;
        String query = "SELECT * FROM Products WHERE SKU = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query);) {
            stm.setString(1, SKU);

            try (ResultSet rs = stm.executeQuery();) {
                if (rs.next()) {
                    product = new Product(rs.getString("SKU"),
                            rs.getString("ProductName"),
                            rs.getInt("StockQuantity"),
                            rs.getFloat("UnitPrice"),
                            rs.getInt("MinStockLevel"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Database Error:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return product;
    }

    public boolean addStock(Product p, int num) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "UPDATE Products "
                    + "SET StockQuantity = StockQuantity + ? "
                    + "WHERE SKU = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, num);
            stmt.setString(2, p.getSKU());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create product with this SKU: " + p.getSKU());
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public boolean addProduct(Product p) {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "INSERT into Products (SKU, ProductName, StockQuantity, UnitPrice, MinStockLevel) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getSKU());
            stmt.setString(2, p.getProductName());
            stmt.setInt(3, p.getStockQuantity());
            stmt.setFloat(4, p.getUnitPrice());
            stmt.setInt(5, p.getMinStockLevel());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create product with this SKU: " + p.getSKU());
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}
