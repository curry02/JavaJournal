package persistence;

import model.Entry;

import static org.junit.jupiter.api.Assertions.*;

//Json Test class
public class JsonTest {
    protected void checkEntry(String date, double amt, String contents, Entry entry) {
        assertEquals(date, entry.getDate());
        assertEquals(amt, entry.getBalance());
        assertEquals(contents, entry.getContents());
    }
}
