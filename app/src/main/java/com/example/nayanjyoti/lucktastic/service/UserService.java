package com.example.nayanjyoti.lucktastic.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.nayanjyoti.lucktastic.Api;
import com.example.nayanjyoti.lucktastic.model.ResponseData;
import com.example.nayanjyoti.lucktastic.SharePref;
import com.example.nayanjyoti.lucktastic.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private static final String TAG = "userServiceLog";
    private Context context;
    private User user;

    public UserService(Context context) {
        this.context = context;
    }

    protected void getUserDetails(String email){
        Client userClient = Api.getInstance().getClient();
        Log.d(TAG, userClient.createUser(email).request().url().toString());
        Call<ResponseData> call = userClient.createUser(email);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if(response.isSuccessful()){
                    ResponseData responseData = response.body();
                    user = responseData.getData();
                    Log.d(TAG,user.getEmail()+"-"+user.getCreateDate());
                    SharePref pref = SharePref.getInstance(context);
                    pref.setEmail(user.getEmail());
                    pref.setGoldCoin(user.getTotalCoin());


                }else{
                    Toast.makeText(context,"No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
}
