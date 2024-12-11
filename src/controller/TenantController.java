package controller;

import database.dao.TenantDAO;
import database.model.Tenant;
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
 * Controller for Tenant operations
 */
public class TenantController {

    private TenantDAO tenantDAO;

    public TenantController() throws SQLException {
        tenantDAO = new TenantDAO();  // Instantiate DAO in the controller
    }

    // Get all tenants via the controller
    public List<Tenant> getAllTenants() throws SQLException {
        return tenantDAO.getAllTenants();
    }

    public boolean deleteTenantById(int tenantId) throws SQLException, IOException {
        // Get tenant info to retrieve photo paths
        Tenant tenant = tenantDAO.getTenantById(tenantId);
        if (tenant != null) {
            String frontPhotoPath = "resources/images/tenants/" + tenant.getId_front_photo();
            String backPhotoPath = "resources/images/tenants/" + tenant.getId_back_photo();

            // Delete photos
            Files.deleteIfExists(Paths.get(frontPhotoPath));
            Files.deleteIfExists(Paths.get(backPhotoPath));
        }
        return tenantDAO.deleteTenantById(tenantId);
    }

    // Get tenant by ID via the controller
    public Tenant getTenantById(int tenantId) throws SQLException {
        return tenantDAO.getTenantById(tenantId);
    }

    // Add new tenant via the controller
    public void addTenant(Map<String, Object> tenantAttributes) throws SQLException, IOException {
        String name = (String) tenantAttributes.get("name");
        String phone = (String) tenantAttributes.get("phone");
        String idType = (String) tenantAttributes.get("idType");
        String frontPhotoPath = (String) tenantAttributes.get("frontPhotoPath");
        String backPhotoPath = (String) tenantAttributes.get("backPhotoPath");

        // Handle photo files
        String frontFileExtension = frontPhotoPath.substring(frontPhotoPath.lastIndexOf("."));
        String backFileExtension = backPhotoPath.substring(backPhotoPath.lastIndexOf("."));
        String uniqueFrontFileName = UUID.randomUUID().toString() + frontFileExtension;
        String uniqueBackFileName = UUID.randomUUID().toString() + backFileExtension;
        Path frontDestinationPath = Paths.get("resources/images/tenants/" + uniqueFrontFileName);
        Path backDestinationPath = Paths.get("resources/images/tenants/" + uniqueBackFileName);

        Files.copy(Paths.get(frontPhotoPath), frontDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(backPhotoPath), backDestinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Create Tenant object
        Tenant newTenant = new Tenant(0, name, phone, idType, uniqueFrontFileName, uniqueBackFileName);
        tenantDAO.addTenant(newTenant);
    }

    public void updateTenant(Map<String, Object> tenantAttributes) throws SQLException, IOException {
        int id = (int) tenantAttributes.get("id");
        String name = (String) tenantAttributes.get("name");
        String phone = (String) tenantAttributes.get("phone");
        String idType = (String) tenantAttributes.get("idType");
        String frontPhotoPath = (String) tenantAttributes.get("frontPhotoPath");
        String backPhotoPath = (String) tenantAttributes.get("backPhotoPath");

        // Retrieve current tenant to get existing photo paths
        Tenant currentTenant = tenantDAO.getTenantById(id);
        String uniqueFrontFileName = currentTenant.getId_front_photo();
        String uniqueBackFileName = currentTenant.getId_back_photo();

        // Handle front photo
        if (!frontPhotoPath.equals("resources/images/tenants/" + currentTenant.getId_front_photo())) {
            Files.deleteIfExists(Paths.get("resources/images/tenants/" + currentTenant.getId_front_photo()));

            String frontFileExtension = frontPhotoPath.substring(frontPhotoPath.lastIndexOf("."));
            uniqueFrontFileName = UUID.randomUUID().toString() + frontFileExtension;
            Path frontDestinationPath = Paths.get("resources/images/tenants/" + uniqueFrontFileName);
            Files.copy(Paths.get(frontPhotoPath), frontDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Handle back photo
        if (!backPhotoPath.equals("resources/images/tenants/" + currentTenant.getId_back_photo())) {
            Files.deleteIfExists(Paths.get("resources/images/tenants/" + currentTenant.getId_back_photo()));

            String backFileExtension = backPhotoPath.substring(backPhotoPath.lastIndexOf("."));
            uniqueBackFileName = UUID.randomUUID().toString() + backFileExtension;
            Path backDestinationPath = Paths.get("resources/images/tenants/" + uniqueBackFileName);
            Files.copy(Paths.get(backPhotoPath), backDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create Tenant object and update it in the database
        Tenant updatedTenant = new Tenant(id, name, phone, idType, uniqueFrontFileName, uniqueBackFileName);
        tenantDAO.updateTenant(updatedTenant);
    }
    
}
