package database.model;

/**
 *
 * @author SAK
 */
public class User {

    private int id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String phone;
    private String profile_picture;
    private String id_front_photo;
    private String id_back_photo;
    private Property property;

    public User(int id, String name, String username, String password, String role, String phone, String profile_picture, String id_front_photo, String id_back_photo, Property property) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.profile_picture = profile_picture;
        this.id_front_photo = id_front_photo;
        this.id_back_photo = id_back_photo;
        this.property = property;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getId_front_photo() {
        return id_front_photo;
    }

    public void setId_front_photo(String id_front_photo) {
        this.id_front_photo = id_front_photo;
    }

    public String getId_back_photo() {
        return id_back_photo;
    }

    public void setId_back_photo(String id_back_photo) {
        this.id_back_photo = id_back_photo;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", role=" + role + ", phone=" + phone + ", profile_picture=" + profile_picture + ", id_front_photo=" + id_front_photo + ", id_back_photo=" + id_back_photo + ", property=" + property + '}';
    }

}
