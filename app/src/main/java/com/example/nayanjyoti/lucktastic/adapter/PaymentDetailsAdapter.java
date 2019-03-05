package com.example.nayanjyoti.lucktastic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nayanjyoti.lucktastic.R;
import com.example.nayanjyoti.lucktastic.model.PaymentDetails;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.MyViewHolder> {

    private List<PaymentDetails> paymentDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView paymentGateway,accountId,requestTime,amount,refNumber;
        public MyViewHolder(View itemView) {
            super(itemView);
            paymentGateway = itemView.findViewById(R.id.tv_gateway);
            accountId = itemView.findViewById(R.id.tv_account_id);
            requestTime = itemView.findViewById(R.id.tv_time);
            amount = itemView.findViewById(R.id.tv_amount);
            refNumber = itemView.findViewById(R.id.tv_number);
        }
    }

    public PaymentDetailsAdapter(List<PaymentDetails> paymentDetailsList) {
        this.paymentDetailsList = paymentDetailsList;
    }

    public void setData(List<PaymentDetails> paymentDetailsList){
        this.paymentDetailsList = paymentDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_details,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PaymentDetails paymentDetails = paymentDetailsList.get(position);
        String str = paymentDetails.getGateway();
        holder.paymentGateway.setText(str);
        str = paymentDetails.getAccountId();
        holder.accountId.setText(str);
        str = paymentDetails.getRequestTime();
        holder.requestTime.setText(str);
        str = String.valueOf(paymentDetails.getAmount());
        holder.amount.setText(str);
        str = paymentDetails.getRefNumber();
        holder.refNumber.setText(str);
    }

    @Override
    public int getItemCount() {
        return paymentDetailsList!=null?paymentDetailsList.size():0;
    }

}
