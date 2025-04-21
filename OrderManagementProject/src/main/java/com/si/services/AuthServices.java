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
        if (user != null && PasswordHasher.hashPassword(password).equals(user.getPassword())) {
            return user;
        }
        return null;
    }
    
}
