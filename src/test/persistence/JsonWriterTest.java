package persistence;

import model.Entry;
import model.Journal;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//tests for JsonWriter
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Journal journal1 = new Journal("My Journal");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyJournal() {
        try {
            Journal journal1 = new Journal("My Journal");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyJournal.json");
            writer.open();
            writer.write(journal1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyJournal.json");
            journal1 = reader.read();
            Set<Entry> journal = journal1.getEntries();
            assertEquals("My Journal", journal1.getName());
            assertEquals(0, journal.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Journal journal1 = new Journal("My Journal");
            journal1.addEntry(new Entry("dec 24", 50));
            journal1.getEntry("dec 24").setContents("worked 5 hours");
            journal1.addEntry(new Entry("jan 1", 10));
            journal1.getEntry("jan 1").setContents("new year");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralJournal.json");
            writer.open();
            writer.write(journal1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralJournal.json");
            journal1 = reader.read();
            assertEquals("My Journal", journal1.getName());
            Set<Entry> journal = journal1.getEntries();
            assertEquals(2, journal.size());
            checkEntry("dec 24", 50, "worked 5 hours", journal1.getEntry("dec 24"));
            checkEntry("jan 1", 10, "new year", journal1.getEntry("jan 1"));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}