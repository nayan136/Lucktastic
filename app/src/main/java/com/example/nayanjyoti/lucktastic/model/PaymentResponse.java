package com.example.nayanjyoti.lucktastic.model;

public class PaymentResponse {

    private Boolean error;
    private PaymentDetails data;

    public PaymentResponse(Boolean error, PaymentDetails data) {
        this.error = error;
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public PaymentDetails getData() {
        return data;
    }

    public void setData(PaymentDetails data) {
        this.data = data;
    }
}
