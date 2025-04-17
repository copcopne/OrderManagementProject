/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.pojo.User;
import com.si.services.UserServices;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author copcopne
 */
public class UserTestSerivces {

    UserServices service = new UserServices();

    @Test
    public void testFindUserByUsername_Success() {
        String input = "admin";
        User actual = service.findUserByUsername(input);

        assertNotNull(actual);
        assertEquals(1, actual.getId());
        assertEquals("admin", actual.getUsername());
        assertEquals("Admin", actual.getFullName());
        assertTrue(actual.getIsAdmin());

    }
}
