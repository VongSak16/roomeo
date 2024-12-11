package controller;

import database.dao.UserDAO;
import database.model.User;
import database.model.Property;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import utils.PasswordUtils;

/**
 * Controller for User operations
 */
public class UserController {

    private UserDAO userDAO;

    public UserController() throws SQLException {
        userDAO = new UserDAO(); // Instantiate DAO in the controller
    }

    // Get all users via the controller
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    // Get user by ID via the controller
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    // Delete user by ID via the controller
    public boolean deleteUserById(int userId) throws SQLException, IOException {
        // Get user info to retrieve photo paths
        User user = userDAO.getUserById(userId);
        if (user != null) {
            String profilePicturePath = "resources/images/users/" + user.getProfile_picture();
            String frontPhotoPath = "resources/images/users/" + user.getId_front_photo();
            String backPhotoPath = "resources/images/users/" + user.getId_back_photo();
            // Delete photos
            Files.deleteIfExists(Paths.get(profilePicturePath));
            Files.deleteIfExists(Paths.get(frontPhotoPath));
            Files.deleteIfExists(Paths.get(backPhotoPath));
        }
        return userDAO.deleteUserById(userId);
    }

    // Add new user via the controller
    public void addUser(Map<String, Object> userAttributes) throws SQLException, IOException {
        String name = (String) userAttributes.get("name");
        String username = (String) userAttributes.get("username");
        String password = PasswordUtils.hashPassword((String) userAttributes.get("password"));
        String role = (String) userAttributes.get("role");
        String phone = (String) userAttributes.get("phone");
        String profilePicturePath = (String) userAttributes.get("profilePicturePath");
        String frontPhotoPath = (String) userAttributes.get("frontPhotoPath");
        String backPhotoPath = (String) userAttributes.get("backPhotoPath");
        Property property = (Property) userAttributes.get("property");

        // Check if username is unique
        if (userDAO.usernameExists(username)) {
            throw new SQLException("Username already exists");
        }

        String uniqueProfileFileName = null;
        String uniqueFrontFileName = null;
        String uniqueBackFileName = null;

        // Handle profile photo if it's not null
        if (profilePicturePath != null) {
            String profileFileExtension = profilePicturePath.substring(profilePicturePath.lastIndexOf("."));
            uniqueProfileFileName = UUID.randomUUID().toString() + profileFileExtension;
            Path profileDestinationPath = Paths.get("resources/images/users/" + uniqueProfileFileName);
            Files.copy(Paths.get(profilePicturePath), profileDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Handle front photo if it's not null
        if (frontPhotoPath != null) {
            String frontFileExtension = frontPhotoPath.substring(frontPhotoPath.lastIndexOf("."));
            uniqueFrontFileName = UUID.randomUUID().toString() + frontFileExtension;
            Path frontDestinationPath = Paths.get("resources/images/users/" + uniqueFrontFileName);
            Files.copy(Paths.get(frontPhotoPath), frontDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Handle back photo if it's not null
        if (backPhotoPath != null) {
            String backFileExtension = backPhotoPath.substring(backPhotoPath.lastIndexOf("."));
            uniqueBackFileName = UUID.randomUUID().toString() + backFileExtension;
            Path backDestinationPath = Paths.get("resources/images/users/" + uniqueBackFileName);
            Files.copy(Paths.get(backPhotoPath), backDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create User object
        User newUser = new User(0, name, username, password, role, phone, uniqueProfileFileName, uniqueFrontFileName, uniqueBackFileName, property);
        userDAO.addUser(newUser);
    }

    // Update user via the controller
    public void updateUser(Map<String, Object> userAttributes) throws SQLException, IOException {
        int id = (int) userAttributes.get("id");
        String name = (String) userAttributes.get("name");
        String username = (String) userAttributes.get("username");
        String password = (String) userAttributes.get("password");
        String role = (String) userAttributes.get("role");
        String phone = (String) userAttributes.get("phone");
        String profilePicturePath = (String) userAttributes.get("profilePicturePath");
        String frontPhotoPath = (String) userAttributes.get("frontPhotoPath");
        String backPhotoPath = (String) userAttributes.get("backPhotoPath");
        Property property = (Property) userAttributes.get("property");

        // Retrieve current user to get existing photo paths and username
        User currentUser = userDAO.getUserById(id);
        String uniqueProfileFileName = currentUser.getProfile_picture();
        String uniqueFrontFileName = currentUser.getId_front_photo();
        String uniqueBackFileName = currentUser.getId_back_photo();

        if (password != null && !password.isEmpty()) {
            password = PasswordUtils.hashPassword(password); // Hash the new password
        } else {
            password = currentUser.getPassword(); // Keep the existing hashed password
        }

        // Check if username is unique only if it has changed
        if (!username.equals(currentUser.getUsername()) && userDAO.usernameExists(username)) {
            throw new SQLException("Username already exists");
        }

        // Handle profile photo
        if (profilePicturePath != null && !profilePicturePath.equals("resources/images/users/" + currentUser.getProfile_picture())) {
            Files.deleteIfExists(Paths.get("resources/images/users/" + currentUser.getProfile_picture()));
            String profileFileExtension = profilePicturePath.substring(profilePicturePath.lastIndexOf("."));
            uniqueProfileFileName = UUID.randomUUID().toString() + profileFileExtension;
            Path profileDestinationPath = Paths.get("resources/images/users/" + uniqueProfileFileName);
            Files.copy(Paths.get(profilePicturePath), profileDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Handle front photo
        if (frontPhotoPath != null && !frontPhotoPath.equals("resources/images/users/" + currentUser.getId_front_photo())) {
            Files.deleteIfExists(Paths.get("resources/images/users/" + currentUser.getId_front_photo()));
            String frontFileExtension = frontPhotoPath.substring(frontPhotoPath.lastIndexOf("."));
            uniqueFrontFileName = UUID.randomUUID().toString() + frontFileExtension;
            Path frontDestinationPath = Paths.get("resources/images/users/" + uniqueFrontFileName);
            Files.copy(Paths.get(frontPhotoPath), frontDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Handle back photo
        if (backPhotoPath != null && !backPhotoPath.equals("resources/images/users/" + currentUser.getId_back_photo())) {
            Files.deleteIfExists(Paths.get("resources/images/users/" + currentUser.getId_back_photo()));
            String backFileExtension = backPhotoPath.substring(backPhotoPath.lastIndexOf("."));
            uniqueBackFileName = UUID.randomUUID().toString() + backFileExtension;
            Path backDestinationPath = Paths.get("resources/images/users/" + uniqueBackFileName);
            Files.copy(Paths.get(backPhotoPath), backDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create User object and update it in the database
        User updatedUser = new User(id, name, username, password, role, phone, uniqueProfileFileName, uniqueFrontFileName, uniqueBackFileName, property);
        userDAO.updateUser(updatedUser);

    }

    public boolean usernameExists(String username) throws SQLException {
        return userDAO.usernameExists(username);
    }
}
