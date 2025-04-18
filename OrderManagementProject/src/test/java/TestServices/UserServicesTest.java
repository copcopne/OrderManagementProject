/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.pojo.JdbcUtils;
import com.si.pojo.User;
import com.si.services.AuthServices;
import com.si.services.PasswordHasher;
import com.si.services.UserServices;
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
public class UserServicesTest {

    private static final UserServices service = new UserServices();
    String username = "tester123";

    @BeforeEach
    public void setupData() throws SQLException {
        String query = "INSERT INTO Users (FullName, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = PasswordHasher.hashPassword("123456");
            stmt.setString(1, "Tester 1");
            stmt.setString(2, username);
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
            stmt.setString(1, username);

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
        String uname = "test123";
        String fullName = "Nguyễn Văn A Tester";
        String rawPassword = "test1234";
        boolean actual = service.createUser(fullName, uname, rawPassword, false);
        assertTrue(actual, "Expected trả về true(tạo thành công) với username: "
                + "" + uname+ " , và fullName: " + fullName);
        
        service.deleteUser(uname); // Dọn dẹp

    }

    @Test
    public void testCreateUser_Failure() {
        String[] usernames = {"", "null", "admin", "1' OR '1'='1"};
        String[] fullNames = {"", "こんにちは", "%", "1' OR '1'='1"};
        String rawPassword = "test1234";
        for (String fullName : fullNames) {
            for (String uname : usernames) {
                assertFalse(service.createUser(fullName, uname, rawPassword, false),
                        "Expect trả về false(tạo thất bại) với username: " + uname + " , và fullName: " + fullName);
            }
        }
    }

    @Test
    public void testUpdateUser_Success() {
        try {
            String newUsername = "teo123";
            String newFullName = "Nguyễn Văn Tèo";
            String newRawPassword = "12345678";

            assertTrue(service.updateUser(username, null, newFullName, null, null), "Expected trả về true (cập nhật thành công) khi cập nhật 1 trường FullName");
            assertTrue(service.updateUser(username, newUsername, null, null, null), "Expected trả về true (cập nhật thành công) khi cập nhật 1 trường FullName");
            assertNotNull(service.findUserByUsername(newUsername), "Expected trả về User (tìm thấy User bằng username mới) sau khi thực hiện đổi tên người dùng và tìm lại");
            assertTrue(service.updateUser(newUsername, null, null, newRawPassword, null), "Expected trả về true (cập nhật thành công) khi cập nhật 1 trường Password");
            assertNotNull(AuthServices.authenticate(newUsername, newRawPassword), "Expected trả về User (đăng nhập thành công) với username mới và password mới");

            service.updateUser(newUsername, username, null, null, null); // Cho username dùng để test quay về như cũ
        } catch (IllegalArgumentException | SQLException ex) {
            Logger.getLogger(UserServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testUpdateUser_Failure() {
        try {
            String nonexistentUsername = "not_exist_123";
            assertFalse(service.updateUser(nonexistentUsername, "newUsername", "New FullName", "newpassword", false),
                    "Expected trả về false (cập nhật thất bại) khi cố gắng cập nhật user không tồn tại");

            String[] invalidUsernames = {"", "null", "1' OR '1'='1", "$%%%%%%%$%$%"};
            for (String n : invalidUsernames) {
                assertFalse(service.updateUser("tester123", n, "New FullName", "newpassword", false),
                        "Expected trả về false (cập nhật thất bại) khi username không hợp lệ: " + username);
            }

            String[] invalidFullNames = {"", "null", "1' OR '1'='1", "$%%%%%%%$%$%"};
            for (String fullName : invalidFullNames) {
                assertFalse(service.updateUser("tester123", "tester123", fullName, "newpassword", false),
                        "Expected trả về false (cập nhật thất bại) khi FullName không hợp lệ: " + fullName);
            }

            String[] invalidPasswords = {"123456", "", "null"};
            for (String password : invalidPasswords) {
                assertFalse(service.updateUser("tester123", "tester123", "New FullName", password, false),
                        "Expected trả về false (cập nhật thất bại) khi password không hợp lệ: " + password);
            }

            String existingUsername = "admin";
            assertFalse(service.updateUser(existingUsername, username, "New FullName", "newpassword", false),
                    "Expected trả về false (cập nhật thất bại) khi cố gắng thay đổi username đã tồn tại");

        } catch (IllegalArgumentException | SQLException ex) {
            Logger.getLogger(UserServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDeleteUSer_Success() {
        String input = "testdeleteuser";
        service.createUser("Nguyễn Văn A", input, "123456789", false); // Khởi tạo dữ liệu để test
        
        assertTrue(service.deleteUser(input), "Expected trả về true (Xóa thành công user dùng để test) với username: " + input);
    }

    @Test
    public void testDeleteUser_Failure() {
        String[] notExistUsernames = {"", "null", "1' OR '1'='1", "blablabla"};
        for(String uname: notExistUsernames)
            assertFalse(service.deleteUser(uname), "Expected trả về false (xóa thất bại) khi cố gắng xóa User không tồn tại với username: " + uname);
    }
}
