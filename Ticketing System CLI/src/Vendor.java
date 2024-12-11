public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final int vendorId;

    /**
     * Constructor to initialize the vendor with the ticket pool, release rate, and vendor ID.
     *
     * @param ticketPool  Ticket pool to add tickets to
     * @param releaseRate Release rate of the tickets by per vendor in 1 second
     * @param vendorId    Unique ID of the vendor
     */
    public Vendor(TicketPool ticketPool, int releaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        System.out.println("Vendor " + vendorId + " started");
        while (ticketPool.isSystemRunning()) {
            try {
                ticketPool.addTicket();
                Thread.sleep(1000 / releaseRate);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Vendor " + vendorId + " stopped");
    }
}