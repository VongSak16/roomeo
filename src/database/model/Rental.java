/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.model;

/**
 *
 * @author SAK
 */
public interface Rental {

    int getId();

    double getFee();

    double getPrice();

    String getType();

    String getLocation();

    String getAvailability();
}
