public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final int customerId;

    /**
     * Constructor to initialize the customer with the ticket pool, retrieval rate, and customer ID.
     *
     * @param ticketPool     Ticket pool to buy tickets from
     * @param retrievalRate  Retrieval rate of the tickets by per customer in 1 second
     * @param customerId     Unique ID of the customer
     */
    public Customer(TicketPool ticketPool, int retrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        System.out.println("Customer " + customerId + " started");
        while (ticketPool.isSystemRunning()) {
            try {
                ticketPool.buyTicket();
                Thread.sleep(1000 / retrievalRate);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Customer " + customerId + " stopped");
    }
}