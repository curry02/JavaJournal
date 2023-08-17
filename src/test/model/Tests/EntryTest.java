package model.Tests;

import model.Entry;
import model.Journal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//tests for Entry class
class EntryTest {
    private Entry entry1;
    private Entry entry2;
    private Entry entry3;
    private Journal emptyJournal;
    private Journal withEntries;

    @BeforeEach
    void setUp() {
        entry1 = new Entry("Dec 25 2021", 7000.00);
        entry2 = new Entry ("Jan 01 2022", 0);
        entry3 = new Entry ("Dec 26 2021", 5000);
        emptyJournal = new Journal("Bob");
        withEntries = new Journal("Bobby");
        withEntries.addEntry(entry1);
    }

    @Test
    void testConstructorEntry() {
        assertEquals("Dec 25 2021", entry1.getDate());
        assertEquals(7000, entry1.getBalance());
        assertEquals("", entry1.getContents());

        assertEquals("Jan 01 2022", entry2.getDate());
        assertEquals(0, entry2.getBalance());
        assertEquals("", entry2.getContents());
    }

    @Test
    void testConstructorEmptyEntryList() {
        assertTrue(emptyJournal.isJournalEmpty());
        assertEquals("Bob", emptyJournal.getName());
    }

    @Test
    void testConstructorNonEmptyEntryList() {
        assertFalse(withEntries.isJournalEmpty());
    }

    @Test
    void testSetContents() {
        entry1.setContents("worked 5 hours");
        assertEquals("worked 5 hours", entry1.getContents());
    }
    @Test
    void testFindEntry() {
        assertTrue(withEntries.canFindEntry("Dec 25 2021"));
        withEntries.addEntry(entry2);
        assertTrue(withEntries.canFindEntry("Jan 01 2022"));
    }

    @Test
    void testCannotFindEntry() {
        assertFalse(emptyJournal.canFindEntry("Dec 25 2021"));
        assertFalse(withEntries.canFindEntry("May 04 2022"));
    }

    @Test
    void testAddEntryOnce() {
        withEntries.addEntry(entry3);
        assertTrue(withEntries.canFindEntry("Dec 26 2021"));
    }

    @Test
    void testAddEntryMoreThanOnce() {
        withEntries.addEntry(entry2);
        assertTrue(withEntries.canFindEntry("Jan 01 2022"));
        withEntries.addEntry(entry3);
        assertTrue(withEntries.canFindEntry("Dec 26 2021"));
    }

    @Test
    void testDeleteEntryOnce() {
        withEntries.deleteEntry(entry1);
        assertFalse(withEntries.canFindEntry("Dec 25 2021"));
    }

    @Test
    void testDeleteEntryMoreThanOnce() {
        withEntries.addEntry(entry2);
        withEntries.addEntry(entry3);
        withEntries.deleteEntry(entry1);
        withEntries.deleteEntry(entry3);
        assertTrue(withEntries.canFindEntry("Jan 01 2022"));
        assertFalse(withEntries.canFindEntry("Dec 25 2021"));
        assertFalse(withEntries.canFindEntry("Dec 26 2021"));
    }

    @Test
    void testGetEntry() {
        assertEquals(entry1, withEntries.getEntry("Dec 25 2021"));
        assertNull(withEntries.getEntry("Jan 20 2022"));
    }

    @Test
    void testGetJournalEntryContentsEntryExists() {
        entry1.setContents("worked 5 hours");
        assertEquals("worked 5 hours" + System.lineSeparator() + System.lineSeparator()
                + "7000.0", withEntries.getJournalEntryContents("Dec 25 2021"));

    }

    @Test
    void testGetJournalEntryContentsEntryNonExistent() {
        assertNull(withEntries.getJournalEntryContents("Jan 05 2034"));
    }

    @Test
    void testGetJournalDates() {
        Set<String> testList = new HashSet<>();
        testList.add("Dec 25 2021");
        assertEquals(testList, withEntries.getJournalDates());
    }

    @Test
    void testDepositOnce() {
        entry1.deposit(100.00);
        assertEquals(7100.00, entry1.getBalance());
    }

    @Test
    void testDepositMultiple() {
        entry2.deposit(1000.20);
        assertEquals(1000.20, entry2.getBalance());
        entry2.deposit(1000);
        assertEquals(2000.20, entry2.getBalance());
    }

    @Test
    void testSpentOnce() {
        entry1.spent(1000.50);
        assertEquals(5999.50, entry1.getBalance());
    }

    @Test
    void testSpentMultiple() {
        entry1.spent(1000.50);
        assertEquals(5999.50, entry1.getBalance());
        entry1.spent(200);
        assertEquals(5799.50, entry1.getBalance());
    }



}