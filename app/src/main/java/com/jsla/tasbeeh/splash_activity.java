package com.jsla.tasbeeh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class splash_activity extends AppCompatActivity {

    private ImageView splash_img_view;
    private Button startBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        AppOpenManager appOpenManager = new AppOpenManager(this);
        splash_img_view = findViewById(R.id.splash_img_view);
        startBtn = findViewById(R.id.startBtn);

        startBtn.setVisibility(View.GONE);
        startBtn.setAlpha(0);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                appOpenManager.showAdIfAvailable();

                startBtn.setVisibility(View.VISIBLE);
                startBtn.animate().alpha(1).setDuration(500);

            }
        },3000);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        setArabicFont();
    }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }
}