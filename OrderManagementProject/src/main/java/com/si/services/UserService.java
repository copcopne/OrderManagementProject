/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.si.services;

import com.si.pojo.JdbcUtils;
import com.si.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author copcopne
 */
public class UserService {
    private static UserService instance;
    
    private UserService() { }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    public User findUserByUsername(String username) throws SQLException {
        User user = null;
        
        try(Connection conn = JdbcUtils.getConn(); 
            PreparedStatement stm = conn.prepareCall("SELECT * FROM Users WHERE Username=?");) {
            stm.setString(1, username);
            
            try(ResultSet rs = stm.executeQuery();) {
                if(rs.next()) {
                    user = new User(rs.getInt("UserID"), rs.getString("FullName"),rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                }
            }
        }
        catch(Exception e) {
            System.out.println("An error occured during query for username: " + username);
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> findUsersByName(String kw) {
        
        List<User> users = new ArrayList<>();
        return users;
    }
}
