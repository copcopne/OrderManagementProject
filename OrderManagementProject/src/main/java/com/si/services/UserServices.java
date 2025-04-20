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

//    public List<User> findUsers(String kw) {
//        List<User> users = new ArrayList<>();
//        if (kw == null || kw.contains("%") || kw.contains("_")) {
//            return users;
//        }
//
//        String queryAll = "SELECT * FROM Users";
//        String queryByFullName = "SELECT * FROM Users WHERE FullName LIKE ?";
//        String queryByUsername = "SELECT * FROM Users WHERE Username LIKE ?";
//
//        try (Connection conn = JdbcUtils.getConn()) {
//            if (kw.trim().isEmpty()) {
//                try (PreparedStatement stm = conn.prepareStatement(queryAll); ResultSet rs = stm.executeQuery()) {
//                    while (rs.next()) {
//                        users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
//                                rs.getString("Username"), rs.getBoolean("isAdmin")));
//                    }
//                }
//            } else {
//                try (PreparedStatement stm1 = conn.prepareStatement(queryByFullName)) {
//                    stm1.setString(1, "%" + kw.trim() + "%");
//                    try (ResultSet rs = stm1.executeQuery()) {
//                        while (rs.next()) {
//                            users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
//                                    rs.getString("Username"), rs.getBoolean("isAdmin")));
//                        }
//                    }
//                }
//                if (users.isEmpty()) {
//                    try (PreparedStatement stm2 = conn.prepareStatement(queryByUsername)) {
//                        stm2.setString(1, "%" + kw.trim() + "%");
//                        try (ResultSet rs = stm2.executeQuery()) {
//                            while (rs.next()) {
//                                users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
//                                        rs.getString("Username"), rs.getBoolean("isAdmin")));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            System.out.println("Database Error: " + ex.getMessage());
//        } catch (Exception ex) {
//            System.out.println("Unexpected Error: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        return users;
//    }
    public List<User> findUsers(String kw) {
        List<User> users = new ArrayList<>();

        if (kw == null || kw.contains("%") || kw.contains("_")) {
            return users;
        }

        String queryAll = "SELECT * FROM Users";
        String queryByKeyword = "SELECT * FROM Users WHERE FullName LIKE ? OR Username LIKE ?";

        try (Connection conn = JdbcUtils.getConn()) {
            if (kw.trim().isEmpty()) {
                try (PreparedStatement stm = conn.prepareStatement(queryAll); ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
                                rs.getString("Username"), rs.getBoolean("isAdmin")));
                    }
                }
            } else {
                try (PreparedStatement stm = conn.prepareStatement(queryByKeyword)) {
                    String likeKw = "%" + kw.trim() + "%";
                    stm.setString(1, likeKw);
                    stm.setString(2, likeKw);
                    try (ResultSet rs = stm.executeQuery()) {
                        while (rs.next()) {
                            users.add(new User(rs.getInt("UserID"), rs.getString("FullName"),
                                    rs.getString("Username"), rs.getBoolean("isAdmin")));
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

    public boolean createUser(String fullName, String username, String password, boolean isAdmin)
            throws IllegalArgumentException, SQLException {
        if (this.findUserByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists: " + username);
        }
        ValidationServices.validateUserData(username, fullName);
        ValidationServices.validatePassword(password);

        String query = "INSERT INTO Users (FullName, Username, Password, IsAdmin) VALUES (?, ?, ?, ?)";
        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
            String hashedPassword = PasswordHasher.hashPassword(password);
            stm.setString(1, fullName.trim());
            stm.setString(2, username.trim());
            stm.setString(3, hashedPassword);
            stm.setBoolean(4, isAdmin);

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to create user: " + username);
            }
            return true;
        }
    }

    public boolean deleteUser(String username) throws NoSuchElementException, SQLException {
        String query = "DELETE FROM Users WHERE Username = ?";

            User u = this.findUserByUsername(username);
            if (u == null) {
                throw new NoSuchElementException("Không tìm thấy người dùng với username: " + username);
            }

            try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
                stm.setString(1, username);

                int rowsAffected = stm.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Lỗi khi cố gắng xóa người dùng với username: " + username);
                }
                return true;
            }

    }

    public boolean updateUser(String currentUsername, String newUsername, String fullName, String password, Boolean isAdmin)
            throws IllegalArgumentException, SQLException {

        User user = this.findUserByUsername(currentUsername);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với username: " + currentUsername);
        }

        if (newUsername != null && !currentUsername.equals(newUsername)) {
            User u = findUserByUsername(newUsername);
            if (u != null) {
                throw new IllegalArgumentException("Username đã tồn tại: " + newUsername);
            }
        }

//        boolean hasUsername = newUsername != null && !newUsername.isBlank();
//        boolean hasFullName = fullName != null && !fullName.isBlank();
//        boolean hasPassword = password != null && !password.isBlank();
        boolean hasUsername = newUsername != null;
        boolean hasFullName = fullName != null;
        boolean hasPassword = password != null;
        boolean hasIsAdmin = isAdmin != null;

        List<String> updates = new ArrayList<>();

        if (hasFullName) {
            ValidationServices.validateUserData(null, fullName);
            updates.add("FullName = ?");
        }
        if (hasUsername) {
            ValidationServices.validateUserData(newUsername, null);
            updates.add("Username = ?");
        }
        if (hasPassword) {
            ValidationServices.validatePassword(password);
            updates.add("Password = ?");
        }
        if (hasIsAdmin) {
            updates.add("IsAdmin = ?");
        }

        String query = "UPDATE Users SET " + String.join(", ", updates) + " WHERE Username = ?";

        try (Connection conn = JdbcUtils.getConn(); PreparedStatement stm = conn.prepareStatement(query)) {
            int index = 1;
            if (hasFullName) {
                stm.setString(index++, fullName.trim());
            }
            if (hasUsername) {
                stm.setString(index++, newUsername.trim());
            }
            if (hasPassword) {
                stm.setString(index++, PasswordHasher.hashPassword(password));
            }
            if (hasIsAdmin) {
                stm.setBoolean(index++, isAdmin);
            }

            stm.setString(index++, currentUsername);

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Cập nhật thất bại cho user: " + currentUsername);
            }

            return true;

        } catch (Exception ex) {
            System.out.println("Unexpected Error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

}
