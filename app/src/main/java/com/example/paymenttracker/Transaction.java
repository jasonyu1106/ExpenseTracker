package com.example.paymenttracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Transaction implements Comparable<Transaction>{
    private String name, description;
    private Date date;
    private BigDecimal amount;
    private int category;
    private int type;

    Transaction(String name, String description, Date date, BigDecimal amount, int category, int type){
        this.name = name;
        this.description = description;
        this.date = date;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.category = category;
        this.type = type;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setDate (Date date){
        this.date = date;
    }
    public void setDescription (String info){
        description = info;
    }
    public void setAmount (BigDecimal price){
        amount = price;
    }
    public void setCategory (int position){
        category = position;
    }
    public void setType (int typeCode){
        type=typeCode;
    }

    public String getRecipient (){
        return name;
    }
    public Date getDate (){
        return date;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getAmount (){
        return amount;
    }
    public int getCategory() {
        return category;
    }
    public int getType(){return type;}

    @Override
    public int compareTo(Transaction o) {
        if (this.date.after(o.date))
            return -1;
        else if (this.date.before(o.date))
            return 1;
        else
            return 0;
    }
}