package database.dao;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.model.Tenant;
import java.sql.ResultSet;

/**
 * * @author SAK
 */
public class TenantDAO {

    // Get all tenants
    public List<Tenant> getAllTenants() throws SQLException {
        List<Tenant> tenantList = new ArrayList<>();
        String sql = "SELECT * FROM tenants";

        // Using try-with-resources to automatically close resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String id_type = resultSet.getString("id_type");
                String id_front_photo = resultSet.getString("id_front_photo");
                String id_back_photo = resultSet.getString("id_back_photo");
                Tenant tenant = new Tenant(id, name, phone, id_type, id_front_photo, id_back_photo);
                tenantList.add(tenant);
            }
        }
        return tenantList;
    }

    // Get tenant by ID
    public Tenant getTenantById(int tenantId) throws SQLException {
        Tenant tenant = null;
        String sql = "SELECT * FROM tenants WHERE id = ?";

        // Using try-with-resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tenantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone");
                    String id_type = resultSet.getString("id_type");
                    String id_front_photo = resultSet.getString("id_front_photo");
                    String id_back_photo = resultSet.getString("id_back_photo");
                    tenant = new Tenant(id, name, phone, id_type, id_front_photo, id_back_photo);
                }
            }
        }
        return tenant;
    }

    // Delete tenant by ID
    public boolean deleteTenantById(int tenantId) throws SQLException {
        String sql = "DELETE FROM tenants WHERE id = ?";

        // Using try-with-resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tenantId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Add new tenant
    public void addTenant(Tenant tenant) throws SQLException {
        String sql = "INSERT INTO tenants (name, phone, id_type, id_front_photo, id_back_photo) VALUES (?, ?, ?, ?, ?)";

        // Using try-with-resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tenant.getName());
            statement.setString(2, tenant.getPhone());
            statement.setString(3, tenant.getId_type());
            statement.setString(4, tenant.getId_front_photo());
            statement.setString(5, tenant.getId_back_photo());
            statement.executeUpdate();
        }
    }

    // Update tenant
    public void updateTenant(Tenant tenant) throws SQLException {
        String sql = "UPDATE tenants SET name = ?, phone = ?, id_type = ?, id_front_photo = ?, id_back_photo = ? WHERE id = ?";

        // Using try-with-resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tenant.getName());
            statement.setString(2, tenant.getPhone());
            statement.setString(3, tenant.getId_type());
            statement.setString(4, tenant.getId_front_photo());
            statement.setString(5, tenant.getId_back_photo());
            statement.setInt(6, tenant.getId());
            statement.executeUpdate();
        }
    }

    // Get the count of all tenants
    public int countAllTenants() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tenants";
        int tenantCount = 0;

        // Using try-with-resources
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                tenantCount = resultSet.getInt(1); // The first column contains the count
            }
        }
        return tenantCount;
    }
}
