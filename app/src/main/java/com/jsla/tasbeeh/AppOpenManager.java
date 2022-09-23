package com.jsla.tasbeeh;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks{


    static AppOpenManager appOpenManager;
    private static final String AD_UNIT_ID = "ca-app-pub-2373057914394698/2849012660";
    private AppOpenAd appOpenAd = null;

    FullScreenContentCallback fullScreenContentCallback;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private AppCompatActivity myApplication;

    /** Constructor */
    public AppOpenManager(AppCompatActivity myApplication) {
        this.myApplication = myApplication;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }

        fetchAd();

    }
    AppOpenManager(){

    }

    public void setActivity(AppCompatActivity myApplication){
        this.myApplication = myApplication;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }



        fetchAd();
    }

    /** Request an ad */
    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            return;

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        showAdIfAvailable();
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    private static boolean isShowingAd = false;

    /** Shows the ad if one isn't already showing. */
    public void showAdIfAvailable() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            return;

        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.

        if (!isShowingAd && isAdAvailable()) {

            fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;

                            fetchAd();

                            if(myApplication.getLocalClassName().matches("splash_activity")){

                                myApplication.startActivity(new Intent(myApplication.getApplicationContext(),MainActivity.class));
                                myApplication.finish();
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };


            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(myApplication);

        } else {
            fetchAd();
        }
    }


}