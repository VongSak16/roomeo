/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.dao.UserDAO;
import database.model.User;
import java.sql.SQLException;
import services.AuthService;
import utils.PasswordUtils;

public class LoginController {

    private UserDAO userDAO; // Assuming you have a DAO to fetch user data

    public LoginController() throws SQLException {
        this.userDAO = new UserDAO(); // Or inject it via constructor
    }

    public boolean login(String username, String password) throws SQLException {
        User user = userDAO.getUserByUsername(username); // Fetch user by username

        if (user == null) {
            throw new SQLException("User not found");
        }

        // Verify the password using PasswordUtils (Hash comparison)
        boolean passwordMatch = PasswordUtils.verifyPassword(password, user.getPassword());

        if (!passwordMatch) {
            throw new SQLException("Invalid password");
        }

        // Successful login
        AuthService.setAuthenticatedUser(user); // Store user in AuthService singleton
        return true; // Login success
    }
}
