import java.util.*;

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

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count <= 0) return false;
        inventory.put(roomType, count - 1);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation get() {
        return queue.poll();
    }
}

class BookingProcessor implements Runnable {
    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                r = queue.get();
            }
            if (r == null) break;

            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " booked " + r.getRoomType() +
                        " for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " failed for " + r.getGuestName() +
                        " (" + r.getRoomType() + ")");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        queue.add(new Reservation("Alice", "Deluxe"));
        queue.add(new Reservation("Bob", "Suite"));
        queue.add(new Reservation("Charlie", "Suite"));
        queue.add(new Reservation("David", "Standard"));
        queue.add(new Reservation("Eve", "Standard"));

        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        inventory.displayInventory();
    }
}
