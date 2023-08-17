package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// journal class with name, collection of dates, collection of entries
public class Journal implements Writable {
    private String name;
    private Set<Entry> journal;
    private Set<String> dateCollection;

    // EFFECTS: constructs journal with empty list of entries and empty date collection
    public Journal(String name) {
        this.name = name;
        journal = new HashSet<>();
        dateCollection = new HashSet<>();
    }

    // EFFECTS: adds entry to list
    // MODIFIES: this
    public void addEntry(Entry e) {
        journal.add(e);
        dateCollection.add(e.getDate());
        EventLog.getInstance().logEvent(new Event("An entry was added to Journal."));
    }

    // MODIFIES: this
    // EFFECTS: removes entry from journal, thus removing date from date collection
    public void deleteEntry(Entry e) {
        journal.remove(e);
        dateCollection.remove(e.getDate());
        EventLog.getInstance().logEvent(new Event("An entry was deleted."));
    }

    public boolean isJournalEmpty() {
        return journal.isEmpty();
    }

    //EFFECTS: true if entry exists in journal
    public boolean canFindEntry(String str) {
        for (Entry e : journal) {
            if (Objects.equals(e.getDate(), str)) {
                return true;
            }
        }
        return false;
    }

    //EFFECTS: returns entry in journal if it exists
    public Entry getEntry(String str) {
        for (Entry e : journal) {
            if (Objects.equals(e.getDate(), str)) {
                return e;
            }
        }
        return null;
    }

    //REQUIRES: journal date entered exists in journal
    public String getJournalEntryContents(String str) {
        for (Entry e : journal) {
            if (Objects.equals(e.getDate(), str)) {
                String d = Double.toString(e.getBalance());
                return (e.getContents() + System.lineSeparator() + System.lineSeparator() + d);
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Set<String> getJournalDates() {
        return dateCollection;
    }

    // EFFECTS: returns an unmodifiable list of thingies in this workroom
    public Set<Entry> getEntries() {
        return journal;
    }

    //EFFECTS: convert to Json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("journal", entriesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray entriesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Entry e : journal) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

}
