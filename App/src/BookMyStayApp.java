import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking request
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

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all queued requests
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\n--- Booking Request Queue (FIFO Order) ---");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Process next request (just simulation, no allocation)
    public void processNextRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("No requests to process.");
            return;
        }

        Reservation r = requestQueue.poll();
        System.out.println("Processing request: " + r);
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating incoming booking requests
        queue.addRequest(new Reservation("Alice", "Deluxe", 2));
        queue.addRequest(new Reservation("Bob", "Suite", 3));
        queue.addRequest(new Reservation("Charlie", "Standard", 1));

        // Display queue (FIFO order)
        queue.displayQueue();

        // Simulate processing requests
        System.out.println("\n--- Processing Requests ---");
        queue.processNextRequest();
        queue.processNextRequest();

        // Remaining queue
        queue.displayQueue();
    }
}
