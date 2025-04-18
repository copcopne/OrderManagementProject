/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.pojo.JdbcUtils;
import com.si.pojo.User;
import com.si.services.PasswordHasher;
import com.si.services.UserServices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

/**
 *
 * @author copcopne
 */
public class UserTestSerivces {

    private static final UserServices service = new UserServices();

    @BeforeEach
    public void setupData() throws SQLException {
        String query = "INSERT INTO Users (FullName, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = PasswordHasher.hashPassword("123456");
            stmt.setString(1, "Tester 1");
            stmt.setString(2, "tester123");
            stmt.setString(3, hashedPassword);
            stmt.setBoolean(4, false);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create user: " + "tester123");
            }
        }
    }

    @AfterEach
    public void cleanup() throws SQLException {
        String query = "DELETE FROM Users WHERE Username = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "tester123");

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete user: " + "tester123");
            }
        }
    }

    @Test
    public void testFindUserByUsername_Found() {
        String input = "tester123";
        User actual = service.findUserByUsername(input);

        assertNotNull(actual, "Expected có user với input: tester123");
        assertEquals(input, actual.getUsername(), "Expected username của user tìm được giống với input: tester123");
        assertEquals("Tester 1", actual.getFullName(), "Expected username của user tìm được giống với input: Tester 1");
        assertFalse(actual.getIsAdmin(), "Expected isAdmin của user tìm được là False");
    }

    @Test
    public void testFindUserByUsername_NotFound() {
        String[] inputs = {"no_user_exists_123", "😊🔥💀", "null", "こんにちは", "%", "_", "1' OR '1'='1"};
        for (String input : inputs) {
            User actual = service.findUserByUsername(input);
            assertNull(actual, "Expected null với input: " + input);
        }
    }

    @Test
    public void testFindUsers_NotEmpty() {
        List<User> users1 = service.findUsers("");
        assertTrue(!users1.isEmpty(), "Expected list có phần tử với input: " + "");

        List<User> users2 = service.findUsers("tester123");
        assertTrue(!users2.isEmpty(), "Expected list có phần tử với input: " + "tester123");
    }

    @Test
    public void testFindUsers_Empty() {
        String[] inputs = {"no_user_exists_123", "😊🔥💀", "null", "こんにちは", "%", "_", "1' OR '1'='1"};
        for (String input : inputs) {
            List<User> user = service.findUsers(input);
            assertTrue(user.isEmpty(), "Expected list rỗng với input: " + input);
        }
    }

    @Test
    public void testCreateUser_Success() {
        String username = "test123";
        String fullName = "Nguyễn Văn A Tester";
        String rawPassword = "test1234";
        boolean actual = service.createUser(fullName, username, rawPassword, false);
        assertTrue(actual, "Expected trả về true(tạo thành công) với username: "
                + "" + username + " , và fullName: " + fullName);

        service.deleteUser(username); // Dọn dẹp
    }

    @Test
    public void testCreateUser_Failure() {
        String[] usernames = {"", "null", "admin", "1' OR '1'='1"};
        String[] fullNames = {"", "こんにちは", "%", "1' OR '1'='1"};
        String rawPassword = "test1234";
        for (String fullName : fullNames) {
            for (String username : usernames) {
                assertFalse(service.createUser(fullName, username, rawPassword, false),
                        "Expect trả về false(tạo thất bại) với username: " + username + " , và fullName: " + fullName);
            }
        }
    }

    @Test
    public void testUpdateUser_Success() {
        String newUsername = "teo123456";
    }

    @Test
    public void testUpdateUser_Failure() {

    }

    @Test
    public void testDeleteUSer_Success() {

    }

    @Test
    public void testDeleteUser_Failure() {

    }
}
