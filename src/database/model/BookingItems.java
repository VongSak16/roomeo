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
public class BookingItems {

    private int id;
    private Booking booking;
    private LocalDateTime check_in_date;
    private LocalDateTime check_out_date;
    private LocalDateTime paid_date;
    private String status;
    private Room room;
    private House house;
    private double fee;
    private double price;

    public BookingItems(int id, Booking booking, LocalDateTime check_in_date, LocalDateTime check_out_date, LocalDateTime paid_date, String status, Room room, House house, double fee, double price) {
        this.id = id;
        this.booking = booking;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.paid_date = paid_date;
        this.status = status;
        this.room = room;
        this.house = house;
        this.fee = fee;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDateTime getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(LocalDateTime check_in_date) {
        this.check_in_date = check_in_date;
    }

    public LocalDateTime getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(LocalDateTime check_out_date) {
        this.check_out_date = check_out_date;
    }

    public LocalDateTime getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(LocalDateTime paid_date) {
        this.paid_date = paid_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookingItems{" + "id=" + id + ", booking=" + booking + ", check_in_date=" + check_in_date + ", check_out_date=" + check_out_date + ", paid_date=" + paid_date + ", status=" + status + ", room=" + room + ", house=" + house + ", fee=" + fee + ", price=" + price + '}';
    }

}
