package com.example.nayanjyoti.lucktastic;

public class Data {
    protected static final String BASE_PATH = "http://luckyastic.tk/lucktastic/";

    protected static final String[] sectors = { "32 red", "15 black",
            "19 red", "4 black", "21 red", "2 black", "25 red", "17 black", "34 red",
            "6 black", "27 red","13 black", "36 red", "11 black", "30 red", "8 black",
            "23 red", "10 black", "5 red", "24 black", "16 red", "33 black",
            "1 red", "20 black", "14 red", "31 black", "9 red", "22 black",
            "18 red", "29 black", "7 red", "28 black", "12 red", "35 black",
            "3 red", "26 black", "zero"
    };

    protected static final int[] number = { 32, 15,
            19, 4, 21, 2, 25, 17, 34,
            6, 27,13, 36, 11, 30, 8,
            23, 10, 5, 24, 16, 33,
            1, 20, 14, 31, 9, 22,
            18, 29, 7, 28, 12, 35,
            3, 26, 0
    };

    protected static final int chancePerDay = 10;


    protected  static final String COIN = "gold_coin_";
    protected  static final String DATE = "last_check_in";
    protected  static final String CHANCE_LEFT = "today_chance_left";

//    share prefernce constant
    protected static final String SHARE_PREF_EMAIL = "user_email";
    protected static final String SHARE_PREF_COIN = "gold_coin_";
    protected static final String SHARE_PREF_LAST_LOGIN= "user_last_login";
    protected static final String SHARE_PREF_CHANCE_LEFT = "user_chance_left";

//    gateway type
    public static final String PAYTM = "paytm";
    public static final String PAYPAL = "paypal";

//    currency type
    public static final String RUPEES = "INR";
    public static final String DOLLAR = "USD";
}
