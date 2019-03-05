package com.example.nayanjyoti.lucktastic.model;

import java.util.List;

public class PaymentDetailsResponse {

    private Boolean error;
    private List<PaymentDetails> data;

    public PaymentDetailsResponse(Boolean error, List<PaymentDetails> data) {
        this.error = error;
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<PaymentDetails> getData() {
        return data;
    }

    public void setData(List<PaymentDetails> data) {
        this.data = data;
    }
}
