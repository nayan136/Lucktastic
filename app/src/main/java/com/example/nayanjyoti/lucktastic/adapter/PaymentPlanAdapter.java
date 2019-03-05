package com.example.nayanjyoti.lucktastic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nayanjyoti.lucktastic.Data;
import com.example.nayanjyoti.lucktastic.R;
import com.example.nayanjyoti.lucktastic.model.Plan;

import java.util.List;

public class PaymentPlanAdapter extends RecyclerView.Adapter<PaymentPlanAdapter.MyViewHolder> {

    private static final String TAG = "PaymentPlanAdapter_log";
    private OnRecyclerViewItemClickListener mItemClickListener;

    private List<Plan> plans;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView paymentPlan;
        public CardView cardView;
        public ImageView gatewayLogo;
        public MyViewHolder(View itemView) {
            super(itemView);
            paymentPlan = itemView.findViewById(R.id.text_view_payment);
            cardView = itemView.findViewById(R.id.card_view_plan);
            gatewayLogo = itemView.findViewById(R.id.image_view_gateway_logo);
        }
    }

    public PaymentPlanAdapter(List<Plan> plans) {
        Log.d(TAG,String.valueOf(plans.size()));
        this.plans = plans;
    }

    public void setPlan(List<Plan> plans) {
        Log.d(TAG,String.valueOf(plans.size()));
        this.plans = plans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_plan,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Plan plan = plans.get(position);
        Log.d(TAG,String.valueOf(plan.getGoldAmount()));
        String plandata = "";
        if(plan.getCurrencyType().equals(Data.RUPEES)){
            plandata = plan.getGoldAmount()+" gold = Rs. "+plan.getCurrencyAmount();
        }else{
            plandata = plan.getGoldAmount()+" gold = "+plan.getCurrencyAmount()+" $";
        }

        holder.paymentPlan.setText(plandata);
        holder.cardView.setOnClickListener(v->{
            Log.d(TAG,String.valueOf(position));

            if(mItemClickListener != null){
                mItemClickListener.onItemClickListener(position);
            }
        });

        if(plan.getPaymentGateway().equals(Data.PAYPAL)){
            holder.gatewayLogo.setImageResource(R.drawable.paypal);
        }

    }

    @Override
    public int getItemCount() {
        if(plans != null){
            return plans.size();
        }
        return 0;
    }


}
