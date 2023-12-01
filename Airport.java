import java.util.*;
/**
 * Represents a Plane with unique identifier, fuel amount, and time spent in the departure queue.
 *
 * @author Ayden Maglaya
 */
class Plane {
    private int id;
    private int fuel;
    private int queueTime;

    /**
     * Constructs a new Plane with the given identifier and initial fuel amount.
     *
     * @param id    The unique identifier of the plane.
     * @param fuel  The initial fuel amount of the plane.
     */
    public Plane(int id, int fuel) {
        this.id = id;
        this.fuel = fuel;
        queueTime = 1;
    }

    /**
     * Gets the fuel amount of the plane.
     *
     * @return The current fuel amount of the plane.
     */
    public int getFuelAmount() {
        return fuel;
    }

    /**
     * Gets the unique identifier of the plane.
     *
     * @return The identifier of the plane.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the time spent by the plane in the departure queue.
     *
     * @return The time spent in the departure queue.
     */
    public int getQueueTime() {
        return queueTime;
    }

    /**
     * Decrements the fuel amount of the plane.
     */
    public void decrementFuel() {
        fuel--;
    }

    /**
     * Increments the time spent by the plane in the departure queue.
     */
    public void incrementQueueTime() {
        queueTime++;
    }
}

/**
 * Represents an Airport with queues for departing planes, departed planes, and planes with insufficient fuel.
 * Manages the runway, fuel, and simulation parameters.
 */
public class Airport {

    Queue<Plane> departing = new LinkedList<>();
    Queue<Plane> departed = new LinkedList<>();
    Queue<Plane> notEnoughFuel = new LinkedList<>();
    Plane plane;

    int departingQueueTime;
    int fuel;

    int totalPlanes;
    final int fuelNeededForFlight = 100;
    final int simulationTime = 50;

    /**
     * Constructs an Airport with default departing queue time and initial fuel amount.
     */
    public Airport() {
        departingQueueTime = 3;
        fuel = 150;
    }

    /**
     * Starts a new plane with the given identifier, adds it to the departing queue, and increments total planes.
     *
     * @param id The identifier of the new plane.
     */
    void startNewPlane(int id) {
        Plane newPlane = new Plane(id, fuel);
        departing.add(newPlane);
        totalPlanes++;
    }

    /**
     * Checks if the runway is empty.
     *
     * @return True if the runway is empty, false otherwise.
     */
    boolean isRunwayEmpty() {
        return plane == null;
    }

    /**
     * Removes the plane from the runway by setting it to null.
     */
    void removePlaneFromRunway() {
        plane = null;
    }

    /**
     * Decrements the fuel of all planes in the departing queue.
     */
    void decrementFuelOfAllPlanes() {
        for (Plane p : departing) {
            p.decrementFuel();
        }
    }

    /**
     * Processes the departing queue by assigning planes to the runway if it's empty.
     * Checks each plane's fuel, moving it to the departed or notEnoughFuel queue accordingly.
     * Stops when the runway is no longer empty or the departing queue is empty.
     */
    void processDepartingQueue() {    // get a plane wit sum fuel
        if(isRunwayEmpty()) {
            Iterator<Plane> it = departing.iterator();
            while (it.hasNext()) {
                plane = it.next();
                it.remove();
                if (plane.getFuelAmount() >= fuelNeededForFlight)
                    break;
                else
                    notEnoughFuel.add(plane);
            }
        }
    }


    /**
     * Processes the plane currently on the runway, moving it to the departed queue
     * if its queue time meets the departure threshold. Otherwise, increments the
     * queue time of the plane.
     */
    void processDepartingPlane() {
        if (plane != null) {
            if (plane.getQueueTime() >= departingQueueTime) {
                departed.add(plane);
                removePlaneFromRunway();
            }
            else
                plane.incrementQueueTime();
        }
    }

    /**
     * Processes the runway by decrementing fuel for all planes, processing the departing queue, and processing the departing plane.
     */
    void processRunway() {
        decrementFuelOfAllPlanes();
        processDepartingQueue();
        processDepartingPlane();
    }

    /**
     * Prints information about the simulation, including total planes, departed planes, planes with insufficient fuel,
     * and planes still on the runway.
     */
    void printSimulationInfo() {
        System.out.printf("\nSimulation time: %d\n", simulationTime);

        System.out.printf("Total Planes: %d\n", totalPlanes);
        System.out.printf("Planes that departed: %d\n", departed.size());
        System.out.printf("Planes that don't have enough fuel: %d\n", notEnoughFuel.size());
        System.out.printf("Planes still on the runway: %d\n", departing.size());
    }

    /**
     * Prints the queue of departed planes.
     */
    void printDepartedQueue() {
        Iterator<Plane> it = departed.iterator();
        System.out.println("Planes departed queue:");
        while (it.hasNext()) {
            Plane plane = it.next();
            System.out.printf("[%d, %d]->", plane.getId(), plane.getFuelAmount());
        }
        System.out.println();
    }

    /**
     * Prints the queue of planes with insufficient fuel.
     */
    void printNotEnoughFuelQueue() {
        Iterator<Plane> it = notEnoughFuel.iterator();
        System.out.println("Planes not enough fuel queue:");
        while (it.hasNext()) {
            Plane plane = it.next();
            System.out.printf("[%d, %d]->", plane.getId(), plane.getFuelAmount());
        }
        System.out.println();
    }

    /**
     * The main method to run the simulation. Creates an Airport, starts new planes, processes the runway,
     * and prints simulation information.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Airport runway = new Airport();
        int i = 0;
        do {
            if (i < runway.simulationTime)
                runway.startNewPlane(i);
            runway.processRunway();
            i++;
        } while (i < runway.simulationTime || runway.departing.size() > 0);

        runway.printSimulationInfo();
        runway.printDepartedQueue();
        runway.printNotEnoughFuelQueue();
    }
}