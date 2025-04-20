/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestServices;

import com.si.services.ValidationServices;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author copcopne
 */
public class ValidationServicesTest {
    @Test
    public void testValidatePassword_True() {
        String[] validPasswords = {"12345678", "VipPro123", "68686868", "StrongP@SSw0rD", "1' OR '1'='1"};
        for(String password: validPasswords) 
            assertDoesNotThrow(() -> ValidationServices.validatePassword(password), 
                    "Expected không throw ra ngoại lệ với password: " + password);
    }
    
    @Test
    public void testValidatePassword_False() {
        String[] invalidPasswords = {"", "123456", "null"};
        for(String password: invalidPasswords) 
            assertThrows(IllegalArgumentException.class, () -> {
                ValidationServices.validatePassword(password);
        }, "Expected throw ra ngoại lệ IllegalArgumentException với password: " + password);
    }
    
    @Test
    public void testValidateUserdata_True() {
        String[] validUsernames = {"valid_username", "nguyenvana123","aaaaaa"};
        String[] validFullNames = {"Tên Hợp Lệ", "Nguyễn Văn A","AAAAAAAAA"};
        
        for(String username: validUsernames)
            assertDoesNotThrow(() -> ValidationServices.validateUserData(username, null),
                    "Expected không throw ra ngoại lệ với username: " + username);
        
        for(String fullname: validFullNames)
            assertDoesNotThrow(() -> ValidationServices.validateUserData(null, fullname),
                    "Expected không throw ra ngoại lệ với fullName: " + fullname);
    }
    
    @Test
    public void testValidateUserdata_False() {
        String[] invalidUsernames = {"1", "null", "1' OR '1'='1", ""};
        String[] invalidFullNames = {"Nguyễn Văn A🔥", "1' OR '1'='1", ""};
        
        for(String username: invalidUsernames)
            assertThrows(IllegalArgumentException.class, 
                    () -> ValidationServices.validateUserData(username, null),
                    "Expected không throw ra ngoại lệ với username: " + username);
        
        for(String fullname: invalidFullNames)
            assertThrows(IllegalArgumentException.class, 
                    () -> ValidationServices.validateUserData(null, fullname),
                    "Expected không throw ra ngoại lệ với fullName: " + fullname);
    }
}
