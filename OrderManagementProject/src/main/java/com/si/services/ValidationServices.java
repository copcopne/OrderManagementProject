/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

/**
 *
 * @author copcopne
 */
public class ValidationServices {
    public static void isPasswordValid(String password) throws IllegalArgumentException {
        if (password.length() <= 8) {
            throw new IllegalArgumentException("Mật khẩu phải từ 8 ký tự trở lên.");
        }
    }
    
    public static void validateUserData(String username, String fullName) throws IllegalArgumentException {
        if (username.isEmpty() || username.isBlank())
            throw new IllegalArgumentException("Tên người dùng không được để trống!");
        
        if (username.length() < 6 || username.length() > 20)
            throw new IllegalArgumentException("Tên người dùng phải nằm trong khoảng (6-20) ký tự!");
        
        
        if (fullName.isEmpty() || fullName.isBlank())
            throw new IllegalArgumentException("Tên không được để trống!");
        
        if (fullName.length() > 50)
            throw  new IllegalArgumentException("Tên không được quá 50 ký tự!");
    }
}
