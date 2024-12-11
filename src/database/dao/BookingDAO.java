package database.dao;

import database.DatabaseConnection;
import database.model.Booking;
import database.model.Tenant;
import database.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking DAO class for CRUD operations on the Booking table.
 *
 * @author SAK
 */
public class BookingDAO {

    // Get all bookings
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookingList = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                int tenantId = resultSet.getInt("tenant_id");
                LocalDateTime bookingDate = resultSet.getTimestamp("booking_date").toLocalDateTime();
                String status = resultSet.getString("status");
                double totalFee = resultSet.getDouble("total_fee");
                double totalPrice = resultSet.getDouble("total_price");

                // Assuming you have a method to get User and Tenant by their ID
                User user = new UserDAO().getUserById(userId);  // You need to implement this method
                Tenant tenant = new TenantDAO().getTenantById(tenantId);

                Booking booking = new Booking(id, user, tenant, bookingDate, status, totalFee, totalPrice);
                bookingList.add(booking);
            }
        }
        return bookingList;
    }

    // Get a booking by ID
    public Booking getBookingById(int bookingId) throws SQLException {
        Booking booking = null;
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int userId = resultSet.getInt("user_id");
                    int tenantId = resultSet.getInt("tenant_id");
                    LocalDateTime bookingDate = resultSet.getTimestamp("booking_date").toLocalDateTime();
                    String status = resultSet.getString("status");
                    double totalFee = resultSet.getDouble("total_fee");
                    double totalPrice = resultSet.getDouble("total_price");

                    // Assuming you have methods to fetch User and Tenant objects
                    User user = new UserDAO().getUserById(userId);
                    Tenant tenant = new TenantDAO().getTenantById(tenantId);

                    booking = new Booking(id, user, tenant, bookingDate, status, totalFee, totalPrice);
                }
            }
        }
        return booking;
    }

    public int addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (user_id, tenant_id, booking_date, status, total_fee, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = 0;

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getTenant().getId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getBooking_date()));
            statement.setString(4, booking.getStatus());
            statement.setDouble(5, booking.getTotal_fee());
            statement.setDouble(6, booking.getTotal_price());

            // Execute the insertion
            statement.executeUpdate();

            // Retrieve the generated booking ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    booking.setId(generatedId); // Set the ID back to the booking object
                }
            }
        }

        return generatedId;
    }

    // Update an existing booking
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET user_id = ?, tenant_id = ?, booking_date = ?, status = ?, total_fee = ?, total_price = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, booking.getUser().getId());
            statement.setInt(2, booking.getTenant().getId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getBooking_date()));
            statement.setString(4, booking.getStatus());
            statement.setDouble(5, booking.getTotal_fee());
            statement.setDouble(6, booking.getTotal_price());
            statement.setInt(7, booking.getId());

            statement.executeUpdate();
        }
    }

    // Delete a booking by ID
    public boolean deleteBookingById(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookingId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
