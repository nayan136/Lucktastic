package com.example.nayanjyoti.lucktastic;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayanjyoti.lucktastic.model.ResponseData;
import com.example.nayanjyoti.lucktastic.model.User;
import com.example.nayanjyoti.lucktastic.service.Client;
import com.example.nayanjyoti.lucktastic.service.NetworkCheck;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 12;
    private static final String TAG = "MainActivity_log";

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog dialog;
    private TextView totalCoin;

    private BroadcastReceiver mNetworkReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalCoin = findViewById(R.id.coin_amount_text_view);

        checkNetwork();
        DialogHelper.setNoInternetDialogCreate(this);
        createDialog();
//      sign in
        if(SharePref.getInstance(this).getEmail().isEmpty()){
            signIn();
        }else{
            setGoldCoin();
        }

        loadBannerAd();
        loadInterstitialAd();
        buttonClicks();
    }

    private void setGoldCoin() {
        int coinAmount = SharePref.getInstance(this).getGoldCoin();
        MCrypt mcrypt = new MCrypt();
        try {
            String encrypted = MCrypt.bytesToHex( mcrypt.encrypt(String.valueOf(coinAmount)) );
            Log.d(TAG,encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Encryption went wrong");
        }
        totalCoin.setText(String.valueOf(coinAmount));
    }

    private void buttonClicks() {
        // go to roulette game
        findViewById(R.id.roulette_button).setOnClickListener(v->{
            showInterstitial();
        });

        findViewById(R.id.paypal_button).setOnClickListener(v->{
            Intent i = new Intent(this,PaymentActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btn_payment_history).setOnClickListener(v->{
            Intent i = new Intent(this,PaymentHistoryActivity.class);
            startActivity(i);
        });
    }

    private void loadBannerAd() {
//        AdView firstAd = findViewById(R.id.adView);
//        MobileAds.initialize(this, "ca-app-pub-9162845057862669~2626388873");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        firstAd.loadAd(adRequest);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void loadInterstitialAd() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-9162845057862669~2626388873");
        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);

        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent i = new Intent(MainActivity.this,RouletteGameActivity.class);
                startActivity(i);
            }

        });
    }

    private void newlyLoadInterstitialAd(){
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            newlyLoadInterstitialAd();
        }
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

    private void checkNetwork() {
        mNetworkReceiver = new NetworkCheck();
        registerNetworkBroadcastForNougat();
    }

    private void createUser(String email){
//        dialog.show();
        DialogHelper.showDialog();
        Client userClient = Api.getInstance().getClient();
        Log.d(TAG, userClient.createUser(email).request().url().toString());
        String url = "user/create/"+email;
        MCrypt mcrypt = new MCrypt();
        String encrypted="";
        try {
            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(url) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG+"_url",encrypted);
        if(!encrypted.isEmpty()){
            Call<ResponseData> call = userClient.createUser(encrypted);

            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if(response.isSuccessful()){
                        ResponseData responseData = response.body();
                        User user = responseData.getData();
                        Log.d(TAG,user.getEmail()+"-"+user.getCreateDate());
                        SharePref pref = SharePref.getInstance(getApplicationContext());
                        pref.setEmail(user.getEmail());
                        pref.setGoldCoin(user.getTotalCoin());
                        pref.setLastLogin(user.getLastLogin());
                        pref.setChanceLeft(user.getChanceleft());
//                    dialog.dismiss();
                        DialogHelper.cancelDialog();
                        setGoldCoin();
                    }else{
                        Toast.makeText(MainActivity.this,"No data found", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                        DialogHelper.cancelDialog();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }

    }

    private void createDialog(){
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading. Please wait...");
        DialogHelper.createDialog(this);
    }

    private void signIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            createUser(account.getEmail());
            Log.d(TAG, account.getEmail());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
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
        newlyLoadInterstitialAd();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadInterstitialAd();
        setGoldCoin();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}
