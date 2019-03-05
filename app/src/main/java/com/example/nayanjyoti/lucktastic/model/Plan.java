package com.example.nayanjyoti.lucktastic.model;

import com.google.gson.annotations.SerializedName;

public class Plan {

    private int id;

    @SerializedName("gold_amount")
    private int goldAmount;

    @SerializedName("currency_type")
    private String currencyType;

    @SerializedName("currency_amount")
    private int currencyAmount;

    @SerializedName("payment_gateway")
    private String paymentGateway;

    public Plan(int id, int goldAmount, String currencyType, int currencyAmount) {
        this.id = id;
        this.goldAmount = goldAmount;
        this.currencyType = currencyType;
        this.currencyAmount = currencyAmount;
    }

    public int getId() {
        return id;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(int currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
