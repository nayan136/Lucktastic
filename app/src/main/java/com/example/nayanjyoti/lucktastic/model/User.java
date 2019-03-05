package com.example.nayanjyoti.lucktastic.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private int id;
    private String email;
    @SerializedName("total_coin")
    private int totalCoin;
    @SerializedName("created_date")
    private String createDate;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("chance_left")
    private int chanceleft;

    public User(int id, String email, int totalCoin, String createDate, String lastLogin, int chanceleft) {
        this.id = id;
        this.email = email;
        this.totalCoin = totalCoin;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
        this.chanceleft = chanceleft;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalCoin() {
        return totalCoin;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setTotalCoin(int totalCoin) {
        this.totalCoin = totalCoin;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getChanceleft() {
        return chanceleft;
    }

    public void setChanceleft(int chanceleft) {
        this.chanceleft = chanceleft;
    }
}
