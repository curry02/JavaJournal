package ui;

import model.Entry;
import model.Event;
import model.EventLog;
import model.Journal;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Double.parseDouble;

//Main class
public class Main extends JFrame {
    private Journal journal;
    private Entry currentEntry;
    private JLabel label = new JLabel();
    private JTextField field;
    private JTextField field2;
    private ImageIcon image = new ImageIcon("./data/bye.png");
    private JLabel picture = new JLabel(image);

    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private JInternalFrame controlPanelTwo;
    private JButton name;
    private JButton date;
    private JButton contents;
    private JButton delete;

    private static final int WIDTH = 680;
    private static final int HEIGHT = 700;

    //EFFECTS: show opening screen with first panel
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public Main() {
        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(80, 13, 13, 13));
        controlPanel = new JInternalFrame("Control Panel", false, false, false, false);
        controlPanel.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Journal App");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        centreOnScreen();
        addButtonPanel();
        setVisible(true);

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);
        setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Iterator<Event> itr = EventLog.getInstance().iterator();
                while (itr.hasNext()) { 
                    System.out.println(itr.next().getDescription());
                }
                System.exit(-1);
            }
        });
    }

    //EFFECTS: adjust to center
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    //EFFECTS: Button choices for first menu
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(new JButton(new OpenJournal()));
        buttonPanel.add(new JButton(new NewJournal()));
        buttonPanel.add(new JButton(new QuitJournal()));
        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    //EFFECTS: button choices after opening new/old journal
    private void addButtonPanelTwo() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));
        controlPanelTwo = new JInternalFrame("Control Panel", false, false, false, false);
        controlPanelTwo.setLayout(new BorderLayout());
        controlPanel.setVisible(false);

        buttonPanel.add(new JButton(new ReadJournal()));
        buttonPanel.add(new JButton(new AddEntry()));
        buttonPanel.add(new JButton(new DeleteEntry()));
        buttonPanel.add(new JButton(new Bank()));
        buttonPanel.add(new JButton(new Save()));
        buttonPanel.add(new JButton(new Quit()));
        controlPanelTwo.add(buttonPanel, BorderLayout.WEST);
    }

    //EFFECTS: read journal if entries exist, otherwise tell user there are no entries
    private class ReadJournal extends AbstractAction {
        ReadJournal() {
            super("Read Journal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (!journal.isJournalEmpty()) {
                String date = JOptionPane.showInputDialog(new JFrame(),
                        "Enter date: " + journal.getJournalDates());
                if (journal.canFindEntry(date)) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            date + "\n" + journal.getJournalEntryContents(date));
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), date + " entry does not exist.");
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Journal is empty.");
            }
        }
    }

    //EFFECTS: prompt user to enter date for new entry
    private class AddEntry extends AbstractAction {
        AddEntry() {
            super("Add Entry");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            controlPanel.setVisible(false);
            controlPanelTwo.setVisible(false);
            label = new JLabel("Entry date");
            field = new JTextField(40);
            field2 = new JTextField(40);
            date = new JButton(new SetDate());

            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setLayout(new FlowLayout());
            add(label);
            add(field);
            add(field2);
            add(date);
            pack();
            setLocationRelativeTo(null);
        }

    }

    //EFFECTS: make new entry with inputted date and money
    //MODIFIES: journal
    private class SetDate extends AbstractAction {
        SetDate() {
            super("Date & Amount");
        }

        @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (!(journal.canFindEntry(field.getText()))) {
                controlPanel.setVisible(false);
                label.setText("Enter journal");
                field.setVisible(true);
                contents = new JButton(new FinishedEntry());
                date.setVisible(false);
                field2.setVisible(false);
                add(contents);
                setPreferredSize(new Dimension(WIDTH, HEIGHT));
                setLayout(new FlowLayout());

                try {
                    double amount = parseDouble(field2.getText());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,
                            exception.getMessage(), "No String", JOptionPane.ERROR_MESSAGE);

                }
                double amount = parseDouble(field2.getText());
                journal.addEntry(new Entry(field.getText(), amount));
                currentEntry = journal.getEntry(field.getText());

                pack();
                setLocationRelativeTo(null);

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Date already exists");
            }
        }
    }

    //EFFECTS: set entry contents, finish adding entry into journal and show second menu
    //MODIFIES: journal
    private class FinishedEntry extends AbstractAction {
        FinishedEntry() {
            super("Finish");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            currentEntry.setContents(field.getText());
            contents.setVisible(false);
            field.setVisible(false);
            field2.setVisible(false);

            label.setVisible(false);
            name.setVisible(false);
            setResizable(false);
            addButtonPanelTwo();
            centreOnScreen();

            controlPanelTwo.pack();
            controlPanelTwo.setVisible(true);
            desktop.add(controlPanelTwo);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setLayout(new FlowLayout());
        }
    }

    //EFFECTS: create new journal with the given name
    //MODIFIES: journal
    private class JournalName extends AbstractAction {
        JournalName() {
            super("Enter");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            journal = new Journal(field.getText());
            setTitle(field.getText() + "'s Journal");
            field.setVisible(false);
            label.setVisible(false);
            name.setVisible(false);
            setVisible(true);
            setResizable(false);
            addButtonPanelTwo();
            centreOnScreen();

            controlPanelTwo.pack();
            controlPanelTwo.setVisible(true);
            desktop.add(controlPanelTwo);
        }
    }

    //EFFECTS: delete entry from journal
    //MODIFIES: journal
    private class DeleteThis extends AbstractAction {
        DeleteThis() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (journal.canFindEntry(field.getText())) {
                field.setVisible(false);
                delete.setVisible(false);
                label.setVisible(false);
                journal.deleteEntry(journal.getEntry(field.getText()));
                JOptionPane.showMessageDialog(new JFrame(), "Deleted");
                controlPanelTwo.setVisible(true);
                setPreferredSize(new Dimension(WIDTH, HEIGHT));
                setLayout(new FlowLayout());
            } else {
                field.setVisible(false);
                delete.setVisible(false);
                label.setVisible(false);
                JOptionPane.showMessageDialog(new JFrame(), "Entry does not exist");
                controlPanelTwo.setVisible(true);
                setPreferredSize(new Dimension(WIDTH, HEIGHT));
                setLayout(new FlowLayout());
            }
        }
    }

    //EFFECTS: prompt user to enter the date they wish to delete
    private class DeleteEntry extends AbstractAction {
        DeleteEntry() {
            super("Delete Entry");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setLayout(new FlowLayout());
            controlPanelTwo.setVisible(false);
            label.setText("Entry date");
            field = new JTextField(40);
            delete = new JButton(new DeleteThis());

            add(field);
            add(delete);
            pack();
            setLocationRelativeTo(null);
        }
    }

    //EFFECTS: show spending history of user
    private class Bank extends AbstractAction {
        Bank() {
            super("Bank");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            LinkedList<String> spendHistory = new LinkedList<>();
            for (Entry entry : journal.getEntries()) {
                spendHistory.add(String.valueOf(entry.getBalance()));
            }
            JOptionPane.showMessageDialog(new JFrame(), "Spend history: " + spendHistory);
        }
    }

    //EFFECTS: save state of journal
    //MODIFIES: journal
    private class Save extends AbstractAction {
        Save() {
            super("Save Journal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JsonWriter writer = new JsonWriter("./data/" + journal.getName() + ".json");
            try {
                writer.open();
                writer.write(journal);
                writer.close();
                JOptionPane.showMessageDialog(new JFrame(), "Saved successfully");
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null,
                        exception.getMessage(), "No String", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    //EFFECTS: create new journal by prompting user to enter name
    private class NewJournal extends AbstractAction {
        NewJournal() {
            super("New Journal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            label = new JLabel("Name");
            field = new JTextField(40);
            controlPanel.setVisible(false);
            name = new JButton(new JournalName());
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setLayout(new FlowLayout());
            desktop.setVisible(true);
            add(label);
            add(field);
            add(name);
            pack();
            setLocationRelativeTo(null);
        }
    }

    //EFFECTS: open existing journal if inputted name matches with an existing journal,
    //         otherwise tell user that journal does not exist
    private class OpenJournal extends AbstractAction {
        OpenJournal() {
            super("Open Journal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String existingJournal = JOptionPane.showInputDialog("Enter name: ");
            JsonReader reader = new JsonReader("./data/" + existingJournal + ".json");
            try {
                journal = reader.read();
                setTitle(existingJournal + "'s Journal");
                addButtonPanelTwo();
                controlPanel.setVisible(false);
                centreOnScreen();
                setVisible(true);

                controlPanelTwo.pack();
                controlPanelTwo.setVisible(true);
                desktop.add(controlPanelTwo);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JFrame(), "Journal does not exist");
            }
        }
    }

    //EFFECTS: go back to first menu
    private class Quit extends AbstractAction {
        Quit() {
            super("Quit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            setVisible(true);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            controlPanelTwo.setVisible(false);
            controlPanel.setVisible(true);
            pack();
            setLocationRelativeTo(null);
//          desktop.add(new JLabel(new ImageIcon("./data/bye.png")));
//          label = new JLabel();
//          label.setIcon(image);
//          add(label);
//          pack();
//          label.update(label.getGraphics());
//          label.setVisible(true);
//          setLocationRelativeTo(null);
//          label.revalidate();
//          label.repaint();
        }
    }

    //EFFECTS: end application by showing picture
    private class QuitJournal extends AbstractAction {
        QuitJournal() {
            super("Quit");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            setVisible(true);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            picture.setIcon(image);
            desktop.add(picture);
            desktop.setVisible(true);
            setLayout(new FlowLayout());
            controlPanel.setVisible(false);
            picture.setIcon(image);

            pack();
            repaint();
            setLocationRelativeTo(null);

        }
    }

    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Main.this.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
