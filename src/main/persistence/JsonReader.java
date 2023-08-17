package persistence;

import model.Entry;
import model.Journal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

//reader that reads workroom from JSON data stored in file
//referenced given sample
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads journal from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Journal read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseJournal(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses journal from JSON object and returns it
    private Journal parseJournal(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Journal j = new Journal(name);
        addEntries(j, jsonObject);
        return j;
    }

    // MODIFIES: j
    // EFFECTS: parses entries from JSON object and adds them to journal
    private void addEntries(Journal j, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("journal");
        for (Object json : jsonArray) {
            JSONObject nextEntry = (JSONObject) json;
            addEntry(j, nextEntry);
        }
    }

    // MODIFIES: j
    // EFFECTS: parses entry from JSON object and adds it to journal
    private void addEntry(Journal j, JSONObject jsonObject) {
        String date = jsonObject.getString("date");
        double amt = jsonObject.getDouble("moneyInAccount");
        String contents = jsonObject.getString("contents");
        Entry entry = new Entry(date, amt);
        entry.setContents(contents);
        j.addEntry(entry);
    }
}

