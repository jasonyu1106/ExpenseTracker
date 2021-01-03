package com.example.paymenttracker;

import java.util.Date;

public class SpendTransaction extends Transaction {
    private boolean isOwed;
    private String owedName;

    SpendTransaction(String name, String description, Date date, float amount, int category,
                     int type, boolean isOwed, String owedName) {
        super(name, description, date, amount, category, type);
        this.isOwed = isOwed;
        this.owedName = owedName;
    }

    public void setOwed(boolean isOwed){
        this.isOwed = isOwed;
    }
    public void setOwedName(String name){
        this.owedName = name;
    }

    public boolean getOwed(){
       return isOwed;
    }
    public String getOwedName(){
        return owedName;
    }
}
