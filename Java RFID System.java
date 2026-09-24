import java.io.*;
import java.util.*;

public class RFIDSystem {

    // Store authorized RFID card UIDs
    private static final Set<String> authorizedCards = new HashSet<>();

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("      JAVA RFID SYSTEM");
        System.out.println("================================");

        loadAuthorizedCards();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("1. Scan RFID Card");
            System.out.println("2. Add RFID Card");
            System.out.println("3. Remove RFID Card");
            System.out.println("4. Show Authorized Cards");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    scanCard(scanner);
                    break;

                case "2":
                    addCard(scanner);
                    break;

                case "3":
                    removeCard(scanner);
                    break;

                case "4":
                    showCards();
                    break;

                case "5":
                    saveAuthorizedCards();
                    System.out.println("System stopped.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Simulate reading an RFID card
    private static void scanCard(Scanner scanner) {

        System.out.print("Scan RFID card / Enter UID: ");
        String uid = scanner.nextLine().trim().toUpperCase();

        if (authorizedCards.contains(uid)) {
            System.out.println();
            System.out.println("ACCESS GRANTED");
            System.out.println("Card UID: " + uid);

            // Raspberry Pi GPIO can activate a relay/door lock here.
            openDoor();

        } else {
            System.out.println();
            System.out.println("ACCESS DENIED");
            System.out.println("Unknown Card: " + uid);
        }
    }

    private static void openDoor() {

        System.out.println("Opening door...");

        // Add Raspberry Pi GPIO code here.
        // Example:
        // GPIO HIGH -> relay ON
        // wait 3 seconds
        // GPIO LOW -> relay OFF

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Door locked.");
    }

    private static void addCard(Scanner scanner) {

        System.out.print("Enter new RFID UID: ");

        String uid = scanner.nextLine()
                .trim()
                .toUpperCase();

        if (uid.isEmpty()) {
            System.out.println("Invalid UID.");
            return;
        }

        if (authorizedCards.add(uid)) {
            System.out.println("RFID card added successfully.");
            saveAuthorizedCards();
        } else {
            System.out.println("Card already exists.");
        }
    }

    private static void removeCard(Scanner scanner) {

        System.out.print("Enter RFID UID to remove: ");

        String uid = scanner.nextLine()
                .trim()
                .toUpperCase();

        if (authorizedCards.remove(uid)) {
            System.out.println("RFID card removed.");
            saveAuthorizedCards();
        } else {
            System.out.println("Card not found.");
        }
    }

    private static void showCards() {

        System.out.println();
        System.out.println("Authorized RFID Cards:");

        if (authorizedCards.isEmpty()) {
            System.out.println("No cards registered.");
            return;
        }

        for (String uid : authorizedCards) {
            System.out.println("- " + uid);
        }
    }

    private static void loadAuthorizedCards() {

        File file = new File("allowed_cards.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim().toUpperCase();

                if (!line.isEmpty()) {
                    authorizedCards.add(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading cards: "
                    + e.getMessage());
        }
    }

    private static void saveAuthorizedCards() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("allowed_cards.txt"))) {

            for (String uid : authorizedCards) {
                writer.write(uid);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving cards: "
                    + e.getMessage());
        }
    }
}
