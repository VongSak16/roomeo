package database.model;

/**
 *
 * @author SAK
 */
public class Tenant {

    private int id;
    private String name;
    private String phone;
    private String id_type;
    private String id_front_photo;
    private String id_back_photo;

    public Tenant(int id, String name, String phone, String id_type, String id_front_photo, String id_back_photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.id_type = id_type;
        this.id_front_photo = id_front_photo;
        this.id_back_photo = id_back_photo;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
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

    @Override
    public String toString() {
        return "Tenant{" + "id=" + id + ", name=" + name + ", phone=" + phone + ", id_type=" + id_type + ", id_front_photo=" + id_front_photo + ", id_back_photo=" + id_back_photo + '}';
    }

}
