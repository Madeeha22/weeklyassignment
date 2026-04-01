import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean isOccupied;

    public ParkingSpot() {
        this.licensePlate = null;
        this.entryTime = 0;
        this.isOccupied = false;
    }
}

class ParkingLot {

    private ParkingSpot[] table;
    private int capacity = 500;
    private int size = 0;
    private int totalProbes = 0;

    public ParkingLot() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].isOccupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].isOccupied = true;

        size++;
        totalProbes += probes;

        System.out.println("Vehicle " + plate +
                " parked at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].isOccupied) {

            if (plate.equals(table[index].licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;
                double hours = duration / (1000.0 * 60 * 60);
                double fee = hours * 5; // $5/hour

                table[index].isOccupied = false;
                table[index].licensePlate = null;

                size--;

                System.out.println("Vehicle " + plate + " exited.");
                System.out.println("Duration: " + String.format("%.2f", hours) + " hours");
                System.out.println("Fee: $" + String.format("%.2f", fee));
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found!");
    }

    // Statistics
    public void getStatistics() {

        double occupancy = (size * 100.0) / capacity;
        double avgProbes = size == 0 ? 0 : (totalProbes * 1.0 / size);

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Average Probes: " + avgProbes);
    }

    // MAIN METHOD
    public static void main(String[] args) throws InterruptedException {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate time

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}