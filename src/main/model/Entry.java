package model;

import org.json.JSONObject;
import persistence.Writable;

// Journal entries with date, moneyInAccount, and contents
public class Entry implements Writable {
    private String date;
    private double moneyInAccount;
    private String contents;

    // EFFECTS: creates new entry in journal with given date and account balance, with nothing
    //          written in journal entry
    // REQUIRES: balance >= 0
    public Entry(String date, double balance) {
        this.date = date;
        this.moneyInAccount = balance;
        contents = "";
    }

    // MODIFIES: this
    // EFFECTS: adds money onto bank account
    // REQUIRES: addedMoney > 0
    public void deposit(double addedMoney) {
        moneyInAccount = moneyInAccount + addedMoney;
    }

    // MODIFIES: this
    // EFFECTS: calculates how much money left in account after spending
    // REQUIRES: moneySpent >= 0, moneySpent <= moneyInAccount
    public void spent(double moneySpent) {
        moneyInAccount = moneyInAccount - moneySpent;
    }

    public void setContents(String str) {
        contents = str;
    }

    //EFFECTS: returns money in account in dollars
    public double getBalance() {
        return moneyInAccount;
    }

    //EFFECTS: returns entry date
    public String getDate() {
        return date;
    }

    //EFFECTS: returns entry contents
    public String getContents() {
        return contents;
    }

    //EFFECTS: convert to Json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", date);
        json.put("moneyInAccount", moneyInAccount);
        json.put("contents", contents);
        return json;
    }
}
