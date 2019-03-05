package com.example.nayanjyoti.lucktastic.model;

import java.util.List;

public class ResponsePlanData {

    private Boolean error;
    private List<Plan> data;

    public ResponsePlanData(Boolean error, List<Plan> data) {
        this.error = error;
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Plan> getData() {
        return data;
    }

    public void setData(List<Plan> data) {
        this.data = data;
    }
}
