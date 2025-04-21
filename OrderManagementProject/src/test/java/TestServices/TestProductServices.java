package TestServices;


import com.si.pojo.JdbcUtils;
import com.si.pojo.Product;
import com.si.services.ProductServices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class TestProductServices {

    private static final ProductServices p = new ProductServices();
    private int orderID = -1;

    @BeforeEach
    public void setupData() throws SQLException {

        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);

            // Tạo product TestSKU
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO Products(SKU, ProductName, StockQuantity, UnitPrice, MinStockLevel) "
                    + "VALUES (?, ?, ?, ?, ?)");
            st.setString(1, "TestSKU");
            st.setString(2, "Test Product");
            st.setInt(3, 5); // Stock = 5
            st.setFloat(4, 100.0f);
            st.setInt(5, 1);
            st.executeUpdate();

            // Tạo đơn hàng và lấy ID
            PreparedStatement st2 = conn.prepareStatement(
                    "INSERT INTO Orders(CustomerID, OrderDate, ShipAddress, OrderStatus) VALUES (?, NOW(), ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st2.setInt(1, 1);
            st2.setString(2, "Test Address");
            st2.setString(3, "Đang xử lý");
            st2.executeUpdate();
            ResultSet rs = st2.getGeneratedKeys();
            rs.next();
            this.orderID = rs.getInt(1);

            PreparedStatement st3 = conn.prepareStatement(
                    "INSERT INTO OrderDetails(OrderID, SKU, Quantity, Discount) VALUES (?, ?, ?, ?)");
            st3.setInt(1, orderID);
            st3.setString(2, "TestSKU");
            st3.setInt(3, 3); // Quantity < Stock
            st3.setFloat(4, 0.0f);
            st3.executeUpdate();
            conn.commit();
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            PreparedStatement st1 = conn.prepareStatement("DELETE FROM OrderDetails WHERE SKU = ?");
            st1.setString(1, "TestSKU");
            st1.executeUpdate();

            PreparedStatement st2 = conn.prepareStatement("DELETE FROM Orders WHERE OrderID = ?");
            st2.setInt(1, orderID);
            st2.executeUpdate();

            PreparedStatement st3 = conn.prepareStatement("DELETE FROM Products WHERE SKU = ?");
            st3.setString(1, "TestSKU");
            st3.executeUpdate();
            conn.commit();
            conn.close();
        }
    }

    @Test
    public void testGetProductsWithoutKW() {
        List<Product> products = p.getProducts(null);
        assertTrue(!products.isEmpty());
    }

    @Test
    public void testGetProductsWithValidKW() {
        String kw = "TestSKU";
        List<Product> products = p.getProducts(kw);
        assertTrue(!products.isEmpty());
        assertEquals("TestSKU", products.get(0).getSKU());
    }

    @Test
    public void testGetProductWithInvalidKW() {
        String kw = "khonghople";
        List<Product> products = p.getProducts(kw);
        assertTrue(products.isEmpty());
    }

    @Test
    public void testIsStockAvailable_True() {
        assertTrue(p.isStockAvailable(orderID));
    }

    @Test
    public void testIsStockAvailable_False() throws SQLException {
        // Cập nhật OrderDetail quantity = 10 để vượt stock
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement st = conn.prepareStatement("UPDATE OrderDetails SET Quantity = ? WHERE OrderID = ? AND SKU = ?");
            st.setInt(1, 10);
            st.setInt(2, orderID);
            st.setString(3, "TestSKU");
            st.executeUpdate();
        }

        assertFalse(p.isStockAvailable(orderID));
    }

    @Test
    public void testDeductStock_Success() throws SQLException {

        p.deductStock(orderID);

        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement st = conn.prepareStatement("SELECT StockQuantity FROM Products WHERE SKU = ?");
            st.setString(1, "TestSKU");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("StockQuantity");
                assertEquals(2, quantity); // 5 - 3 = 2
            } else {
                fail("Product TestSKU not found");
            }
        }
    }

    @Test
    public void testDeductStock_Fail() throws SQLException {
        
        // Cập nhật lại số lượng trong OrderDetails thành 10 (lớn hơn stock 5)
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement st = conn.prepareStatement("UPDATE OrderDetails SET Quantity = ? "
                    + "WHERE OrderID = ? AND SKU = ?");
            st.setInt(1, 10);
            st.setInt(2, orderID);
            st.setString(3, "TestSKU");
            st.executeUpdate();
        }

        p.deductStock(orderID);

        // Kiểm tra tồn kho vẫn giữ nguyên
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement st = conn.prepareStatement("SELECT StockQuantity FROM Products WHERE SKU = ?");
            st.setString(1, "TestSKU");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("StockQuantity");
                assertEquals(5, quantity); // vì không đủ hàng nên không bị trừ
            } else {
                fail("Product TestSKU not found!");
            }
        }

    }
}
