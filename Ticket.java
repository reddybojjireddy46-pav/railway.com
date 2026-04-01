package com.railway.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket {
    private String pnr;
    private String passengerName;
    private String trainName;
    private String trainNumber;
    private String from;
    private String to;
    private String date;
    private String travelClass;
    private int price;
    private String status; // CONFIRMED or WL1, WL2...
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String bookingTime;
    private boolean cancelled;

    public Ticket(String pnr, String passengerName, String trainName, String trainNumber,
                  String from, String to, String date, String travelClass, int price,
                  String status, String departureTime, String arrivalTime, String duration) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.from = from;
        this.to = to;
        this.date = date;
        this.travelClass = travelClass;
        this.price = price;
        this.status = status;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.cancelled = false;
        this.bookingTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(new Date());
    }

    // Getters and Setters
    public String getPnr() { return pnr; }
    public String getPassengerName() { return passengerName; }
    public String getTrainName() { return trainName; }
    public String getTrainNumber() { return trainNumber; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDate() { return date; }
    public String getTravelClass() { return travelClass; }
    public int getPrice() { return price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDuration() { return duration; }
    public String getBookingTime() { return bookingTime; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isWaitlisted() {
        return status != null && status.startsWith("WL");
    }
}
