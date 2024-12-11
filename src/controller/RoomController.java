package controller;

import database.dao.RoomDAO;
import database.model.Room;
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

/**
 * Controller for Room operations
 */
public class RoomController {

    private RoomDAO roomDAO;

    public RoomController() throws SQLException {
        roomDAO = new RoomDAO();  // Instantiate DAO in the controller
    }

    // Get all rooms via the controller
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    // Get room by ID via the controller
    public Room getRoomById(int roomId) throws SQLException {
        return roomDAO.getRoomById(roomId);
    }

    // Add new room via the controller
    public void addRoom(Map<String, Object> roomAttributes) throws SQLException, IOException {
        String roomNumber = (String) roomAttributes.get("room_number");
        Property property = (Property) roomAttributes.get("property"); // Get the Property object
        String location = (String) roomAttributes.get("location");
        String type = (String) roomAttributes.get("type");
        double fee = (double) roomAttributes.get("fee");
        double price = (double) roomAttributes.get("price");
        String availability = (String) roomAttributes.get("availability");
        String photoPath = (String) roomAttributes.get("photoPath");

        String uniquePhotoFileName = null;

        if (photoPath != null) {
            // Handle photo file
            String photoFileExtension = photoPath.substring(photoPath.lastIndexOf("."));
            uniquePhotoFileName = UUID.randomUUID().toString() + photoFileExtension;
            Path photoDestinationPath = Paths.get("resources/images/rooms/" + uniquePhotoFileName);
            Files.copy(Paths.get(photoPath), photoDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create Room object
        Room newRoom = new Room(0, roomNumber, property, location, type, fee, price, availability, uniquePhotoFileName);
        roomDAO.addRoom(newRoom);
    }

    // Update room via the controller
    public void updateRoom(Map<String, Object> roomAttributes) throws SQLException, IOException {
        int id = (int) roomAttributes.get("id");
        String roomNumber = (String) roomAttributes.get("room_number");
        Property property = (Property) roomAttributes.get("property"); // Get the Property object
        String location = (String) roomAttributes.get("location");
        String type = (String) roomAttributes.get("type");
        double fee = (double) roomAttributes.get("fee");
        double price = (double) roomAttributes.get("price");
        String availability = (String) roomAttributes.get("availability");
        String photoPath = (String) roomAttributes.get("photoPath");

        // Retrieve current room to get existing photo path
        Room currentRoom = roomDAO.getRoomById(id);
        String uniquePhotoFileName = currentRoom.getPhoto(); // Assuming getPhoto() retrieves the photo filename

        // Handle photo file
        if (photoPath != null && !photoPath.equals("resources/images/rooms/" + currentRoom.getPhoto())) {
            // Delete existing photo file if it exists
            Files.deleteIfExists(Paths.get("resources/images/rooms/" + currentRoom.getPhoto()));
            String photoFileExtension = photoPath.substring(photoPath.lastIndexOf("."));
            uniquePhotoFileName = UUID.randomUUID().toString() + photoFileExtension;
            Path photoDestinationPath = Paths.get("resources/images/rooms/" + uniquePhotoFileName);
            Files.copy(Paths.get(photoPath), photoDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create Room object and update it in the database
        Room updatedRoom = new Room(id, roomNumber, property, location, type, fee, price, availability, uniquePhotoFileName);
        roomDAO.updateRoom(updatedRoom);
    }

    // Delete room by ID via the controller
    public boolean deleteRoomById(int roomId) throws SQLException, IOException {
        // Get room info to retrieve photo path
        Room room = roomDAO.getRoomById(roomId);
        if (room != null) {
            String photoPath = "resources/images/rooms/" + room.getPhoto();
            // Delete photo
            Files.deleteIfExists(Paths.get(photoPath));
        }
        return roomDAO.deleteRoomById(roomId);
    }

    public boolean room_numberExists(String room_number) throws SQLException {
        return roomDAO.room_numberExists(room_number);
    }

}
