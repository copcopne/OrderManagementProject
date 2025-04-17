/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.pojo;

/**
 *
 * @author copcopne
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private boolean loggedIn;
    private boolean isAdmin;
    
    private UserSession() {
        this.loggedIn = false;
    }
    
    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void login(User user) {
        this.currentUser = user;
        this.loggedIn = true;
        this.isAdmin = user.getIsAdmin();
    }
    
    public void logout() {
        this.currentUser = null;
        this.loggedIn = false;
    }
    
    public boolean isLoggedIn() {
        return this.loggedIn;
    }
    
    public String getUsername() {
        return this.getCurrentUser().getUsername();
    }

    /**
     * @return the currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @return the isAdmin
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }
}
