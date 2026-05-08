package com.sathish.thodar.data.dto.response.passenger;

public class Transaction {
    public String tId;
    public Long userId;
    public String pnr;
    public double amount;
    public String type; // "DEBIT" (Booking) or "CREDIT" (Refund)
    public String remark;

    public Transaction(String pnr, Long userId, double amount, String type, String remark) {
        this.tId = "TXN" + (System.currentTimeMillis() % 1000000);
        this.pnr = pnr;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.remark = remark;
    }
}