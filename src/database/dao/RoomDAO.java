package database.dao;

import database.DatabaseConnection;
import database.model.Room;
import database.model.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing rooms in the database. Handles CRUD operations for the
 * Room entity.
 *
 * Author: SAK
 */
public class RoomDAO {

    // Get all rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> roomList = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Room room = extractRoomFromResultSet(resultSet);
                roomList.add(room);
            }
        }
        return roomList;
    }

    // Get room by ID
    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractRoomFromResultSet(resultSet);
                }
            }
        }
        return null; // Return null if not found
    }

    // Add new room
    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, property_id, location, type, fee, price, availability, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            addRoomStatement(statement, room);
            statement.executeUpdate();
        }
    }

    // Update room
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number = ?, property_id = ?, location = ?, type = ?, fee = ?, price = ?, availability = ?, photo = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            updateRoomStatement(statement, room);
            statement.executeUpdate();
        }
    }

    // Delete room by ID
    public boolean deleteRoomById(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Helper method to extract Room from ResultSet
    private Room extractRoomFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String roomNumber = resultSet.getString("room_number");
        int propertyId = resultSet.getInt("property_id");
        String location = resultSet.getString("location");
        String type = resultSet.getString("type");
        double fee = resultSet.getDouble("fee");
        double price = resultSet.getDouble("price");
        String availability = resultSet.getString("availability");
        String photo = resultSet.getString("photo"); // Get the photo field

        Property property = new PropertyDAO().getPropertyById(propertyId); // Assuming PropertyDAO has a method getPropertyById
        return new Room(id, roomNumber, property, location, type, fee, price, availability, photo); // Include photo
    }

    // Helper method to prepare PreparedStatement for adding Room
    private void addRoomStatement(PreparedStatement statement, Room room) throws SQLException {
        statement.setString(1, room.getRoom_number());
        if (room.getProperty() != null) {
            statement.setInt(2, room.getProperty().getId());
        } else {
            statement.setNull(2, java.sql.Types.INTEGER);
        }
        statement.setString(3, room.getLocation());
        statement.setString(4, room.getType());
        statement.setDouble(5, room.getFee());
        statement.setDouble(6, room.getPrice());
        statement.setString(7, room.getAvailability());
        statement.setString(8, room.getPhoto()); // Set the photo field
    }

    // Helper method to prepare PreparedStatement for updating Room
    private void updateRoomStatement(PreparedStatement statement, Room room) throws SQLException {
        statement.setString(1, room.getRoom_number());
        if (room.getProperty() != null) {
            statement.setInt(2, room.getProperty().getId());
        } else {
            statement.setNull(2, java.sql.Types.INTEGER);
        }
        statement.setString(3, room.getLocation());
        statement.setString(4, room.getType());
        statement.setDouble(5, room.getFee());
        statement.setDouble(6, room.getPrice());
        statement.setString(7, room.getAvailability());
        statement.setString(8, room.getPhoto()); // Set the photo field
        statement.setInt(9, room.getId()); // Update the room ID
    }

    public boolean room_numberExists(String room_number) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room_number);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
