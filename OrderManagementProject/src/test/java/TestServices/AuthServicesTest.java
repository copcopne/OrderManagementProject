/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.pojo.JdbcUtils;
import com.si.pojo.User;
import com.si.services.AuthServices;
import com.si.services.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author copcopne
 */
public class AuthServicesTest {
    
    String username = "tester123";
    String password = "123456";

    @BeforeEach
    public void setupData() throws SQLException {
        String query = "INSERT INTO Users (FullName, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = PasswordHasher.hashPassword(password);
            stmt.setString(1, "Tester 1");
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setBoolean(4, false);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create user: " + username);
            }
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        String query = "DELETE FROM Users WHERE Username = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete user: " + username);
            }
        }
    }
    
    @Test
    public void testAuthenticate_Success() {
        User testUser = AuthServices.authenticate(username, password);
        assertEquals(testUser.getUsername(), username, "Expected trả về User với username: " + username);
    }
    
    @Test
    public void testAuthenticate_Failure() {
        String notExistUsername = "aaa";
        String randomPassword = "aaa";
        
        assertNull(AuthServices.authenticate(notExistUsername, randomPassword), "Expected trả về null (xác thực thất bại) do nhập username và password không tồn tại");
        //Đăng nhập sai mật khẩu với user dùng để test
        assertNull(AuthServices.authenticate(username, "wrongPassword123"), "Expected trả về null (xác thực thất bại) do nhập sai nhật khẩu");
    }
}
