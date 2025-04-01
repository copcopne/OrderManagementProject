/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author copcopne
*/

public class PasswordHasher {
    private PasswordHasher() {}
    
    public static String hashPassword(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String mixedInput = mixStrings(username, password);
            byte[] hashedBytes = digest.digest(mixedInput.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi thuật toán băm", e);
        }
    }

    private static String mixStrings(String str1, String str2) {
        StringBuilder mixed = new StringBuilder();
        int maxLength = Math.max(str1.length(), str2.length());
        for (int i = 0; i < maxLength; i++) {
            if (i < str1.length()) mixed.append(str1.charAt(i));
            if (i < str2.length()) mixed.append(str2.charAt(i));
        }
        return mixed.toString();
    }

}
