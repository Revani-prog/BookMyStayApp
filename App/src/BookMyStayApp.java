import java.util.*;

// Reservation class (same as previous use case)
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Service
class BookingService {
    private Queue<Reservation> requestQueue;
    private InventoryService inventoryService;

    // Track all allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds;

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> roomAllocations;

    private int roomCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocations = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 3).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process booking requests
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            System.out.println("\nProcessing: " + reservation);

            // Check availability
            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("No rooms available for type: " + roomType);
                continue;
            }

            // Allocate room (atomic logic block)
            String roomId = generateRoomId(roomType);

            // Ensure uniqueness
            allocatedRoomIds.add(roomId);

            roomAllocations
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            // Update inventory immediately
            inventoryService.decrementRoom(roomType);

            // Confirm booking
            System.out.println("Booking Confirmed!");
            System.out.println("Assigned Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Room Allocations ---");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<Reservation> requestQueue = new LinkedList<>();
        requestQueue.offer(new Reservation("Alice", "Deluxe", 2));
        requestQueue.offer(new Reservation("Bob", "Suite", 3));
        requestQueue.offer(new Reservation("Charlie", "Standard", 1));
        requestQueue.offer(new Reservation("David", "Suite", 2)); // Will fail (only 1 Suite)

        // Step 2: Initialize services
        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(requestQueue, inventoryService);

        // Step 3: Process bookings
        bookingService.processBookings();

        // Step 4: Show final state
        bookingService.displayAllocations();
        inventoryService.displayInventory();
    }
}
