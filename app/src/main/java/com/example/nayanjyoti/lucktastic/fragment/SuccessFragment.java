package com.example.nayanjyoti.lucktastic.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.nayanjyoti.lucktastic.service.Client;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PaymentDetailsAdapter mAdapter;
    private List<PaymentDetails> paymentDetails = new ArrayList<>();

    private String email;


    public SuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_success, container, false);
        email = SharePref.getInstance(getActivity()).getEmail();
        progressBar = v.findViewById(R.id.pb_success);
        recyclerView = v.findViewById(R.id.recycler_view_success);
        mAdapter = new PaymentDetailsAdapter(paymentDetails);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        DialogHelper.createDialog(getContext());
        return v;
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
        String url = "payments/success_list/"+email;
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<PaymentDetailsResponse> call = client.getSuccessPayments(encrypted);
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
