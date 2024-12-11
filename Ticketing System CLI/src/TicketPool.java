public class TicketPool {
    private int availableTickets;
    private int totalTicketsSold;
    private final int maxCapacity;
    private final int totalTickets;
    private boolean systemRunning;

    /**
     * Constructor to initialize the ticket pool with the configuration parameters.
     *
     * @param config Configuration parameters for the ticket pool
     */
    public TicketPool(Configuration config) {
        this.availableTickets = 0;
        this.totalTicketsSold = 0;
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
        this.systemRunning = true;
    }

    /**
     * Adds a ticket to the pool.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized void addTicket() throws InterruptedException {
        while (availableTickets >= maxCapacity && systemRunning) {
            System.out.println("Pool is full. Vendor waiting...");
            wait();
        }

        if (!systemRunning) return;

        availableTickets++;
        System.out.println("Vendor added a ticket. Available: " + availableTickets);
        notifyAll();
    }

    /**
     * Buys a ticket from the pool.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized void buyTicket() throws InterruptedException {
        while (availableTickets == 0 && systemRunning) {
            System.out.println("Pool is empty. Customer waiting...");
            wait();
        }

        if (!systemRunning) return;

        availableTickets--;
        totalTicketsSold++;
        System.out.println("Customer bought a ticket. Available: " + availableTickets
                + " (Total sold: " + totalTicketsSold + "/" + totalTickets + ")");

        if (totalTicketsSold >= totalTickets) {
            systemRunning = false;
            notifyAll();
            System.out.println("\nAll tickets have been sold! Total sold: " + totalTicketsSold);
        }
        notifyAll();
    }

    public boolean isSystemRunning() {
        return systemRunning;
    }
}