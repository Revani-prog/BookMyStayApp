import java.util.*;

abstract class Room {
    String type;
    double price;

    Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + type + " | Price: $" + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 80);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 150);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 300);
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 0);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

class RoomSearchService {

    void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {

        System.out.println("===== Available Rooms =====");

        for (String type : inventory.getRoomTypes()) {

            int available = inventory.getAvailability(type);

            if (available > 0) {
                Room room = roomCatalog.get(type);
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(inventory, roomCatalog);
    }
}
