package controller;

import database.dao.PropertyDAO;
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
 * Controller for Property operations
 */
public class PropertyController {

    private PropertyDAO propertyDAO;

    public PropertyController() throws SQLException {
        propertyDAO = new PropertyDAO();  // Instantiate DAO in the controller
    }
    // Get all properties via the controller
    public List<Property> getAllProperties() throws SQLException {
        return propertyDAO.getAllProperties();
    }

    // Get property by ID via the controller
    public Property getPropertyById(int propertyId) throws SQLException {
        return propertyDAO.getPropertyById(propertyId);
    }

    // Add new property via the controller
    public void addProperty(Map<String, Object> propertyAttributes) throws SQLException, IOException {
        String name = (String) propertyAttributes.get("name");
        String logoPath = (String) propertyAttributes.get("logoPath");
        String info = (String) propertyAttributes.get("info");
        User owner = (User) propertyAttributes.get("owner");

        String uniqueLogoFileName = null;

        if (logoPath != null) {
            // Handle logo file
            String logoFileExtension = logoPath.substring(logoPath.lastIndexOf("."));
            uniqueLogoFileName = UUID.randomUUID().toString() + logoFileExtension;
            Path logoDestinationPath = Paths.get("resources/images/properties/" + uniqueLogoFileName);
            Files.copy(Paths.get(logoPath), logoDestinationPath, StandardCopyOption.REPLACE_EXISTING);

        }
        // Create Property object
        Property newProperty = new Property(0, name, uniqueLogoFileName, info, owner);
        propertyDAO.addProperty(newProperty);
    }

    // Update property via the controller
    public void updateProperty(Map<String, Object> propertyAttributes) throws SQLException, IOException {
        int id = (int) propertyAttributes.get("id");
        String name = (String) propertyAttributes.get("name");
        String logoPath = (String) propertyAttributes.get("logoPath");
        String info = (String) propertyAttributes.get("info");
        User owner = (User) propertyAttributes.get("owner");

        // Retrieve current property to get existing logo path
        Property currentProperty = propertyDAO.getPropertyById(id);
        String uniqueLogoFileName = currentProperty.getLogo();

        // Handle logo file
        if (logoPath != null && !logoPath.equals("resources/images/properties/" + currentProperty.getLogo())) {
            Files.deleteIfExists(Paths.get("resources/images/properties/" + currentProperty.getLogo()));
            String logoFileExtension = logoPath.substring(logoPath.lastIndexOf("."));
            uniqueLogoFileName = UUID.randomUUID().toString() + logoFileExtension;
            Path logoDestinationPath = Paths.get("resources/images/properties/" + uniqueLogoFileName);
            Files.copy(Paths.get(logoPath), logoDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create Property object and update it in the database
        Property updatedProperty = new Property(id, name, uniqueLogoFileName, info, owner);
        propertyDAO.updateProperty(updatedProperty);
    }

    // Delete property by ID via the controller
    public boolean deletePropertyById(int propertyId) throws SQLException, IOException {
        // Get property info to retrieve logo path
        Property property = propertyDAO.getPropertyById(propertyId);
        if (property != null) {
            String logoPath = "resources/images/properties/" + property.getLogo();
            // Delete logo
            Files.deleteIfExists(Paths.get(logoPath));
        }
        return propertyDAO.deletePropertyById(propertyId);
    }
}
