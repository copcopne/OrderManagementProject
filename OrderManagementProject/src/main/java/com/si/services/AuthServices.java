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
            throw new IllegalArgumentException("Mật khẩu phải từ 8 ký tự trở lên.");
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$")) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 1 ký tự hoa, 1 ký tự thường, 1 chữ số và 1 ký tự đặc biệt.");
        }
    }
}
