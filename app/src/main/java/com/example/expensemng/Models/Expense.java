package com.example.expensemng.Models;

import java.io.Serializable;

public class Expense implements Serializable {
    private String id;
    private int amount;
    private String group;
    private String note;
    private String Date;

    public Expense(int amount, String date, String note, String group) {
        this.amount = amount;
        Date = date;
        this.note = note;
        this.group = group;
    }

    public Expense() {
    }

    public Expense(String id, int amount, String group, String note, String date) {
        this.id = id;
        this.amount = amount;
        this.group = group;
        this.note = note;
        Date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "amount=" + amount +
                ", group='" + group + '\'' +
                ", note='" + note + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
