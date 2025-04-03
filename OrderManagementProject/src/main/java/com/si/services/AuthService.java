/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

import com.si.pojo.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author copcopne
 */
public class AuthService {
    private AuthService() {}
    
    public static User authenticate(String username, String password){
        try {
            UserService u = UserService.getInstance();
            User user = u.findUserByUsername(username);
             if (user != null && PasswordHasher.hashPassword(username, password).equals(user.getPassword())) {
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
