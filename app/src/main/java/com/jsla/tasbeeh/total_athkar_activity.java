package com.jsla.tasbeeh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;


import helperClasses.AdsManager;

public class total_athkar_activity extends AppCompatActivity {

    private ConstraintLayout activityConstraintLayout;
    private TextView totalAthkarTxtView;
    private LinearLayout gotoHomeLinearLayout, gotoMasbahaLinearLayout;
    private AdView mAdView;
    private AdsManager adsManager;
    private InterstitialAd mInterstitialAd[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_athkar);

        setAds();
        setViews();
        setListeners();
        setArabicFont();


        int totalAthkar = getIntent().getIntExtra("totalAthkar",0);

        totalAthkarTxtView.setText("" + totalAthkar);
    }

    private void setViews(){

        activityConstraintLayout = findViewById(R.id.activityConstraintLayout);
        totalAthkarTxtView = findViewById(R.id.totalAthkarTxtView);
        gotoHomeLinearLayout = findViewById(R.id.gotoHomeLinearLayout);
        gotoMasbahaLinearLayout = findViewById(R.id.gotoMasbahaLinearLayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setListeners(){

        activityConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        gotoHomeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });


        gotoMasbahaLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("isToMasbaha",true));
                finish();
            }
        });

    }

    private void setAds(){

        adsManager = new AdsManager(getApplicationContext());

        int numberOfAdsRequests = 2, numberOfInterstitialAd = 1;
        AdRequest adRequest[] = new AdRequest[numberOfAdsRequests];
        mInterstitialAd = new InterstitialAd[numberOfInterstitialAd];

        for (int i = 0; i < numberOfAdsRequests; i++){

            adRequest[i] = adsManager.getAdRequest();
        }

        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest[0]);

        mInterstitialAd =  new InterstitialAd[1];


        InterstitialAd.load(this,"ca-app-pub-2373057914394698/4475750644", adRequest[1],
                adsManager.getInterstitialAdLoadCallback(0,mInterstitialAd));

    }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }

    @Override
    public void finish() {
        super.finish();

        if(mInterstitialAd[0] != null)
            mInterstitialAd[0].show(this);

    }
}