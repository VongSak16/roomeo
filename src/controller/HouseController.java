package controller;

import database.dao.HouseDAO;
import database.model.House;
import database.model.Property;
import database.model.User;

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
 * Controller for House operations
 */
public class HouseController {

    private HouseDAO houseDAO;

    public HouseController() throws SQLException {
        houseDAO = new HouseDAO();  // Instantiate DAO in the controller
    }

    // Get all houses via the controller
    public List<House> getAllHouses() throws SQLException {
        return houseDAO.getAllHouses();
    }

    // Get house by ID via the controller
    public House getHouseById(int houseId) throws SQLException {
        return houseDAO.getHouseById(houseId);
    }

    // Add new house via the controller
    public void addHouse(Map<String, Object> houseAttributes) throws SQLException, IOException {
        String name = (String) houseAttributes.get("name");
        Property property = (Property) houseAttributes.get("property"); // Get the Property object
        String location = (String) houseAttributes.get("location");
        String type = (String) houseAttributes.get("type");
        double fee = (double) houseAttributes.get("fee");
        double price = (double) houseAttributes.get("price");
        String availability = (String) houseAttributes.get("availability");
        String photoPath = (String) houseAttributes.get("photoPath");

        String uniquePhotoFileName = null;

        if (photoPath != null) {
            // Handle photo file
            String photoFileExtension = photoPath.substring(photoPath.lastIndexOf("."));
            uniquePhotoFileName = UUID.randomUUID().toString() + photoFileExtension;
            Path photoDestinationPath = Paths.get("resources/images/houses/" + uniquePhotoFileName);
            Files.copy(Paths.get(photoPath), photoDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create House object
        House newHouse = new House(0, name, property, location, type, fee, price, availability, uniquePhotoFileName);
        houseDAO.addHouse(newHouse);
    }

    // Update house via the controller
    public void updateHouse(Map<String, Object> houseAttributes) throws SQLException, IOException {
        int id = (int) houseAttributes.get("id");
        String name = (String) houseAttributes.get("name");
        Property property = (Property) houseAttributes.get("property"); // Get the Property object
        String location = (String) houseAttributes.get("location");
        String type = (String) houseAttributes.get("type");
        double fee = (double) houseAttributes.get("fee");
        double price = (double) houseAttributes.get("price");
        String availability = (String) houseAttributes.get("availability");
        String photoPath = (String) houseAttributes.get("photoPath");

        // Retrieve current house to get existing photo path
        House currentHouse = houseDAO.getHouseById(id);
        String uniquePhotoFileName = currentHouse.getPhoto(); // Assuming getPhoto() retrieves the photo filename

        // Handle photo file
        if (photoPath != null && !photoPath.equals("resources/images/houses/" + currentHouse.getPhoto())) {
            // Delete existing photo file if it exists
            Files.deleteIfExists(Paths.get("resources/images/houses/" + currentHouse.getPhoto()));
            String photoFileExtension = photoPath.substring(photoPath.lastIndexOf("."));
            uniquePhotoFileName = UUID.randomUUID().toString() + photoFileExtension;
            Path photoDestinationPath = Paths.get("resources/images/houses/" + uniquePhotoFileName);
            Files.copy(Paths.get(photoPath), photoDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create House object and update it in the database
        House updatedHouse = new House(id, name, property, location, type, fee, price, availability, uniquePhotoFileName);
        houseDAO.updateHouse(updatedHouse);
    }

    // Delete house by ID via the controller
    public boolean deleteHouseById(int houseId) throws SQLException, IOException {
        // Get house info to retrieve photo path
        House house = houseDAO.getHouseById(houseId);
        if (house != null) {
            String photoPath = "resources/images/houses/" + house.getPhoto();
            // Delete photo
            Files.deleteIfExists(Paths.get(photoPath));
        }
        return houseDAO.deleteHouseById(houseId);
    }
}
