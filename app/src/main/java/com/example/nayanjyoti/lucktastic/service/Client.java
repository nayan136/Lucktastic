package com.example.nayanjyoti.lucktastic.service;

import com.example.nayanjyoti.lucktastic.model.PaymentDetailsResponse;
import com.example.nayanjyoti.lucktastic.model.PaymentResponse;
import com.example.nayanjyoti.lucktastic.model.ResponseData;
import com.example.nayanjyoti.lucktastic.model.ResponsePlanData;
import com.example.nayanjyoti.lucktastic.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Client {

    @GET("user/show/{user_email}")
    Call<User> reposForUser(@Path("user_email") String email);

//    @POST("user/create/{user_email}")
//    Call<ResponseData> createUser(@Path("user_email") String email);
    @GET()
    Call<ResponseData> createUser(@Url String url);

//    @POST("user/increaseGold/{user_email}/{user_gold}")
//    Call<ResponseData> increaseGold(@Path("user_email") String email, @Path("user_gold") int amount );
    @GET()
    Call<ResponseData> increaseGold(@Url String url);

//    @POST("plan/showAll")
//    Call<ResponsePlanData> plans();
    @GET()
    Call<ResponsePlanData> plans(@Url String url);

//    @POST("user/checkBalance/{user_email}/{plan_id}")
//    Call<ResponseData> checkBalance(@Path("user_email") String email, @Path("plan_id") int planId );
    @GET()
    Call<ResponseData> checkBalance(@Url String url);

//    @POST("payments/insert/{user_email}/{account_id}/{plan_id}")
//    Call<PaymentResponse> sendAccountId(@Path("user_email") String email, @Path("account_id") String accountId, @Path("plan_id") int planId );
    @GET()
    Call<PaymentResponse> sendAccountId(@Url String url);

//    @POST("payments/pending_list/{user_email}")
//    Call<PaymentDetailsResponse> getPendingPayments(@Path("user_email") String email);
    @GET()
    Call<PaymentDetailsResponse> getPendingPayments(@Url String url);

//    @POST("payments/success_list/{user_email}")
//    Call<PaymentDetailsResponse> getSuccessPayments(@Path("user_email") String email);
    @GET()
    Call<PaymentDetailsResponse> getSuccessPayments(@Url String url);

//    @POST("payments/failure_list/{user_email}")
//    Call<PaymentDetailsResponse> getFailurePayments(@Path("user_email") String email);
    @GET()
    Call<PaymentDetailsResponse> getFailurePayments(@Url String url);


}
