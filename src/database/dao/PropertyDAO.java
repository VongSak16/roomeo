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

/**
 * DAO class for managing properties in the database. Handles CRUD operations
 * for the Property entity.
 *
 * Author: SAK
 */
public class PropertyDAO {

    // Get all properties
    public List<Property> getAllProperties() throws SQLException {
        List<Property> propertyList = new ArrayList<>();
        String sql = "SELECT * FROM properties";

        // Use try-with-resources to ensure proper closure of resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Property property = extractPropertyFromResultSet(resultSet);
                propertyList.add(property);
            }
        }
        return propertyList;
    }

    // Get property by ID
    public Property getPropertyById(int propertyId) throws SQLException {
        String sql = "SELECT * FROM properties WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, propertyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPropertyFromResultSet(resultSet);
                }
            }
        }
        return null; // Return null if not found
    }

    // Add new property
    public void addProperty(Property property) throws SQLException {
        String sql = "INSERT INTO properties (name, logo, info, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            preparePropertyStatement(statement, property);
            statement.executeUpdate();
        }
    }

    // Update property
    public void updateProperty(Property property) throws SQLException {
        String sql = "UPDATE properties SET name = ?, logo = ?, info = ?, owner_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            preparePropertyStatement(statement, property);
            statement.setInt(5, property.getId());
            statement.executeUpdate();
        }
    }

    // Delete property by ID
    public boolean deletePropertyById(int propertyId) throws SQLException {
        String sql = "DELETE FROM properties WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, propertyId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Helper method to extract Property from ResultSet
    private Property extractPropertyFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String logo = resultSet.getString("logo");
        String info = resultSet.getString("info");
        int ownerId = resultSet.getInt("owner_id");

        User owner = new UserDAO().getUserById(ownerId); // Assuming UserDAO has a method getUserById
        return new Property(id, name, logo, info, owner);
    }

    // Helper method to prepare PreparedStatement for Property
    private void preparePropertyStatement(PreparedStatement statement, Property property) throws SQLException {
        statement.setString(1, property.getName());
        statement.setString(2, property.getLogo());
        statement.setString(3, property.getInfo());
        if (property.getOwner() != null) {
            statement.setInt(4, property.getOwner().getId());
        } else {
            statement.setNull(4, java.sql.Types.INTEGER);
        }
    }
}
