package com.jsla.tasbeeh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.w3c.dom.Text;

import java.util.Random;

public class Masbaha extends AppCompatActivity {

    private AdView mAdView;
    private Button increase_counter_btn,zero_counter_btn;
    private TextView counter_txt_view,theker_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masbaha);

        increase_counter_btn = findViewById(R.id.increase_counter_btn);
        zero_counter_btn = findViewById(R.id.zero_counter_btn);
        counter_txt_view = findViewById(R.id.counter_text_view);
        theker_text_view = findViewById(R.id.theker);
        mAdView = findViewById(R.id.adView);

        increase_counter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(counter_txt_view.getText().toString());

                counter_txt_view.setText("" + ++counter);

                if (counter % 10 == 0){

                    theker_text_view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();

                            switch (random.nextInt(7)){
                                case 1 : theker_text_view.setText("سبحان الله");
                                    break;

                                case 2 : theker_text_view.setText("الحمد لله");
                                    break;

                                case 3 : theker_text_view.setText("لا إله إلا الله");
                                    break;

                                case 4 : theker_text_view.setText("الله أكبر");
                                    break;

                                case 5 : theker_text_view.setText("لا حول ولا قوة إلا بالله");
                                    break;

                                case 6 : theker_text_view.setText("اللهم صل وسلم على سيدنا محمد");
                                    break;

                            }

                            theker_text_view.animate().scaleX(1).scaleY(1).setDuration(500).start();
                        }
                    }).start();
                }

            }
        });

        zero_counter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_txt_view.setText("0");
            }
        });

        initialize_ads();
        setArabicFont();
    }

    private void initialize_ads(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                Toast.makeText(Masbaha.this,loadAdError.getCode() + "",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }

    @Override
    public void finish() {
        super.finish();
    }
}
