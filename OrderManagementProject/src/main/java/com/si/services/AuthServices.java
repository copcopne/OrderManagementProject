/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

import com.si.pojo.User;

/**
 *
 * @author copcopne
 */
public class AuthServices {
    private AuthServices() {}
    
    public static User authenticate(String username, String password){
        UserServices u = new UserServices();
        User user = u.findUserByUsername(username);
        if (user != null && PasswordHasher.hashPassword(username, password).equals(user.getPassword())) {
            return user;
        }
        return null;
    }
    
    public static void isPasswordValid(String password) throws IllegalArgumentException {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$")) {
            throw new IllegalArgumentException("Password must include at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }
}
