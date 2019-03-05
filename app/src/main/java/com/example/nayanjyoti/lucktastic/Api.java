package com.example.nayanjyoti.lucktastic;

import com.example.nayanjyoti.lucktastic.service.Client;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Api instance = null;

    // Keep your services here, build them in buildRetrofit method later
    private Client client;

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }

        return instance;
    }

    // Build retrofit once when creating a single instance
    private Api() {
        // Implement a method to build your retrofit
        buildRetrofit();
    }

    private void buildRetrofit() {
        //        okhttp
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Data.BASE_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        // Build your services once
        this.client = retrofit.create(Client.class);
    }

    public Client getClient() {
        return this.client;
    }

}
