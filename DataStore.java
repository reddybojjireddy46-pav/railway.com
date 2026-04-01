package com.railway.util;

import com.railway.model.Train;
import com.railway.model.Ticket;

import java.util.*;

public class DataStore {

    private static DataStore instance;

    // PNR -> Ticket
    private Map<String, Ticket> tickets = new HashMap<>();

    // trainNumber -> Train
    private Map<String, Train> trains = new HashMap<>();

    // All trains list
    private List<Train> trainList = new ArrayList<>();

    private DataStore() {
        initTrains();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initTrains() {
        // Bengaluru -> Chennai
        addTrain(new Train("Vande Bharat Express", "20608", "Bengaluru", "Chennai",
                "06:00", "10:30", "4h 30m", 2800, 1680, 1200, 495));
        addTrain(new Train("Vande Bharat Express", "20663", "Bengaluru", "Chennai",
                "14:30", "19:15", "4h 45m", 2800, 1680, 1200, 495));
        addTrain(new Train("Shatabdi Express", "12028", "Bengaluru", "Chennai",
                "07:00", "11:00", "4h 00m", 2200, 1350, 960, 380));
        addTrain(new Train("Shatabdi Express", "12008", "Bengaluru", "Chennai",
                "16:00", "20:00", "4h 00m", 2200, 1350, 960, 380));

        // Bengaluru -> Hyderabad
        addTrain(new Train("KCG Vande Bharat", "20704", "Bengaluru", "Hyderabad",
                "05:45", "14:15", "8h 30m", 3200, 1920, 1380, 560));
        addTrain(new Train("Rajdhani Express", "22691", "Bengaluru", "Hyderabad",
                "21:00", "07:30", "10h 30m", 3800, 2300, 1650, 680));
        addTrain(new Train("Duronto Express", "12213", "Bengaluru", "Hyderabad",
                "23:30", "09:00", "9h 30m", 3500, 2100, 1500, 610));

        // Chennai -> Bengaluru
        addTrain(new Train("Vande Bharat Express", "20607", "Chennai", "Bengaluru",
                "07:00", "11:30", "4h 30m", 2800, 1680, 1200, 495));
        addTrain(new Train("Shatabdi Express", "12027", "Chennai", "Bengaluru",
                "06:00", "10:00", "4h 00m", 2200, 1350, 960, 380));

        // Chennai -> Hyderabad
        addTrain(new Train("Chennai Express", "12603", "Chennai", "Hyderabad",
                "08:00", "20:30", "12h 30m", 3500, 2100, 1500, 610));
        addTrain(new Train("Charminar Express", "12759", "Chennai", "Hyderabad",
                "18:30", "07:00", "12h 30m", 3200, 1920, 1380, 560));

        // Hyderabad -> Bengaluru
        addTrain(new Train("KCG Vande Bharat", "20703", "Hyderabad", "Bengaluru",
                "15:30", "00:00", "8h 30m", 3200, 1920, 1380, 560));
        addTrain(new Train("Rajdhani Express", "22692", "Hyderabad", "Bengaluru",
                "08:45", "19:15", "10h 30m", 3800, 2300, 1650, 680));

        // Hyderabad -> Chennai
        addTrain(new Train("Charminar Express", "12760", "Hyderabad", "Chennai",
                "08:00", "20:30", "12h 30m", 3200, 1920, 1380, 560));
    }

    private void addTrain(Train t) {
        trains.put(t.getTrainNumber(), t);
        trainList.add(t);
    }

    public List<Train> searchTrains(String from, String to) {
        List<Train> result = new ArrayList<>();
        for (Train t : trainList) {
            if (t.getFrom().equalsIgnoreCase(from) && t.getTo().equalsIgnoreCase(to)) {
                result.add(t);
            }
        }
        return result;
    }

    public Train getTrain(String trainNumber) {
        return trains.get(trainNumber);
    }

    public void saveTicket(Ticket ticket) {
        tickets.put(ticket.getPnr(), ticket);
    }

    public Ticket getTicket(String pnr) {
        return tickets.get(pnr);
    }

    public Map<String, Ticket> getAllTickets() {
        return tickets;
    }

    public String generatePNR() {
        Random rand = new Random();
        String pnr;
        do {
            pnr = "PNR" + (1000000000L + (long)(rand.nextDouble() * 9000000000L));
            // Keep it 13 chars: PNR + 10 digits
        } while (tickets.containsKey(pnr));
        return pnr;
    }
}
