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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class athkarSabaahFloatingService extends Service {

    private boolean isLlShown = false;
    private View cardView;
    private int totalAthkar = 0;
    private TextView finishAthkarTxt;
    private LinearLayout linearLayout;
    private TextView textView;
    private ViewPager2 viewPager2;
    private ArrayList<Theker> athkar;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private athkar_al_masaa_float_adapter athkar_al_masaa_float_adapter;
    private athkar_al_masaa_float_adapter.OnCountBtnClickListener countBtnClickListener ;


    public athkarSabaahFloatingService(){
        }


    @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
            if (finishAthkarTxt != null) mWindowManager.removeView(finishAthkarTxt);
            if (finishAthkarTxt != null) mWindowManager.removeView(cardView);

            Intent intent = new Intent(getApplicationContext(),total_athkar_activity.class);
            intent.putExtra("totalAthkar",totalAthkar);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onCreate() {
            super.onCreate();

            //Inflate the floating view layout we created
            mFloatingView = LayoutInflater.from(this).inflate(R.layout.theker_alsabaah_float_service, null);
            viewPager2 = mFloatingView.findViewById(R.id.theker_alsabaah_float_recycler_view);
            textView = mFloatingView.findViewById(R.id.athkar_al_sabaah_txt);
            linearLayout = mFloatingView.findViewById(R.id.theker_ll);

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            int LAYOUT_FLAG;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }

            //Initialize mFloating view
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.CENTER;
            params.x = 0;
            params.y = 0;
            params.height = mWindowManager.getDefaultDisplay().getHeight();
            params.width = mWindowManager.getDefaultDisplay().getWidth();

            mWindowManager.addView(mFloatingView, params);
            mFloatingView.setVisibility(View.GONE);
            /////////////////////////////////////////////////////////////////


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


            //Initialize cardView
            cardView = LayoutInflater.from(this).inflate(R.layout.athkar_al_sabaah_bubble_card_view, null);
            WindowManager.LayoutParams cardViewParams = new WindowManager.LayoutParams(
                    170,
                    170,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,

                    PixelFormat.TRANSLUCENT
            );

            cardViewParams.gravity = Gravity.TOP | Gravity.START;
            cardViewParams.x = 0;
            cardViewParams.y =200;
            mWindowManager.addView(cardView, cardViewParams);
            /////////////////////////////////////////////////////////////////

            cardView.setOnTouchListener(new View.OnTouchListener() {
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
                            initialX = cardViewParams.x;
                            initialY = cardViewParams.y;

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

                            if(Xdiff < 10 && Ydiff < 10){
                                if (isLlShown) {
                                    cardViewParams.x = 0;
                                    mWindowManager.updateViewLayout(cardView, cardViewParams);
                                    linearLayout.animate()
                                            .scaleY(0.9f)
                                            .scaleX(0.9f)
                                            .alpha(0)
                                            .setDuration(500)
                                            .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            mFloatingView.setVisibility(View.GONE);
                                            isLlShown = false;
                                        }
                                    });

                                } else {
                                    cardViewParams.x = (int) linearLayout.getX();
                                    cardViewParams.y = (-cardView.getHeight()) + ((int) linearLayout.getY()) - 20;
                                    mWindowManager.updateViewLayout(cardView, cardViewParams);
                                    mFloatingView.setVisibility(View.VISIBLE);
                                    linearLayout.animate()
                                            .scaleY(1)
                                            .scaleX(1)
                                            .alpha(1)
                                            .setDuration(500)
                                            .withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    isLlShown = true;
                                                }
                                            });

                                }
                            } else if (cardViewParams.y > finishAthkarParams.y / 0.085) {
                                cardView.animate().scaleY(0).scaleX(0).alpha(0).setDuration(500).start();
                                linearLayout.animate().scaleY(0.9f).scaleX(0.9f).setDuration(300).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayout.animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                stopSelf();
                                            }
                                        }).start();
                                    }
                                }).start();
                            } else if (isLlShown){
                                cardViewParams.x = (int) linearLayout.getX();
                                cardViewParams.y = (-cardView.getHeight()) + ((int) linearLayout.getY()) - 20;
                                mWindowManager.updateViewLayout(cardView, cardViewParams);
                            }
                            else if (cardViewParams.x > (screenWidth - cardView.getWidth()) / 2 && cardViewParams.y < finishAthkarParams.y / 0.085){
                                cardViewParams.x = (screenWidth - cardView.getWidth());
                                mWindowManager.updateViewLayout(cardView, cardViewParams);
                            } else {
                                cardViewParams.x = 0;
                                mWindowManager.updateViewLayout(cardView, cardViewParams);
                            }

                            return true;

                        case MotionEvent.ACTION_MOVE:
                            //Calculate the X and Y coordinates of the view.
                            cardViewParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            cardViewParams.y = initialY + (int) (event.getRawY() - initialTouchY);

                            if (cardViewParams.y > finishAthkarParams.y / 0.085) {
                                finishAthkarTxt.setBackgroundColor(Color.parseColor("#93FF0000"));
                            } else {
                                finishAthkarTxt.setBackgroundColor(Color.parseColor("#98444343"));
                            }

                            //Update the layout with new X & Y coordinate
                            mWindowManager.updateViewLayout(cardView, cardViewParams);
                            return true;
                    }
                    return false;
                }
            });

            initialize_athkar();
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


    private void initialize_athkar() {

            athkar = new ArrayList<Theker>();
        athkar.add(new Theker("أَعُوذُ بِاللهِ مِنْ الشَّيْطَانِ الرَّجِيمِ\n" +
                "اللّهُ لاَ إِلَـهَ إِلاَّ هُوَ الْحَيُّ الْقَيُّومُ لاَ تَأْخُذُهُ سِنَةٌ وَلاَ نَوْمٌ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الأَرْضِ مَن ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلاَّ بِإِذْنِهِ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ وَلاَ يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلاَّ بِمَا شَاء وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالأَرْضَ وَلاَ يَؤُودُهُ حِفْظُهُمَا وَهُوَ الْعَلِيُّ الْعَظِيمُ. [آية الكرسى - البقرة 255].",1));
        athkar.add(new Theker("بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ هُوَ ٱللَّهُ أَحَدٌ، ٱللَّهُ ٱلصَّمَدُ، لَمْ يَلِدْ وَلَمْ يُولَدْ، وَلَمْ يَكُن لَّهُۥ كُفُوًا أَحَدٌۢ.",3));
        athkar.add(new Theker("بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ أَعُوذُ بِرَبِّ ٱلْفَلَقِ، مِن شَرِّ مَا خَلَقَ، وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ، وَمِن شَرِّ ٱلنَّفَّٰثَٰتِ فِى ٱلْعُقَدِ، وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ.",3));
        athkar.add(new Theker("بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ، مَلِكِ ٱلنَّاسِ، إِلَٰهِ ٱلنَّاسِ، مِن شَرِّ ٱلْوَسْوَاسِ ٱلْخَنَّاسِ، ٱلَّذِى يُوَسْوِسُ فِى صُدُورِ ٱلنَّاسِ، مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ.",3));
        athkar.add(new Theker("أَصْـبَحْنا وَأَصْـبَحَ المُـلْكُ لله وَالحَمدُ لله ، لا إلهَ إلاّ اللّهُ وَحدَهُ لا شَريكَ لهُ، لهُ المُـلكُ ولهُ الحَمْـد، وهُوَ على كلّ شَيءٍ قدير ، رَبِّ أسْـأَلُـكَ خَـيرَ ما في هـذا اليوم وَخَـيرَ ما بَعْـدَه ، وَأَعـوذُ بِكَ مِنْ شَـرِّ ما في هـذا اليوم وَشَرِّ ما بَعْـدَه، رَبِّ أَعـوذُبِكَ مِنَ الْكَسَـلِ وَسـوءِ الْكِـبَر ، رَبِّ أَعـوذُ بِكَ مِنْ عَـذابٍ في النّـارِ وَعَـذابٍ في القَـبْر.",1));
        athkar.add(new Theker("اللّهـمَّ أَنْتَ رَبِّـي لا إلهَ إلاّ أَنْتَ ، خَلَقْتَنـي وَأَنا عَبْـدُك ، وَأَنا عَلـى عَهْـدِكَ وَوَعْـدِكَ ما اسْتَـطَعْـت ، أَعـوذُبِكَ مِنْ شَـرِّ ما صَنَـعْت ، أَبـوءُ لَـكَ بِنِعْـمَتِـكَ عَلَـيَّ وَأَبـوءُ بِذَنْـبي فَاغْفـِرْ لي فَإِنَّـهُ لا يَغْـفِرُ الذُّنـوبَ إِلاّ أَنْتَ . ",1));
        athkar.add(new Theker("رَضيـتُ بِاللهِ رَبَّـاً وَبِالإسْلامِ ديـناً وَبِمُحَـمَّدٍ صلى الله عليه وسلم نَبِيّـاً.",3));
        athkar.add(new Theker("اللّهُـمَّ ما أَصْبَـَحَ بي مِـنْ نِعْـمَةٍ أَو بِأَحَـدٍ مِـنْ خَلْـقِك ، فَمِـنْكَ وَحْـدَكَ لا شريكَ لَـك ، فَلَـكَ الْحَمْـدُ وَلَـكَ الشُّكْـر. ",1));
        athkar.add(new Theker("حَسْبِـيَ اللّهُ لا إلهَ إلاّ هُوَ عَلَـيهِ تَوَكَّـلتُ وَهُوَ رَبُّ العَرْشِ العَظـيم.",7));
        athkar.add(new Theker("بِسـمِ اللهِ الذي لا يَضُـرُّ مَعَ اسمِـهِ شَيءٌ في الأرْضِ وَلا في السّمـاءِ وَهـوَ السّمـيعُ العَلـيم.",3));
        athkar.add(new Theker("اللّهُـمَّ بِكَ أَصْـبَحْنا وَبِكَ أَمْسَـينا، وَبِكَ نَحْـيا وَبِكَ نَمُـوتُ وَإِلَـيْكَ النُّـشُور.",1));
        athkar.add(new Theker("أَصْبَـحْـنا عَلَى فِطْرَةِ الإسْلاَمِ، وَعَلَى كَلِمَةِ الإِخْلاَصِ، وَعَلَى دِينِ نَبِيِّنَا مُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ، وَعَلَى مِلَّةِ أَبِينَا إبْرَاهِيمَ حَنِيفاً مُسْلِماً وَمَا كَانَ مِنَ المُشْرِكِينَ.",1));
        athkar.add(new Theker("سُبْحـانَ اللهِ وَبِحَمْـدِهِ عَدَدَ خَلْـقِه ، وَرِضـا نَفْسِـه ، وَزِنَـةَ عَـرْشِـه ، وَمِـدادَ كَلِمـاتِـه.",3));
//        athkar.add(new Theker("اللّهُـمَّ عافِـني في بَدَنـي ، اللّهُـمَّ عافِـني في سَمْـعي ، اللّهُـمَّ عافِـني في بَصَـري ، لا إلهَ إلاّ أَنْـتَ.",3));
//        athkar.add(new Theker("اللّهُـمَّ إِنّـي أَعـوذُ بِكَ مِنَ الْكُـفر ، وَالفَـقْر ، وَأَعـوذُ بِكَ مِنْ عَذابِ القَـبْر ، لا إلهَ إلاّ أَنْـتَ.",3));
        athkar.add(new Theker("اللّهُـمَّ إِنِّـي أسْـأَلُـكَ العَـفْوَ وَالعـافِـيةَ في الدُّنْـيا وَالآخِـرَة ، اللّهُـمَّ إِنِّـي أسْـأَلُـكَ العَـفْوَ وَالعـافِـيةَ في ديني وَدُنْـيايَ وَأهْـلي وَمالـي ، اللّهُـمَّ اسْتُـرْ عـوْراتي وَآمِـنْ رَوْعاتـي ، اللّهُـمَّ احْفَظْـني مِن بَـينِ يَدَيَّ وَمِن خَلْفـي وَعَن يَمـيني وَعَن شِمـالي ، وَمِن فَوْقـي ، وَأَعـوذُ بِعَظَمَـتِكَ أَن أُغْـتالَ مِن تَحْتـي.",1));
        athkar.add(new Theker("يَا حَيُّ يَا قيُّومُ بِرَحْمَتِكَ أسْتَغِيثُ أصْلِحْ لِي شَأنِي كُلَّهُ وَلاَ تَكِلْنِي إلَى نَفْسِي طَـرْفَةَ عَيْنٍ.",3));
        athkar.add(new Theker("اللّهُـمَّ عالِـمَ الغَـيْبِ وَالشّـهادَةِ فاطِـرَ السّماواتِ وَالأرْضِ رَبَّ كـلِّ شَـيءٍ وَمَليـكَه ، أَشْهَـدُ أَنْ لا إِلـهَ إِلاّ أَنْت ، أَعـوذُ بِكَ مِن شَـرِّ نَفْسـي وَمِن شَـرِّ الشَّيْـطانِ وَشِرْكِهِ ، وَأَنْ أَقْتَـرِفَ عَلـى نَفْسـي سوءاً أَوْ أَجُـرَّهُ إِلـى مُسْـلِم.",1));
        athkar.add(new Theker("اللَّهُمَّ صَلِّ وَسَلِّمْ وَبَارِكْ على نَبِيِّنَا مُحمَّد.",10));
//        athkar.add(new Theker("اللَّهُمَّ إِنَّا نَعُوذُ بِكَ مِنْ أَنْ نُشْرِكَ بِكَ شَيْئًا نَعْلَمُهُ ، وَنَسْتَغْفِرُكَ لِمَا لَا نَعْلَمُهُ.",3));// غير مخصص بوقت الصباح والمساء
        athkar.add(new Theker("اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنْ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنْ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ، وَقَهْرِ الرِّجَالِ.",3));// غير مخصص بوقت الصباح والمساء ومرة واحدة
//        athkar.add(new Theker("أسْتَغْفِرُ اللهَ العَظِيمَ الَّذِي لاَ إلَهَ إلاَّ هُوَ، الحَيُّ القَيُّومُ، وَأتُوبُ إلَيهِ.",3));// غير مخصص بوقت الصباح والمساء ومرة واحدة
//        athkar.add(new Theker("يَا رَبِّ , لَكَ الْحَمْدُ كَمَا يَنْبَغِي لِجَلَالِ وَجْهِكَ , وَلِعَظِيمِ سُلْطَانِكَ.",3));//غير مخصص بوقت الصباح والمساء ومرة واحدة
        athkar.add(new Theker("اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلًا مُتَقَبَّلًا.",1));
//        athkar.add(new Theker("اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ ، عَلَيْكَ تَوَكَّلْتُ ، وَأَنْتَ رَبُّ الْعَرْشِ الْعَظِيمِ , مَا شَاءَ اللَّهُ كَانَ ، وَمَا لَمْ يَشَأْ لَمْ يَكُنْ ، وَلا حَوْلَ وَلا قُوَّةَ إِلا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ , أَعْلَمُ أَنَّ اللَّهَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ ، وَأَنَّ اللَّهَ قَدْ أَحَاطَ بِكُلِّ شَيْءٍ عِلْمًا , اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي ، وَمِنْ شَرِّ كُلِّ دَابَّةٍ أَنْتَ آخِذٌ بِنَاصِيَتِهَا ، إِنَّ رَبِّي عَلَى صِرَاطٍ مُسْتَقِيمٍ.",1));// به ضعف
        athkar.add(new Theker("سُبْحـانَ اللهِ وَبِحَمْـدِهِ.",100));
        athkar.add(new Theker("لَا إلَه إلّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءِ قَدِيرِ." +
                "\nاو عشر مرات عند الكسل",100));
        countBtnClickListener = new athkar_al_masaa_float_adapter.OnCountBtnClickListener() {
                @Override
                public void onDeleteItem(athkar_al_masaa_float_adapter athkarAlMasaaFloatAdapter, int position) {
                    athkarAlMasaaFloatAdapter.getAthkar().remove(position);
                    athkarAlMasaaFloatAdapter.notifyItemRemoved(position);

                    if(athkarAlMasaaFloatAdapter.getAthkar().isEmpty()){
                        linearLayout.animate().scaleY(0.9f).scaleX(0.9f).setDuration(300).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cardView.animate().alpha(0).setDuration(500).start();
                                linearLayout.animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopSelf();
                                    }
                                }).start();
                            }
                        }).start();
                    }
                }

            @Override
            public void onBtnClicked() {

                    totalAthkar++;
            }
        };

            athkar_al_masaa_float_adapter = new athkar_al_masaa_float_adapter(athkar, athkarSabaahFloatingService.this,countBtnClickListener,false);
            viewPager2.setAdapter(athkar_al_masaa_float_adapter);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer;
            compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));

            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });
            viewPager2.setPageTransformer(compositePageTransformer);

        }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) mFloatingView);
        fontChanger.setFont_view(finishAthkarTxt);

    }

}

