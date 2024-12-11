package database.dao;

import database.DatabaseConnection;
import database.model.House;
import database.model.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing houses in the database. Handles CRUD operations for
 * the House entity.
 *
 * Author: SAK
 */
public class HouseDAO {

    // Get all houses
    public List<House> getAllHouses() throws SQLException {
        List<House> houseList = new ArrayList<>();
        String sql = "SELECT * FROM houses";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                House house = extractHouseFromResultSet(resultSet);
                houseList.add(house);
            }
        }
        return houseList;
    }

    // Get house by ID
    public House getHouseById(int houseId) throws SQLException {
        String sql = "SELECT * FROM houses WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, houseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractHouseFromResultSet(resultSet);
                }
            }
        }
        return null; // Return null if not found
    }

    // Add new house
    public void addHouse(House house) throws SQLException {
        String sql = "INSERT INTO houses (name, property_id, location, type, fee, price, availability, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareHouseStatement(statement, house);
            statement.setString(8, house.getPhoto()); // Set the photo field
            statement.executeUpdate();
        }
    }

    // Update house
    public void updateHouse(House house) throws SQLException {
        String sql = "UPDATE houses SET name = ?, property_id = ?, location = ?, type = ?, fee = ?, price = ?, availability = ?, photo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareHouseStatement(statement, house);
            statement.setString(8, house.getPhoto()); // Set the photo field
            statement.setInt(9, house.getId()); // Update the house ID
            statement.executeUpdate();
        }
    }

    // Delete house by ID
    public boolean deleteHouseById(int houseId) throws SQLException {
        String sql = "DELETE FROM houses WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, houseId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Helper method to extract House from ResultSet
    private House extractHouseFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int propertyId = resultSet.getInt("property_id");
        String location = resultSet.getString("location");
        String type = resultSet.getString("type");
        double fee = resultSet.getDouble("fee");
        double price = resultSet.getDouble("price");
        String availability = resultSet.getString("availability");
        String photo = resultSet.getString("photo"); // Get the photo field

        Property property = new PropertyDAO().getPropertyById(propertyId); // Assuming PropertyDAO has a method getPropertyById
        return new House(id, name, property, location, type, fee, price, availability, photo); // Include photo
    }

    // Helper method to prepare PreparedStatement for House
    private void prepareHouseStatement(PreparedStatement statement, House house) throws SQLException {
        statement.setString(1, house.getName());
        if (house.getProperty() != null) {
            statement.setInt(2, house.getProperty().getId());
        } else {
            statement.setNull(2, java.sql.Types.INTEGER);
        }
        statement.setString(3, house.getLocation());
        statement.setString(4, house.getType());
        statement.setDouble(5, house.getFee());
        statement.setDouble(6, house.getPrice());
        statement.setString(7, house.getAvailability());
        // No need to set photo here; it's set in addHouse and updateHouse
    }
}
