package com.example.nayanjyoti.lucktastic.model;

import com.google.gson.annotations.SerializedName;

public class PaymentDetails {

    @SerializedName("payment_id")
    private int id;
    private String gateway;
    @SerializedName("account_number")
    private String accountId;
    @SerializedName("request_time")
    private String requestTime;
    @SerializedName("payment_amount")
    private int amount;
    @SerializedName("reference_number")
    private String refNumber;
    @SerializedName("payment_status")
    private String status;
    @SerializedName("user_id")
    private int userId;

    public PaymentDetails(int id, String gateway, String accountId, String requestTime, int amount, String refNumber, String status, int userId) {
        this.id = id;
        this.gateway = gateway;
        this.accountId = accountId;
        this.requestTime = requestTime;
        this.amount = amount;
        this.refNumber = refNumber;
        this.status = status;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
