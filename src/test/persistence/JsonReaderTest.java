package persistence;

import model.Entry;
import model.Journal;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//tests for JsonReader
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Journal journal = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyJournal() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyJournal.json");
        try {
            Journal journal = reader.read();
            Set<String> emptyList = new HashSet<>();
            assertEquals("My Journal", journal.getName());
            assertEquals(emptyList, journal.getJournalDates());
        } catch (IOException e) {
            fail("should have no exception");
        }
    }

    @Test
    void testReaderGeneralJournal() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralJournal.json");
        try {
            Journal j1 = reader.read();
            assertEquals("My Journal", j1.getName());
            Set<Entry> journal = j1.getEntries();
            assertEquals(2, journal.size());
            checkEntry("dec 24", 50, "worked 5 hours", j1.getEntry("dec 24"));
            checkEntry("feb 14", 50, "bus moment", j1.getEntry("feb 14"));
        } catch (IOException e) {
            fail("should have no exception");
        }
    }
}