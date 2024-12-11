package database.dao;

import database.DatabaseConnection;
import database.model.BookingItems;
import database.model.Booking;
import database.model.Room;
import database.model.House;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingItems DAO class for CRUD operations on the booking_items table.
 *
 * Handles database operations related to BookingItems.
 *
 * @author SAK
 */
public class BookingItemDAO {

    // Get all BookingItems
    public List<BookingItems> getAllBookingItems() throws SQLException {
        List<BookingItems> bookingItemsList = new ArrayList<>();
        String sql = "SELECT * FROM booking_items";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BookingItems bookingItem = extractBookingItemFromResultSet(resultSet);
                bookingItemsList.add(bookingItem);
            }
        }
        return bookingItemsList;
    }

    // Get a BookingItem by ID
    public BookingItems getBookingItemById(int id) throws SQLException {
        String sql = "SELECT * FROM booking_items WHERE id = ?";
        BookingItems bookingItem = null;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bookingItem = extractBookingItemFromResultSet(resultSet);
                }
            }
        }
        return bookingItem;
    }

    // Add a new BookingItem
    public void addBookingItem(BookingItems bookingItem) throws SQLException {
        String sql = "INSERT INTO booking_items (booking_id, check_in_date, check_out_date, paid_date, status, room_id, house_id, fee, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingItem.getBooking().getId());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(bookingItem.getCheck_in_date()));
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(bookingItem.getCheck_out_date()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(bookingItem.getPaid_date()));
            statement.setString(5, bookingItem.getStatus());
            if (bookingItem.getRoom() != null) {
                statement.setInt(6, bookingItem.getRoom().getId()); // Set room ID if room is not null
            } else {
                statement.setNull(6, java.sql.Types.INTEGER); // Set NULL if room is null
            }

            if (bookingItem.getHouse() != null) {
                statement.setInt(7, bookingItem.getHouse().getId()); // Set house ID if house is not null
            } else {
                statement.setNull(7, java.sql.Types.INTEGER); // Set NULL if house is null
            }
            statement.setDouble(8, bookingItem.getFee());
            statement.setDouble(9, bookingItem.getPrice());

            statement.executeUpdate();
        }
    }

    // Update an existing BookingItem
    public void updateBookingItem(BookingItems bookingItem) throws SQLException {
        String sql = "UPDATE booking_items SET booking_id = ?, check_in_date = ?, check_out_date = ?, paid_date = ?, status = ?, room_id = ?, house_id = ?, fee = ?, price = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingItem.getBooking().getId());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(bookingItem.getCheck_in_date()));
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(bookingItem.getCheck_out_date()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(bookingItem.getPaid_date()));
            statement.setString(5, bookingItem.getStatus());
            statement.setInt(6, bookingItem.getRoom().getId());
            statement.setInt(7, bookingItem.getHouse().getId());
            statement.setDouble(8, bookingItem.getFee());
            statement.setDouble(9, bookingItem.getPrice());
            statement.setInt(10, bookingItem.getId());

            statement.executeUpdate();
        }
    }

    // Delete a BookingItem by ID
    public boolean deleteBookingItemById(int id) throws SQLException {
        String sql = "DELETE FROM booking_items WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Helper method to extract BookingItems from ResultSet
    private BookingItems extractBookingItemFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int bookingId = resultSet.getInt("booking_id");
        LocalDateTime checkInDate = resultSet.getTimestamp("check_in_date").toLocalDateTime();
        LocalDateTime checkOutDate = resultSet.getTimestamp("check_out_date").toLocalDateTime();
        LocalDateTime paidDate = resultSet.getTimestamp("paid_date").toLocalDateTime();
        String status = resultSet.getString("status");
        int roomId = resultSet.getInt("room_id");
        int houseId = resultSet.getInt("house_id");
        double fee = resultSet.getDouble("fee");
        double price = resultSet.getDouble("price");

        // Assuming you have DAO methods to get Booking, Room, and House by ID
        Booking booking = new BookingDAO().getBookingById(bookingId);
        Room room = new RoomDAO().getRoomById(roomId);
        House house = new HouseDAO().getHouseById(houseId);

        return new BookingItems(id, booking, checkInDate, checkOutDate, paidDate, status, room, house, fee, price);
    }
}
