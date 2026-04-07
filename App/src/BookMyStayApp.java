import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return reservationId + " - " + guestName + " - " + roomType;
    }
}

class InventoryService implements Serializable {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }

    public void setAll(List<Reservation> history) {
        this.history = history;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_state.dat";

    public static void save(InventoryService inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("State saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object[] load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            InventoryService inventory = (InventoryService) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("State loaded successfully.");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("No valid saved data found. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args)  {

        InventoryService inventory;
        BookingHistory history;

        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (InventoryService) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new InventoryService();
            history = new BookingHistory();
        }

        history.add(new Reservation("RES201", "Alice", "Deluxe"));
        history.add(new Reservation("RES202", "Bob", "Suite"));

        inventory.getInventory().put("Deluxe", inventory.getInventory().get("Deluxe") - 1);
        inventory.getInventory().put("Suite", inventory.getInventory().get("Suite") - 1);

        history.display();
        inventory.display();

        PersistenceService.save(inventory, history);
    }
}
