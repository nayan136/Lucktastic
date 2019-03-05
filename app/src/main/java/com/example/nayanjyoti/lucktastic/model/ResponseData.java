package com.example.nayanjyoti.lucktastic.model;

import com.example.nayanjyoti.lucktastic.model.User;

public class ResponseData {

    private Boolean error;
    private User data;

    public ResponseData(Boolean error, User data) {
        this.error = error;
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
