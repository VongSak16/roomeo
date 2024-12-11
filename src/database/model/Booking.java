/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database.model;

import java.time.LocalDateTime;

/**
 *
 * @author SAK
 */
public class Booking {

    private int id;
    private User user;
    private Tenant tenant;
    private LocalDateTime booking_date;
    private String status;
    private double total_fee;
    private double total_price;

    public Booking(int id, User user, Tenant tenant, LocalDateTime booking_date, String status, double total_fee, double total_price) {
        this.id = id;
        this.user = user;
        this.tenant = tenant;
        this.booking_date = booking_date;
        this.status = status;
        this.total_fee = total_fee;
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public LocalDateTime getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(LocalDateTime booking_date) {
        this.booking_date = booking_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Booking{" + "id=" + id + ", user=" + user + ", tenant=" + tenant + ", booking_date=" + booking_date + ", status=" + status + ", total_fee=" + total_fee + ", total_price=" + total_price + '}';
    }
}
