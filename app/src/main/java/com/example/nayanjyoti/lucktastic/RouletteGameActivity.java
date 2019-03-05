package com.example.nayanjyoti.lucktastic;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.nayanjyoti.lucktastic.model.ResponseData;
import com.example.nayanjyoti.lucktastic.model.User;
import com.example.nayanjyoti.lucktastic.service.Client;
import com.example.nayanjyoti.lucktastic.service.NetworkCheck;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouletteGameActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private static final String TAG = "RouleteGameActivity_log";
    private static String email;
    // sectors of our wheel (look at the image to see the sectors)
    private static final int[] numbers = Data.number;

    // We create a Random instance to make our wheel spin randomly
    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;
    // We have 37 sectors on the wheel, we divide 360 by this value to have angle for each sector
    // we divide by 2 to have a half sector
    private static final float HALF_SECTOR = 360f / 37f / 2f;

    private int selectedNumber;

    // wheel image
    private ImageView wheel;

    private TextView chanceLeftText;

    // show gold coin
    private TextView totalCoin;
    private Button spinButton;

    private RewardedVideoAd mRewardedVideoAd;
    private SharePref pref;
    private SimpleDateFormat dateFormat;

    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette_game);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        wheel = findViewById(R.id.roulette_image);
        totalCoin = findViewById(R.id.coin_amount_text_view);
        chanceLeftText = findViewById(R.id.chance_text_view);
        spinButton = findViewById(R.id.spin_button);
        spinButton.setOnClickListener(v->spin());
        //video ad
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        //sharedprefernce data
        pref = SharePref.getInstance(getApplicationContext());
//        -----
        checkNetwork();
        DialogHelper.setNoInternetDialogCreate(this);
        init();
        loadRewardedVideoAd();
    }

    private void checkNetwork() {
        mNetworkReceiver = new NetworkCheck();
        registerNetworkBroadcastForNougat();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void init(){
        email = pref.getEmail();
        totalCoin.setText(String.valueOf(pref.getGoldCoin()));
        chanceLeftText.setText(String.valueOf(pref.getChanceLeft()));
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    private void spin() {
        degreeOld = degree % 360;
        // we calculate random angle for rotation of our wheel
        degree = RANDOM.nextInt(360) + 6*720;
        // rotation effect on the center of the wheel
        RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(3*5600);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start
                //resultTv.setText("");
                spinButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedNumber= Integer.parseInt(getSector(360 - (degree % 360)));
                spinButton.setEnabled(true);
                if(NetworkCheck.isInternetAvailable(getApplicationContext())){
                    showDialog();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // we start the animation
        wheel.startAnimation(rotateAnim);
    }

    private String getSector(int degrees) {
        int i = 0;
        String text = null;

        do {
            // start and end of each sector on the wheel
            float start = HALF_SECTOR * (i * 2 + 1);
            float end = HALF_SECTOR * (i * 2 + 3);

            if (degrees >= start && degrees < end) {
                // degrees is in [start;end[
                // so text is equals to sectors[i];
                int selectedNumber = numbers[i];

                text = Integer.toString(selectedNumber);

            }

            i++;

        } while (text == null  &&  i < numbers.length);

        return text;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d("ad_status","Video Ad Loaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d("ad_status","Video Ad Opened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d("ad_status","Video Ad started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d("ad_status","Video Ad Closed");
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        increaseGoldAmount(selectedNumber);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d("ad_status","Video Ad Left Application");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d("ad_status","Video Load Failed");
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d("ad_status","Video Ad Load Completed");
        loadRewardedVideoAd();
    }

    private void increaseGoldAmount(int amount){
        Client client = Api.getInstance().getClient();
        String url = "user/increaseGold/"+email+"/"+amount;
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<ResponseData> call = client.increaseGold(encrypted);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ResponseData responseData = response.body();
                Boolean failed = responseData.getError();
                if(!failed){
                    User user = responseData.getData();
                    SharePref.getInstance(getApplicationContext()).setGoldCoin(user.getTotalCoin());
                    SharePref.getInstance(getApplicationContext()).setChanceLeft(user.getChanceleft());
                    updateUI(user);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void showDialog(){
        String formatted = getString(R.string.dialog_roulette_finish,selectedNumber);
        Log.d(TAG,String.valueOf(selectedNumber));
        new TTFancyGifDialog.Builder(RouletteGameActivity.this)
                .setTitle("Congratulate")
                .setMessage(formatted)
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#22b573")
                .setNegativeBtnText("Cancel")
                .setNegativeBtnBackground("#c1272d")
                .setGifResource(R.drawable.thumbs_up)      //pass your gif, png or jpg
                .isCancellable(false)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Toast.makeText(RouletteGameActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        }else{
                            loadRewardedVideoAd();
                            Toast.makeText(RouletteGameActivity.this,"Ad is not completely loaded",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .OnNegativeClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(RouletteGameActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

    }

    private void updateUI(User user){
        totalCoin.setText(String.valueOf(user.getTotalCoin()));
        chanceLeftText.setText(String.valueOf(user.getChanceleft()));
    }

    public static void dialog(boolean value){
        if(value){
            DialogHelper.showNoInternetDialog();
        }else{
            DialogHelper.cancelNoInternetDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(this);
        unregisterNetworkChanges();
    }
}
