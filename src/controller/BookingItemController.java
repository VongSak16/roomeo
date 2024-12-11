package controller;

import database.dao.BookingDAO;
import database.model.Booking;
import database.model.BookingItems;
import database.model.House;
import database.model.Room;
import database.model.Tenant;
import database.model.User;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for Booking operations
 */
public class BookingItemController {

    private BookingDAO bookingDAO;

    public BookingItemController() throws SQLException {
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

    //Add a new booking item
    public void addBookingItem(Map<String, Object> bookingItemAttributes) throws SQLException {
        // Extract attributes from the Map 
        Booking booking = (Booking) bookingItemAttributes.get("booking");
        LocalDateTime checkInDate = (LocalDateTime) bookingItemAttributes.get("check_in_date");
        LocalDateTime checkOutDate = (LocalDateTime) bookingItemAttributes.get("check_out_date");
        LocalDateTime paidDate = (LocalDateTime) bookingItemAttributes.get("paid_date");
        String status = (String) bookingItemAttributes.get("status");
        Room room = (Room) bookingItemAttributes.get("room");
        House house = (House) bookingItemAttributes.get("house");
        double fee = (double) bookingItemAttributes.get("fee");
        double price = (double) bookingItemAttributes.get("price");

        // Create BookingItems object (assuming constructor like this)
        BookingItems newBookingItem = new BookingItems(0, booking, checkInDate, checkOutDate, paidDate, status, room, house, fee, price);
        bookingItemDAO.addBookingItem(newBookingItem);
    }
}
