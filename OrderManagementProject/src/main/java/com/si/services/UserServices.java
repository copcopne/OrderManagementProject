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
import java.util.NoSuchElementException;

/**
 *
 * @author copcopne
 */
public class UserServices {

    public User findUserByUsername(String username) {
        User user = null;
        String query = "SELECT * FROM Users WHERE Username=?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query);) {
            stm.setString(1, username);

            try (ResultSet rs = stm.executeQuery();) {
                if (rs.next()) {
                    user = new User(rs.getInt("UserID"), rs.getString("FullName"),
                            rs.getString("Username"), rs.getBoolean("isAdmin"));
                    user.setPassword(rs.getString("Password"));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Database Error:" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return user;
    }

    public List<User> findUsersByName(String kw) {
        List<User> users = new ArrayList<>();
        String queryAll = "SELECT * FROM Users";
        String queryByFullName = "SELECT * FROM Users WHERE FullName LIKE ?";
        String queryByUsername = "SELECT * FROM Users WHERE Username LIKE ?";

        try (Connection conn = JdbcUtils.getConn()) {
            if (kw == null || kw.trim().isEmpty()) {
                try (PreparedStatement stm = conn.prepareStatement(queryAll); ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
                                rs.getString("Username"), rs.getBoolean("isAdmin")));
                    }
                }
            } else {
                try (PreparedStatement stm1 = conn.prepareStatement(queryByFullName)) {
                    stm1.setString(1, "%" + kw + "%");
                    try (ResultSet rs = stm1.executeQuery()) {
                        while (rs.next()) {
                            users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
                                    rs.getString("Username"), rs.getBoolean("isAdmin")));
                        }
                    }
                }
                if (users.isEmpty()) {
                    try (PreparedStatement stm2 = conn.prepareStatement(queryByUsername)) {
                        stm2.setString(1, "%" + kw + "%");
                        try (ResultSet rs = stm2.executeQuery()) {
                            while (rs.next()) {
                                users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
                                        rs.getString("Username"), rs.getBoolean("isAdmin")));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return users;
    }

    public boolean createUser(String fullName, String username, String password, boolean isAdmin) {
        try {
            if (this.findUserByUsername(username) != null) {
                throw new IllegalArgumentException("User already exists: " + username);
            }

            AuthServices.isPasswordValid(password);

            String query = "INSERT INTO Users (FullName, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";
            try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
                String hashedPassword = PasswordHasher.hashPassword(username, password);
                stm.setString(1, fullName);
                stm.setString(2, username);
                stm.setString(3, hashedPassword);
                stm.setBoolean(4, isAdmin);

                int rowsAffected = stm.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to create user: " + username);
                }
                return true;
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation Error: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM Users WHERE Username = ?";

        try {
            User u = this.findUserByUsername(username);
            if (u == null) {
                throw new NoSuchElementException("No user found with username: " + username);
            }

            try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
                stm.setString(1, username);

                int rowsAffected = stm.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to delete user: " + username);
                }
                return true;
            }

        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

    public boolean updateUser(String currentUsername, String newUsername, String fullName, String password) {
        String query = "UPDATE Users SET FullName = ?, Password = ?, Username = ? WHERE Username = ?";

        User user = this.findUserByUsername(currentUsername);
        if (user == null) {
            return false;
        }

        if (!currentUsername.equals(newUsername)) {
            User u = findUserByUsername(newUsername);
            if (u != null) {
                throw new IllegalArgumentException("Username already taken: " + newUsername);
            }
        }

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, fullName);
            stm.setString(2, PasswordHasher.hashPassword(newUsername, password));
            stm.setString(3, newUsername);
            stm.setString(4, currentUsername);

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update user: " + currentUsername);
            }
            return true;

        } catch (IllegalArgumentException ex) {
            System.out.println("Validation Error: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

}
