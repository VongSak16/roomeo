package database.dao;

import database.DatabaseConnection;
import database.model.Property;
import database.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        // Open the connection in a try-with-resources block to ensure it closes
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String phone = resultSet.getString("phone");
                String profile_picture = resultSet.getString("profile_picture");
                String id_front_photo = resultSet.getString("id_front_photo");
                String id_back_photo = resultSet.getString("id_back_photo");
                Property property = new PropertyDAO().getPropertyById(resultSet.getInt("property_id"));

                User user = new User(id, name, username, password, role, phone, profile_picture, id_front_photo, id_back_photo, property);
                userList.add(user);
            }
        }
        return userList;
    }

    // Get user by ID
    public User getUserById(int userId) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    String phone = resultSet.getString("phone");
                    String profile_picture = resultSet.getString("profile_picture");
                    String id_front_photo = resultSet.getString("id_front_photo");
                    String id_back_photo = resultSet.getString("id_back_photo");
                    Property property = new PropertyDAO().getPropertyById(resultSet.getInt("property_id"));

                    user = new User(id, name, username, password, role, phone, profile_picture, id_front_photo, id_back_photo, property);
                }
            }
        }
        return user;
    }

    // Get user by Username
    public User getUserByUsername(String userUsername) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userUsername);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    String phone = resultSet.getString("phone");
                    String profile_picture = resultSet.getString("profile_picture");
                    String id_front_photo = resultSet.getString("id_front_photo");
                    String id_back_photo = resultSet.getString("id_back_photo");
                    Property property = new PropertyDAO().getPropertyById(resultSet.getInt("property_id"));

                    user = new User(id, name, username, password, role, phone, profile_picture, id_front_photo, id_back_photo, property);
                }
            }
        }
        return user;
    }

    // Delete user by ID
    public boolean deleteUserById(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Add new user
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, username, password, role, phone, profile_picture, id_front_photo, id_back_photo, property_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getProfile_picture());
            statement.setString(7, user.getId_front_photo());
            statement.setString(8, user.getId_back_photo());
            if (user.getProperty() != null) {
                statement.setInt(9, user.getProperty().getId());
            } else {
                statement.setNull(9, java.sql.Types.INTEGER);
            }
            statement.executeUpdate();
        }
    }

    // Update user
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, username = ?, password = ?, role = ?, phone = ?, profile_picture = ?, id_front_photo = ?, id_back_photo = ?, property_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getProfile_picture());
            statement.setString(7, user.getId_front_photo());
            statement.setString(8, user.getId_back_photo());
            if (user.getProperty() != null) {
                statement.setInt(9, user.getProperty().getId());
            } else {
                statement.setNull(9, java.sql.Types.INTEGER);
            }
            statement.setInt(10, user.getId());
            statement.executeUpdate();
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
