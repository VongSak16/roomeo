/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.model;

/**
 *
 * @author SAK
 */
public class House implements Rental {

    private int id;
    private String name;
    private Property property;
    private String location;
    private String type;
    private double fee;
    private double price;
    private String availability;
    private String photo;

    public House(int id, String name, Property property, String location, String type, double fee, double price, String availability, String photo) {
        this.id = id;
        this.name = name;
        this.property = property;
        this.location = location;
        this.type = type;
        this.fee = fee;
        this.price = price;
        this.availability = availability;
        this.photo = photo;
    }

    @Override
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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "House{" + "id=" + id + ", name=" + name + ", property=" + property + ", location=" + location + ", type=" + type + ", fee=" + fee + ", price=" + price + ", availability=" + availability + ", photo=" + photo + '}';
    }
}
