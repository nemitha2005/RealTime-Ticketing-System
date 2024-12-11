import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Configuration config = null;
    private static final int NUM_VENDORS = 3;
    private static final int NUM_CUSTOMERS = 3;

    /**
     * Main method to start the ticket system.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    enterConfiguration(scanner);
                    break;
                case 2:
                    if (config == null) {
                        System.out.println("\nError: Please enter configuration first!\n");
                    } else {
                        startSystem();
                    }
                    break;
                case 3:
                    System.out.println("\nExiting program...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice! Please try again.\n");
            }
        }
    }

    /**
     * Prints the main menu for the ticket system.
     */
    private static void printMenu() {
        System.out.println("=== Real-Time Event Ticket System ===");
        System.out.println("1. Enter Configuration");
        System.out.println("2. Start System");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Gets the user's choice from the menu.
     *
     * @param scanner Scanner object to read user input
     * @return User's choice as an integer
     */
    private static int getChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Allows the user to enter the configuration parameters for the ticket system.
     *
     * @param scanner Scanner object to read user input
     */
    private static void enterConfiguration(Scanner scanner) {
        System.out.println("\n=== Enter Configuration ===");
        try {
            System.out.print("Enter total tickets: ");
            int totalTickets = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter ticket release rate: ");
            int releaseRate = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter ticket retrieval rate: ");
            int retrievalRate = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter maximum ticket capacity: ");
            int maxCapacity = Integer.parseInt(scanner.nextLine());

            if (Configuration.validateConfig(totalTickets, releaseRate, retrievalRate, maxCapacity)) {
                config = new Configuration(totalTickets, releaseRate, retrievalRate, maxCapacity);
                System.out.println("\nConfiguration saved successfully!\n");
            } else {
                System.out.println("\nError: All values must be positive numbers!\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nError: Please enter valid numbers!\n");
        }
    }

    /**
     * Starts the ticket system with the configured parameters.
     */
    private static void startSystem() {
        System.out.println("\nStarting ticket system with " + NUM_VENDORS +
                " vendors and " + NUM_CUSTOMERS + " customers...\n");

        TicketPool ticketPool = new TicketPool(config);
        List<Thread> allThreads = new ArrayList<>();

        // Create and start vendor threads
        for (int i = 1; i <= NUM_VENDORS; i++) {
            Thread vendorThread = new Thread(
                    new Vendor(ticketPool, config.getTicketReleaseRate(), i)
            );
            allThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start customer threads
        for (int i = 1; i <= NUM_CUSTOMERS; i++) {
            Thread customerThread = new Thread(
                    new Customer(ticketPool, config.getTicketRetrievalRate(), i)
            );
            allThreads.add(customerThread);
            customerThread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : allThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("System interrupted!");
            }
        }

        System.out.println("\nSystem stopped. Ready for new configuration.\n");
        config = null;
    }
}