/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.model;

/**
 *
 * @author SAK
 */
public class Property {

    private int id;
    private String name;
    private String logo;
    private String info;
    private User owner;

    public Property(int id, String name, String logo, String info, User owner) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.info = info;
        this.owner = owner;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Property{" + "id=" + id + ", name=" + name + ", logo=" + logo + ", info=" + info + ", owner=" + owner + '}';
    }
}
