package com.jsla.tasbeeh;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class MasbahaFloatingService extends Service {

    private TextView finishAthkarTxt,counter_txt_view ,theker_text_view;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private Button increase_counter_btn,zero_counter_btn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MasbahaFloatingService(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        if (finishAthkarTxt != null) mWindowManager.removeView(finishAthkarTxt);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_masbaha, null);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        increase_counter_btn = mFloatingView.findViewById(R.id.increase_counter_btn);
        zero_counter_btn = mFloatingView.findViewById(R.id.zero_counter_btn);
        counter_txt_view = mFloatingView.findViewById(R.id.counter_text_view);
        theker_text_view = mFloatingView.findViewById(R.id.theker);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //Initialize finishAthkarTxt view
        finishAthkarTxt = new TextView(getApplicationContext());
        finishAthkarTxt.setText("الإنتهاء من الأذكار");
        finishAthkarTxt.setTextSize(18);
        finishAthkarTxt.setGravity(Gravity.CENTER);
        finishAthkarTxt.setTextColor(Color.parseColor("#FFFFFF"));
        finishAthkarTxt.setBackgroundColor(Color.parseColor("#98444343"));
        finishAthkarTxt.setElevation(0f);
        finishAthkarTxt.setVisibility(View.INVISIBLE);
        finishAthkarTxt.setAlpha(0);
        finishAthkarTxt.setScaleY(0);
        finishAthkarTxt.setScaleX(0);

        WindowManager.LayoutParams finishAthkarParams = new WindowManager.LayoutParams(
                700,
                200,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                PixelFormat.TRANSLUCENT
        );
        finishAthkarParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        finishAthkarParams.y = (int) (mWindowManager.getDefaultDisplay().getHeight() * 0.07);
        mWindowManager.addView(finishAthkarTxt,finishAthkarParams);
        /////////////////////////////////////////////////////////////////

        //Initialize mFloating view
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = mWindowManager.getDefaultDisplay().getHeight()/4;

        mWindowManager.addView(mFloatingView, params);
        /////////////////////////////////////////////////////////////////


        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            int screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        finishAthkarTxt.setVisibility(View.VISIBLE);
                        finishAthkarTxt.animate().scaleX(1).scaleY(1).alpha(1).setDuration(300).start();

                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        finishAthkarTxt.animate().scaleX(0.9f).scaleY(0.9f).alpha(0).setDuration(300).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                finishAthkarTxt.setVisibility(View.GONE);
                            }
                        }).start();


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.

                        if(Xdiff < 5 && Ydiff < 5){

                        } else if (params.y > finishAthkarParams.y / 0.135) {

                            mFloatingView.animate().scaleY(0).scaleX(0).alpha(0).setDuration(500).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    stopSelf();
                                }
                            }).start();

                        } else {
                            params.x = 0;
                            mWindowManager.updateViewLayout(mFloatingView, params);
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (params.y > finishAthkarParams.y / 0.135) {
                            finishAthkarTxt.setBackgroundColor(Color.parseColor("#93FF0000"));
                        } else {
                            finishAthkarTxt.setBackgroundColor(Color.parseColor("#98444343"));
                        }

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        increase_counter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(counter_txt_view.getText().toString());

                counter_txt_view.setText("" + ++counter);

                if (counter % 10 == 0){

                    theker_text_view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(300).withEndAction(new Runnable() {
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
        setArabicFont();
        startMyOwnForeground();
    }

    private void startMyOwnForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
    }
    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) mFloatingView);
        fontChanger.setFont_view(finishAthkarTxt);

    }
}
