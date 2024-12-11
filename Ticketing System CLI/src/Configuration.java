public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int ticketRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Constructor to initialize the configuration parameters.
     *
     * @param totalTickets       Total number of tickets to be added to the pool
     * @param ticketReleaseRate  Release rate of the tickets by per vendor in 1 second
     * @param ticketRetrievalRate Retrieval rate of the tickets by per customer in 1 second
     * @param maxTicketCapacity  Maximum capacity of the ticket pool
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int ticketRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * @return Total number of tickets to be added to the pool
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * @return Release rate of the tickets by per vendor in 1 second
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * @return Retrieval rate of the tickets by per customer in 1 second
     */
    public int getTicketRetrievalRate() {
        return ticketRetrievalRate;
    }

    /**
     * @return Maximum capacity of the ticket pool
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public static boolean validateConfig(int totalTickets, int ticketReleaseRate, int ticketRetrievalRate, int maxTicketCapacity) {
        return totalTickets > 0 && ticketReleaseRate > 0 && ticketRetrievalRate > 0 && maxTicketCapacity > 0;
    }
}

