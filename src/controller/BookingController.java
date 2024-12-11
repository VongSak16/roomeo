package controller;

import database.dao.BookingDAO;
import database.model.Booking;
import database.model.Tenant;
import database.model.User;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for Booking operations
 */
public class BookingController {

    private BookingDAO bookingDAO;

    public BookingController() throws SQLException {
        bookingDAO = new BookingDAO(); // Instantiate DAO for Booking
    }

    // Get all bookings via the controller
    public List<Booking> getAllBookings() throws SQLException {
        return bookingDAO.getAllBookings();
    }

    // Get booking by ID via the controller
    public Booking getBookingById(int bookingId) throws SQLException {
        return bookingDAO.getBookingById(bookingId);
    }

    // Delete booking by ID via the controller
    public boolean deleteBookingById(int bookingId) throws SQLException {
        return bookingDAO.deleteBookingById(bookingId);
    }

    // Add a new booking via the controller
    public void addBooking(Map<String, Object> bookingAttributes) throws SQLException {
        User user = (User) bookingAttributes.get("user");
        Tenant tenant = (Tenant) bookingAttributes.get("tenant");
        LocalDateTime bookingDate = (LocalDateTime) bookingAttributes.get("booking_date");
        String status = (String) bookingAttributes.get("status");
        double totalFee = (double) bookingAttributes.get("total_fee");
        double totalPrice = (double) bookingAttributes.get("total_price");

        // Create Booking object
        Booking newBooking = new Booking(0, user, tenant, bookingDate, status, totalFee, totalPrice);
        bookingDAO.addBooking(newBooking);
    }

    // Update booking via the controller
    public void updateBooking(Map<String, Object> bookingAttributes) throws SQLException {
        int id = (int) bookingAttributes.get("id");
        User user = (User) bookingAttributes.get("user");
        Tenant tenant = (Tenant) bookingAttributes.get("tenant");
        LocalDateTime bookingDate = (LocalDateTime) bookingAttributes.get("booking_date");
        String status = (String) bookingAttributes.get("status");
        double totalFee = (double) bookingAttributes.get("total_fee");
        double totalPrice = (double) bookingAttributes.get("total_price");

        // Create updated Booking object
        Booking updatedBooking = new Booking(id, user, tenant, bookingDate, status, totalFee, totalPrice);
        bookingDAO.updateBooking(updatedBooking);
    }
}
