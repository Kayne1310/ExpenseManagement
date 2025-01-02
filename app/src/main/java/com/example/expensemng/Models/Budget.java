package com.example.expensemng.Models;

import java.io.Serializable;

public class Budget implements Serializable {
    private String id;
    private int amount;
    private String group;
    private String note;
    private String Date;

    public Budget(int amount, String date, String note, String group) {
        this.amount = amount;
        Date = date;
        this.note = note;
        this.group = group;
    }
    public Budget(){

    }


    public Budget(String id, String date, String note, String group, int amount) {
        this.id = id;
        Date = date;
        this.note = note;
        this.group = group;
        this.amount = amount;
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
        return "Budget{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", group='" + group + '\'' +
                ", note='" + note + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
