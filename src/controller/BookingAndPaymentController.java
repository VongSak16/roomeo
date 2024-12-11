/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.dao.BookingDAO;
import database.dao.BookingItemDAO;
import database.dao.HouseDAO;
import database.dao.RoomDAO;
import database.model.Booking;
import database.model.BookingItems;
import database.model.House;
import database.model.Rental;
import database.model.Room;
import database.model.Tenant;
import database.model.User;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SAK
 */
public class BookingAndPaymentController {

    private HouseDAO houseDAO;
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;
    private BookingItemDAO bookingItemDAO; // Assuming you'll create this DAO

    public BookingAndPaymentController() throws SQLException {
        houseDAO = new HouseDAO();
        roomDAO = new RoomDAO();
        bookingDAO = new BookingDAO();
        bookingItemDAO = new BookingItemDAO();
    }

    // Get all houses via the controller
    public List<House> getAllHouses() throws SQLException {
        return houseDAO.getAllHouses();
    }

    // Get all houses via the controller
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public List<Rental> getAllRentals() throws SQLException {
        List<Rental> allRentals = new ArrayList<>();

        // Get all houses and add them to the list
        List<House> houses = houseDAO.getAllHouses();
        allRentals.addAll(houses);

        // Get all rooms and add them to the list
        List<Room> rooms = roomDAO.getAllRooms();
        allRentals.addAll(rooms);

        return allRentals; // Return combined list of houses and rooms as Rentals
    }

    // Get rental (house or room) by ID (improved)
    public Rental getRentalByID(int id) throws SQLException {
        // Attempt to retrieve a Room
        Room room = roomDAO.getRoomById(id);
        if (room != null) {
            return room; // Return Room if found
        }

        // If no Room is found, attempt to retrieve a House 
        House house = houseDAO.getHouseById(id);
        if (house != null) {
            return house; // Return House if found
        }

        // If neither is found, return null or throw an exception
        return null; // or throw new NoSuchElementException("Rental not found");
    }

    public int addBooking(Booking booking) throws SQLException {
        // Get the booking details from the booking object
        User user = booking.getUser();
        Tenant tenant = booking.getTenant();
        LocalDateTime bookingDate = booking.getBooking_date();
        String status = booking.getStatus();
        double totalFee = booking.getTotal_fee();
        double totalPrice = booking.getTotal_price();

        // You can use the booking object directly to add it to the database
        int bookingId = bookingDAO.addBooking(booking);

        // Return the generated booking ID
        return bookingId;
    }

    // Method to add booking items for a booking
    public void addBookingItems(Booking booking, List<BookingItems> bookingItems) throws SQLException {
        for (BookingItems item : bookingItems) {
            item.setBooking(booking); // Associate booking with each item
            bookingItemDAO.addBookingItem(item);
        }
    }

}
