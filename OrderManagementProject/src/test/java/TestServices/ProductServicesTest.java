/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.pojo.JdbcUtils;
import com.si.pojo.Product;
import com.si.services.ProductServices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

/**
 *
 * @author copcopne
 */
public class ProductServicesTest {

    private static final ProductServices service = new ProductServices();
    private Product test = new Product("PTEST", "Sản phẩm test", 50, 5000, 10);

    @BeforeEach
    public void setupData() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String query = "INSERT into Products (SKU, ProductName, StockQuantity, UnitPrice, MinStockLevel) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, test.getSKU());
            stmt.setString(2, test.getProductName());
            stmt.setInt(3, test.getStockQuantity());
            stmt.setFloat(4, test.getUnitPrice());
            stmt.setInt(5, test.getMinStockLevel());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create product with this SKU: " + test.getSKU());
            }
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String query = "DELETE FROM Products WHERE SKU = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, test.getSKU());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete product with this SKU: " + test.getSKU());
            }
        }
    }

    @Test
    public void testAddStock_Success() {
        int[] amounts = {1, 100, 1000};
        for (int amount : amounts) {
            assertTrue(service.addStock(test, amount),
                    "Expected trả về true (nhập thành công) với số lượng: " + amount);
        }
    }

    @Test
    public void testAddStock_Failure() {
        int[] amounts = {-1, 1001};
        for (int amount : amounts) {
            assertFalse(service.addStock(test, amount),
                    "Expected trả về false (nhập thất bại) với số lượng: " + amount);
        }
    }

    @Test
    public void testDeductStock_Success() {
        int[] amounts = {1, 49};
        for (int amount : amounts) {
            assertTrue(service.deductStock(test, amount),
                    "Expected trả về true (xuất hàng thành công) khi kho còn: "
                    + test.getStockQuantity() + " với số lượng: " + amount);
        }

    }

    @Test
    public void testDeduckStock_Failure() {
        int[] amounts = {-1, 51};
        for (int amount : amounts) {
            assertFalse(service.deductStock(test, amount),
                    "Expected trả về false (xuất hàng thất bại) khi kho còn: "
                    + test.getStockQuantity() + " với số lượng: " + amount);
        }
    }

    @Test
    public void testGetProduct_NotNull() {
        assertNotNull(service.getProduct(test.getSKU()),
                "Expected trả về Product (tìm thấy) với SKU: " + test.getSKU());
    }

    @Test
    public void testGetProduct_Null() {
        String sku = "not_exist_sku";
        assertNull(service.getProduct(sku),
                "Expected trả về null (không tìm thấy) với SKU: " + sku);
    }

    @Test
    public void testGetProducts_NotEmpty() {
        List<Product> source = service.getProducts("");
        assertTrue(!service.getProducts(test.getSKU(), source).isEmpty(),
                "Expected trả về true (tìm thấy) với kw: " + test.getSKU());
    }

    @Test
    public void testGetProducts_Empty() {
        String SKU = "not_exist_123";
        List<Product> source = service.getProducts("");
        assertFalse(!service.getProducts(SKU, source).isEmpty(),
                "Expected trả về false (không tìm thấy) với kw: " + SKU);
    }

    @Test
    public void testAddProduct_Success() {
        String SKU = "123";
        String productName = "Sản Phẩm 1";
        float unitPrice = 1111;
        int minStock = 3;
        int amount = 10;
        Product input = new Product(SKU, productName, amount, unitPrice, minStock);
        assertTrue(service.addProduct(input), 
                "Expected trả về true (tạo product thành công)");
        assertEquals(input.getSKU(), service.getProduct(input.getSKU()).getSKU(), 
                "Expected product được thêm vào CSDL");
        
        // Dọn dẹp
        try (Connection conn = JdbcUtils.getConn()) {
            String query = "DELETE FROM Products WHERE SKU = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, input.getSKU());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete product with this SKU: " + test.getSKU());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Test
    public void testAddProduct_Failure() {
        String SKU = "SKU Không Hợp Lệ @@";
        String productName = "";
        float unitPrice = -1;
        int minStock = -1;
        int amount = 10;
        
        Product input = new Product(SKU, productName, amount, unitPrice, minStock);
        assertFalse(service.addProduct(input), 
                "Expected trả về false (tạo product không thành công)");
        assertNull(service.getProduct(input.getSKU()), 
                "Expected product lỗi không được thêm vào CSDL");
    }
}
