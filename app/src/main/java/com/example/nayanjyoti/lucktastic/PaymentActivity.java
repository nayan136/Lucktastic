package com.example.nayanjyoti.lucktastic;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.nayanjyoti.lucktastic.adapter.OnRecyclerViewItemClickListener;
import com.example.nayanjyoti.lucktastic.adapter.PaymentPlanAdapter;
import com.example.nayanjyoti.lucktastic.model.Plan;
import com.example.nayanjyoti.lucktastic.model.PaymentResponse;
import com.example.nayanjyoti.lucktastic.model.ResponseData;
import com.example.nayanjyoti.lucktastic.model.ResponsePlanData;
import com.example.nayanjyoti.lucktastic.service.Client;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    private static final String TAG = "PaymentActivity_log";

    private RecyclerView recyclerView;
    private PaymentPlanAdapter mAdapter;
    private List<Plan> plans = new ArrayList<>();
    private String email;
    private int selectPlanId;

    private View dialogView;
    private EditText getMobileNumber;
    private EditText getEmail;
    private AlertDialog dialog;
    private Button buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        DialogHelper.setNoInternetDialogCreate(this);

        recyclerView = findViewById(R.id.recycler_view_plan);
        mAdapter = new PaymentPlanAdapter(plans);
        mAdapter.setOnItemClickListener(this);
        buttonRefresh = findViewById(R.id.btn_refresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlanData();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        email = SharePref.getInstance(this).getEmail();
        // load dialog
        DialogHelper.createDialog(this);
        getPlanData();
    }

    private void getPlanData() {
        DialogHelper.showDialog();
        Client client = Api.getInstance().getClient();
        String url = "plan/showAll";
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<ResponsePlanData> call = client.plans(encrypted);
        call.enqueue(new Callback<ResponsePlanData>() {
            @Override
            public void onResponse(Call<ResponsePlanData> call, Response<ResponsePlanData> response) {
                buttonRefresh.setVisibility(View.GONE);
                ResponsePlanData planData = response.body();
                if(!planData.getError()){
                    plans = planData.getData();
                    mAdapter.setPlan(plans);
                    Log.d(TAG,plans.get(0).getPaymentGateway());
                }
                DialogHelper.cancelDialog();
            }

            @Override
            public void onFailure(Call<ResponsePlanData> call, Throwable t) {
                Log.d(TAG,"Network error");
                buttonRefresh.setVisibility(View.VISIBLE);
                DialogHelper.cancelDialog();
            }
        });
    }

    @Override
    public void onItemClickListener(int position) {
        //Toast.makeText(PaymentActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();

        Plan plan = plans.get(position);
        selectPlanId = plan.getId();
        Client client = Api.getInstance().getClient();
        String url = "user/checkBalance/"+email+"/"+plan.getId();
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<ResponseData> call = client.checkBalance(encrypted);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ResponseData data = response.body();
                Boolean isFailer = data.getError();
                Log.d(TAG,String.valueOf(isFailer));
                if(!isFailer){
                    //Toast.makeText(PaymentActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                    showDialogForAccountId();
                }else{
                    showFailureDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                buttonRefresh.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showDialogForAccountId() {
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            dialogView = inflater.inflate(R.layout.item_input_account_id_dialog,null);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v->dialog.dismiss());
        Button submit = dialogView.findViewById(R.id.btn_submit);
        getMobileNumber = dialogView.findViewById(R.id.edit_text_mobile_number);
        getEmail = dialogView.findViewById(R.id.edit_text_email);

        submit.setOnClickListener(v->sendPaymentsDetails());
        dialog.show();

    }

    private void sendPaymentsDetails() {
        String mobileNo = getMobileNumber.getText().toString().trim();
        String emailId = getEmail.getText().toString().trim();
        String accountId="";
        if(mobileNo.isEmpty() && emailId.isEmpty()){
            Log.d(TAG,"empty");
        }else{
            accountId = mobileNo.isEmpty()?emailId:mobileNo;
            Log.d(TAG,accountId);

        }
        dialog.dismiss();
        if(!accountId.isEmpty()){
            Client client = Api.getInstance().getClient();
            String url = "payments/insert/"+email+"/"+accountId+"/"+selectPlanId;
            MCrypt mcrypt = new MCrypt();
            String encrypted="";
            try {
                encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<PaymentResponse> call = client.sendAccountId(encrypted);
            call.enqueue(new Callback<PaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<PaymentResponse> call, Throwable t) {
                    Log.d(TAG, "Connectivity Error");
                }
            });
        }

    }

    public static void dialog(boolean value){
        if(value){
            DialogHelper.showNoInternetDialog();
        }else{
            DialogHelper.cancelNoInternetDialog();
        }
    }



    private void showFailureDialog(){
        new TTFancyGifDialog.Builder(PaymentActivity.this)
            .setTitle("Online Shopping")
            .setMessage("You don't have time for shopping, Visit our website for online shopping with discount price.")
            .setPositiveBtnText("Ok")
            .setPositiveBtnBackground("#22b573")
            //.setGifResource(R.drawable.gif1)      //pass your gif, png or jpg
            .isCancellable(true)
            .OnPositiveClicked(new TTFancyGifDialogListener() {
                @Override
                public void OnClick() {
                    //Toast.makeText(PaymentActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                }
            })
            .build();
    }
}
