package ui;

import model.Entry;
import model.Journal;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//journal app
public class JournalApp {
    private Journal userJournal;
    private Set<Journal> listOfJournals = new HashSet<>();
    private Scanner input;
    private double balance;

    public JournalApp() {
        runJournal();
    }

    //MODIFIES: this
    //EFFECTS:process user input
    private void runJournal() {
        boolean keepGoing = true;
        String command; //= null;

        while (keepGoing) {
            displayMenu();
            input = new Scanner(System.in);
            input.useDelimiter("\n");
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processFirstMenuCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    //EFFECTS: process commands from first menu
    @SuppressWarnings({"checkstyle:SuppressWarnings", "checkstyle:MethodLength"})
    private void processFirstMenuCommand(String command) {
        boolean keepGoing = true;
        if (command.equals("o")) {
            try {
                openJournal();
                while (keepGoing) {
                    displaySecondMenu();
                    command = input.next();
                    command = command.toLowerCase();

                    if (command.equals("q")) {
                        keepGoing = false;
                    } else {
                        processCommand(command);
                    }
                }
            } catch (IOException ioE) {
                System.out.println("Journal not found");
                //displayMenu();
            }
        } else if (command.equals("n")) {
            start();

            while (keepGoing) {
                displaySecondMenu();
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("q")) {
                    keepGoing = false;
                } else {
                    processCommand(command);
                }
            }
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("r")) {
            readJournal();
        } else if (command.equals("a")) {
            addEntry();
        } else if (command.equals("d")) {
            deleteEntry();
        } else if (command.equals("bank")) {
            bankAccount();
        } else if (command.equals("s")) {
            try {
                saveJournal();
            } catch (FileNotFoundException e) {
                System.out.println("Failed to save");
            }
        } else {
            System.out.println("Selection not valid...");
        }
    }

    //EFFECTS: save journal state
    private void saveJournal() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("./data/" + userJournal.getName() + ".json");
        writer.open();
        writer.write(userJournal);
        writer.close();
        System.out.println("Saved successfully");
    }

    //EFFECTS: reload previously saved journal
    private void openJournal() throws IOException {
        System.out.println("Enter name:");
        JsonReader reader = new JsonReader("./data/" + input.next() + ".json");
        userJournal = reader.read();
    }

    //EFFECTS: output entry content if entry exists in journal
    private void readJournal() {
        if (!userJournal.isJournalEmpty()) {
            System.out.println(userJournal.getJournalDates());
            System.out.println("Enter date:");
            String date = input.next();
            if (userJournal.canFindEntry(date)) {
                System.out.println(date + ":");
                System.out.println(userJournal.getJournalEntryContents(date));
            } else {
                System.out.println(date + " entry does not exist.");
            }
        } else {
            System.out.println("Journal is empty.");
        }
    }

    //EFFECTS: add entry in journal
    private void addEntry() {
        System.out.println("Enter date:");
        String date = input.next();
        if (!userJournal.canFindEntry(date)) {
            Entry newEntry = new Entry(date, balance);
            userJournal.addEntry(newEntry);
            System.out.println("Enter journal:");
            newEntry.setContents(input.next());

            System.out.println("Money deposited today:");
            double added = input.nextDouble();
            newEntry.deposit(added);

            System.out.println("Money spent today:");
            double spent = input.nextDouble();
            newEntry.spent(spent);
            balance = newEntry.getBalance();
            System.out.println("Entry for " + date + " complete!");
        } else {
            System.out.println(date + " entry already exists.");
        }

    }

    //REQUIRES: entry exists in journal
    //EFFECTS: remove entry in journal
    private void deleteEntry() {
        System.out.println("Enter date:");
        String date = input.next();
        if (userJournal.canFindEntry(date)) {
            userJournal.deleteEntry(userJournal.getEntry(date));
            System.out.println("Deletion for " + date + "complete!");
        } else {
            System.out.println(date + " entry does not exist.");
        }
    }

    //EFFECTS: print how much money is in bank account
    private void bankAccount() {
        System.out.println("Bank account has: $" + balance);
    }

    //EFFECTS: create journal with user's inputted name
    private void start() {
        System.out.println("Your Name:");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        String name = input.next();
        System.out.println(name + "'s Journal");
        System.out.println("Enter balance:");
        balance = Double.parseDouble(input.next());
        userJournal = new Journal(name);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        //System.out.println(userJournal.getName() + "'s Journal.");
        System.out.println("\nSelect from:");

        System.out.println("\to-> open saved journal");
        System.out.println("\tn -> new journal");
        System.out.println("\tq -> quit");
    }

    //EFFECTS: display second menu
    private void displaySecondMenu() {
        System.out.println("\tr -> read journal");
        System.out.println("\ta -> add entry");
        System.out.println("\td -> delete entry");
        System.out.println("\tbank -> money in bank");
        System.out.println("\ts -> save journal");
        System.out.println("\tq -> quit");
    }
}
