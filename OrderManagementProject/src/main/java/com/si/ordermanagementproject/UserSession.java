/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.ordermanagementproject;

/**
 *
 * @author copcopne
 */
public class UserSession {
    private static UserSession instance;
    private String username;
    private boolean loggedIn;
    
    private UserSession() {
        this.loggedIn = false;
    }
    
    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void login(String username) {
        this.username = username;
        this.loggedIn = true;
    }
    
    public void logout() {
        this.username = null;
        this.loggedIn = false;
    }
    
    public boolean isLoggedIn() {
        return this.loggedIn;
    }
    
    public String getUsername() {
        return this.username;
    }
}
