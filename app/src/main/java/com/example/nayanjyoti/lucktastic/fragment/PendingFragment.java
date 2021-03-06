package com.example.nayanjyoti.lucktastic.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nayanjyoti.lucktastic.Api;
import com.example.nayanjyoti.lucktastic.DialogHelper;
import com.example.nayanjyoti.lucktastic.MCrypt;
import com.example.nayanjyoti.lucktastic.R;
import com.example.nayanjyoti.lucktastic.SharePref;
import com.example.nayanjyoti.lucktastic.adapter.PaymentDetailsAdapter;
import com.example.nayanjyoti.lucktastic.model.PaymentDetails;
import com.example.nayanjyoti.lucktastic.model.PaymentDetailsResponse;
import com.example.nayanjyoti.lucktastic.model.PaymentResponse;
import com.example.nayanjyoti.lucktastic.service.Client;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {

    private static final String TAG = "PendingFragment_log";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PaymentDetailsAdapter mAdapter;
    private List<PaymentDetails> paymentDetails = new ArrayList<>();

    private String email;

    public PendingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = SharePref.getInstance(getActivity()).getEmail();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_pending);
        progressBar = v.findViewById(R.id.pb_pending);
        mAdapter = new PaymentDetailsAdapter(paymentDetails);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        DialogHelper.createDialog(getContext());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            init();
        }
    }


    private void init() {
//        DialogHelper.showDialog();
        Client client = Api.getInstance().getClient();
        String url = "payments/pending_list/"+email;
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<PaymentDetailsResponse> call = client.getPendingPayments(encrypted);
        call.enqueue(new Callback<PaymentDetailsResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailsResponse> call, Response<PaymentDetailsResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    PaymentDetailsResponse paymentDetailsResponse = response.body();
                    Boolean isFailure = paymentDetailsResponse.getError();
                    if(!isFailure){
                        List<PaymentDetails> paymentDetails = paymentDetailsResponse.getData();
                        mAdapter.setData(paymentDetails);
                    }
                }
                Log.d(TAG,"cancel dialog");
//                DialogHelper.cancelDialog();
            }

            @Override
            public void onFailure(Call<PaymentDetailsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
//                DialogHelper.cancelDialog();
            }

        });
    }

}
