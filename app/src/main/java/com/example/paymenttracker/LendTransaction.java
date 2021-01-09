package com.example.paymenttracker;

import java.math.BigDecimal;
import java.util.Date;

public class LendTransaction extends Transaction {
    private boolean isOutstanding;

    LendTransaction(String name, String description, Date date, BigDecimal amount, int category, int type) {
        super(name, description, date, amount, category, type);
        isOutstanding = true;
    }

    public void setOutstanding(boolean isOutstanding){
        this.isOutstanding = isOutstanding;
    }
    public boolean getOutstanding(){
        return isOutstanding;
    }
}
