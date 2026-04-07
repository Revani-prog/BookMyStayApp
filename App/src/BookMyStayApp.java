import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service with validation safety
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        if (!isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Prevent negative inventory
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Validator (Fail-Fast)
class InvalidBookingValidator {

    public static void validate(Reservation reservation, InventoryService inventoryService)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventoryService.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Room type does not exist: " + reservation.getRoomType());
        }
    }
}

// Booking Service with error handling
class BookingService {
    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processBooking(Reservation reservation) {
        try {
            // Step 1: Validate input (Fail-Fast)
            InvalidBookingValidator.validate(reservation, inventoryService);

            // Step 2: Allocate room safely
            inventoryService.allocateRoom(reservation.getRoomType());

            // Step 3: Confirm booking
            System.out.println("Booking successful for " + reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Deluxe");   // valid
        Reservation r2 = new Reservation("", "Suite");         // invalid name
        Reservation r3 = new Reservation("Bob", "Premium");    // invalid room type
        Reservation r4 = new Reservation("Charlie", "Suite");  // valid
        Reservation r5 = new Reservation("David", "Suite");    // no availability

        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
        bookingService.processBooking(r4);
        bookingService.processBooking(r5);

        // Final inventory state
        inventoryService.displayInventory();
    }
}
