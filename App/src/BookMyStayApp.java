import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId; // allocated room

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
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

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 1);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking History (active bookings)
class BookingHistory {
    private Map<String, Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return confirmedBookings.get(id);
    }

    public void removeReservation(String id) {
        confirmedBookings.remove(id);
    }

    public boolean exists(String id) {
        return confirmedBookings.containsKey(id);
    }

    public void displayBookings() {
        System.out.println("\n--- Active Bookings ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        for (Reservation r : confirmedBookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service with rollback
class CancellationService {

    private BookingHistory bookingHistory;
    private InventoryService inventoryService;

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack;

    public CancellationService(BookingHistory bookingHistory, InventoryService inventoryService) {
        this.bookingHistory = bookingHistory;
        this.inventoryService = inventoryService;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!bookingHistory.exists(reservationId)) {
            System.out.println("Cancellation failed: Reservation does not exist.");
            return;
        }

        Reservation reservation = bookingHistory.getReservation(reservationId);

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(reservation.getRoomId());

        // Step 2: Restore inventory
        inventoryService.incrementRoom(reservation.getRoomType());

        // Step 3: Remove booking
        bookingHistory.removeReservation(reservationId);

        System.out.println("Cancellation successful for " + reservation.getGuestName());
        System.out.println("Released Room ID: " + reservation.getRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (LIFO) ---");
        if (rollbackStack.isEmpty()) {
            System.out.println("No rollback operations recorded.");
            return;
        }
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", "DEL1");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", "SUI1");

        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);

        bookingHistory.displayBookings();

        // Cancellation service
        CancellationService cancellationService =
                new CancellationService(bookingHistory, inventoryService);

        // Perform cancellations
        cancellationService.cancelBooking("RES101"); // valid
        cancellationService.cancelBooking("RES999"); // invalid
        cancellationService.cancelBooking("RES101"); // already cancelled

        // Final state
        bookingHistory.displayBookings();
        inventoryService.displayInventory();
        cancellationService.displayRollbackStack();
    }
}
