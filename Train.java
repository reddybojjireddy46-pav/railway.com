package com.railway.model;

import java.util.*;

public class Train {
    private String trainName;
    private String trainNumber;
    private String from;
    private String to;
    private String departureTime;
    private String arrivalTime;
    private String duration;

    // Each class: 1A, 2A, 3A, SL
    private Map<String, Integer> prices = new HashMap<>();
    private Map<String, Integer> confirmedSeats = new HashMap<>();
    private Map<String, Queue<String>> waitingLists = new HashMap<>();

    public static final int TOTAL_SEATS = 10;
    public static final String[] CLASSES = {"1A", "2A", "3A", "SL"};

    public Train(String trainName, String trainNumber, String from, String to,
                 String departureTime, String arrivalTime, String duration,
                 int price1A, int price2A, int price3A, int priceSL) {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;

        prices.put("1A", price1A);
        prices.put("2A", price2A);
        prices.put("3A", price3A);
        prices.put("SL", priceSL);

        for (String cls : CLASSES) {
            confirmedSeats.put(cls, 0);
            waitingLists.put(cls, new ArrayDeque<>());
        }
    }

    public String getTrainName() { return trainName; }
    public String getTrainNumber() { return trainNumber; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDuration() { return duration; }
    public Map<String, Integer> getPrices() { return prices; }
    public int getPrice(String cls) { return prices.getOrDefault(cls, 0); }

    public int getConfirmedSeats(String cls) {
        return confirmedSeats.getOrDefault(cls, 0);
    }

    public int getAvailableSeats(String cls) {
        return TOTAL_SEATS - confirmedSeats.getOrDefault(cls, 0);
    }

    public Queue<String> getWaitingList(String cls) {
        return waitingLists.get(cls);
    }

    public int getWaitingListSize(String cls) {
        return waitingLists.getOrDefault(cls, new ArrayDeque<>()).size();
    }

    public String getAvailabilityStatus(String cls) {
        int available = getAvailableSeats(cls);
        int wl = getWaitingListSize(cls);
        if (available > 0) return "Available (" + available + ")";
        if (wl > 0) return "WL " + wl;
        return "Not Available";
    }

    public String getAvailabilityClass(String cls) {
        int available = getAvailableSeats(cls);
        int wl = getWaitingListSize(cls);
        if (available > 0) return "available";
        if (wl > 0) return "waitlist";
        return "notavailable";
    }

    // Book a seat: returns WL number or 0 if confirmed
    public synchronized String bookSeat(String cls, String pnr) {
        int confirmed = confirmedSeats.getOrDefault(cls, 0);
        if (confirmed < TOTAL_SEATS) {
            confirmedSeats.put(cls, confirmed + 1);
            return "CONFIRMED";
        } else {
            Queue<String> wl = waitingLists.get(cls);
            wl.add(pnr);
            return "WL" + wl.size();
        }
    }

    // Cancel a seat: if confirmed, promote first from WL
    public synchronized String cancelSeat(String cls, String status, String pnr) {
        if (status.equals("CONFIRMED")) {
            int confirmed = confirmedSeats.getOrDefault(cls, 0);
            if (confirmed > 0) {
                confirmedSeats.put(cls, confirmed - 1);
            }
            Queue<String> wl = waitingLists.get(cls);
            if (!wl.isEmpty()) {
                return wl.poll(); // return PNR of first WL person to confirm
            }
        } else {
            // Remove from waiting list
            Queue<String> wl = waitingLists.get(cls);
            List<String> temp = new ArrayList<>(wl);
            temp.remove(pnr);
            wl.clear();
            wl.addAll(temp);
        }
        return null;
    }

    // Get WL position for a PNR
    public int getWLPosition(String cls, String pnr) {
        Queue<String> wl = waitingLists.get(cls);
        if (wl == null) return -1;
        int pos = 1;
        for (String p : wl) {
            if (p.equals(pnr)) return pos;
            pos++;
        }
        return -1;
    }

    public String getTrainId() {
        return trainNumber;
    }
}
