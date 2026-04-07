import java.util.*;

// Reservation class (confirmed booking)
class Reservation {
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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Booking History (acts like in-memory persistence)
class BookingHistory {

    // List to maintain insertion (chronological) order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display history
    public void displayHistory() {
        System.out.println("\n--- Booking History (Chronological Order) ---");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Reporting Service (separate from storage)
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Generate summary report
    public void generateSummaryReport() {
        List<Reservation> reservations = bookingHistory.getAllReservations();

        System.out.println("\n--- Booking Summary Report ---");

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        System.out.println("Total Bookings: " + reservations.size());

        // Count bookings per room type
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite");
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard");
        Reservation r4 = new Reservation("RES104", "David", "Deluxe");

        // Step 3: Store in history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Step 4: Display full history
        history.displayHistory();

        // Step 5: Generate reports
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateSummaryReport();
    }
}
