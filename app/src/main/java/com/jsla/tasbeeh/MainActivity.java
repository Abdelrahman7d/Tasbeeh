package com.jsla.tasbeeh;

import static com.jsla.tasbeeh.App.CHANNEL_1_ID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private LinearLayout button1, button2, button3, button4,
                         home_btn,masbaha_btn,moreApps_btn,setting_btn,
                         share_app_btn,google_play_btn,contactUs_btn,rate_app_btn,donate_dev_btn,
                         showAthkarAlSabahBubbleLl,showAthkarAlMasaaBubbleLl,
                         showMasbahaLl;
    private CardView allowFloatingServiceCv;
    private ConstraintLayout masbaha_act,main_act,more_act,setting_act;
    private AdView mAdView;
    private Button increase_counter_btn,zero_counter_btn,
            allowFloatingServiceBtn;
    private TextView counter_txt_view,theker_text_view,athkarAlSabahTimeTxtView,athkarAlMasaaTimeTxtView,ayahTextView;
    private Switch isBubbleAllowedSwitch,isNotificationAllowedSwitch,showMasbahaSwitch;

    private boolean isBubbleAllowed =  false,isNotificationAllowed = true, showMasbaha = false,
                    isFirstTime = true;
    private String athkarAlSabaahTime = "",athkarAlMasaaTime = "";
    private SharedPreferences sharedPreferences;
    private int masbahaRepeatTime = 4,impressionsLevel = 0;

    private Spinner impressionsLevelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ayahTextView = findViewById(R.id.ayahTextView);
        button1 = findViewById(R.id.go_to_athkar_al_sabaah_btn);
        button2 = findViewById(R.id.go_to_athkar_al_masaa_btn);
        button3 = findViewById(R.id.go_to_athkar_al_salaah_btn);
        button4 = findViewById(R.id.go_to_athkar_al_noom_btn);
        masbaha_act = findViewById(R.id.masbaha_act);
        main_act = findViewById(R.id.main_act);
        home_btn = findViewById(R.id.home_btn);
        masbaha_btn = findViewById(R.id.masbaha_btn);
        moreApps_btn = findViewById(R.id.moreApps_btn);
        setting_btn = findViewById(R.id.setting_btn);
        increase_counter_btn = findViewById(R.id.increase_counter_btn);
        zero_counter_btn = findViewById(R.id.zero_counter_btn);
        counter_txt_view = findViewById(R.id.counter_text_view);
        theker_text_view = findViewById(R.id.theker);
        mAdView = findViewById(R.id.adView);
        more_act = findViewById(R.id.more_id);
        setting_act = findViewById(R.id.settings_id);
        share_app_btn = findViewById(R.id.share_app_app);
        google_play_btn = findViewById(R.id.more_apps_btn);
        contactUs_btn = findViewById(R.id.contactUs_btn);
        rate_app_btn = findViewById(R.id.rate_app_btn);
        donate_dev_btn = findViewById(R.id.donate_dev_btn);
        isBubbleAllowedSwitch = findViewById(R.id.isBubbleAllowedSwitch);
        isNotificationAllowedSwitch = findViewById(R.id.isNotificationAllowedSwitch);
        showMasbahaSwitch = findViewById(R.id.showMasbahaSwitch);
        athkarAlSabahTimeTxtView = findViewById(R.id.athkarAlSabahTimeTxtView);
        athkarAlMasaaTimeTxtView = findViewById(R.id.athkarAlMasaaTimeTxtView);
        showAthkarAlSabahBubbleLl = findViewById(R.id.showAthkarAlSabahBubbleLl);
        showAthkarAlMasaaBubbleLl = findViewById(R.id.showAthkarAlMasaaBubbleLl);
        allowFloatingServiceCv = findViewById(R.id.allowFloatingServiceCv);
        allowFloatingServiceBtn = findViewById(R.id.allowFloatingServiceBtn);
        impressionsLevelSpinner = findViewById(R.id.impressionsLevelSpinner);
        showMasbahaLl = findViewById(R.id.showMasbahaLl);

        if(Build.VERSION.SDK_INT >= 23){
            if (Settings.canDrawOverlays(getApplicationContext())){
                allowFloatingServiceCv.setVisibility(View.GONE);
            }
        }

        ayahTextView.setY(ayahTextView.getY() + 70);
        ayahTextView.setAlpha(0);

        for(int i = 0; i < 3; i++){

            switch (i){
                case 0:
                    ayahTextView.setText("إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا (56)");
                    break;

                case 1:
                    ayahTextView.setText("وَالذَّاكِرِينَ اللَّهَ كَثِيرًا وَالذَّاكِرَاتِ أَعَدَّ اللَّهُ لَهُم مَّغْفِرَةً وَأَجْرًا عَظِيمًا (35)");
                    break;

                case 2:
                    ayahTextView.setText("الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ (28)");
                    break;
            }

            ayahTextView.animate().y(ayahTextView.getY() + 30).alpha(1).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ayahTextView.animate().y(ayahTextView.getY() + 30).alpha(0).setDuration(700).start();
                            ayahTextView.setY(ayahTextView.getY() - 130);
                        }
                    }, 3000);
                }
            }).start();

        }

        allowFloatingServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1236);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Athkar_Al_Sabaah.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Athkar_Al_Masaa.class));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Athkar_Al_Salaah.class));
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Athkar_Al_Noom.class));
            }
        });


        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#936ED3"));
                }

                masbaha_act.setVisibility(View.GONE);
                main_act.setVisibility(View.VISIBLE);
                more_act.setVisibility(View.GONE);
                setting_act.setVisibility(View.GONE);

            }
        });

        masbaha_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#5EA9E6"));
                }

                main_act.setVisibility(View.GONE);
                masbaha_act.setVisibility(View.VISIBLE);
                more_act.setVisibility(View.GONE);
                setting_act.setVisibility(View.GONE);

            }
        });

        moreApps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#F56258"));
                }
                main_act.setVisibility(View.GONE);
                masbaha_act.setVisibility(View.GONE);
                more_act.setVisibility(View.VISIBLE);
                setting_act.setVisibility(View.GONE);

            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#5C5C5C"));
                }
                main_act.setVisibility(View.GONE);
                masbaha_act.setVisibility(View.GONE);
                more_act.setVisibility(View.GONE);
                setting_act.setVisibility(View.VISIBLE);
            }
        });

        share_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");

                startActivity(sendIntent);
            }
        });

        google_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/search?q=pub%3AJSLA&c=apps"));
                startActivity(intent);
            }
        });

        contactUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/html");
                intent.setData(Uri.parse("mailto:jsla3620@gmail.com"));
                intent.putExtra(Intent.EXTRA_EMAIL, "jsla3620@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        rate_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent);
            }
        });

        donate_dev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.paypal.com/paypalme/JSLAdeveloper?locale.x=ar_EG&fbclid=IwAR0eTfjrsCkRnBiGSE0NqD21wcnKaEQwM0FhZf6U3i1kEzf7H5B89_M3Nps"));
                startActivity(intent);
            }
        });


        increase_counter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(counter_txt_view.getText().toString());

                counter_txt_view.setText("" + ++counter);

                if (counter % 10 == 0){

                    theker_text_view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(500).withEndAction(new Runnable() {
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
        setSettings();

    }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
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

                Toast.makeText(MainActivity.this,loadAdError.getCode() + "",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void setSettings(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        isFirstTime = sharedPreferences.getBoolean("isFirstTime",isFirstTime);

        isBubbleAllowed = sharedPreferences.getBoolean("isBubbleAllowed",isBubbleAllowed);
        isNotificationAllowed = sharedPreferences.getBoolean("isNotificationAllowed",isNotificationAllowed);
        showMasbaha = sharedPreferences.getBoolean("showMasbaha",showMasbaha);

        athkarAlSabaahTime = sharedPreferences.getString("athkarAlSabaahTime", athkarAlSabaahTime);
        athkarAlMasaaTime = sharedPreferences.getString("athkarAlMasaaTime",athkarAlMasaaTime);

        masbahaRepeatTime = sharedPreferences.getInt("masbahaRepeatTime",masbahaRepeatTime);
        impressionsLevel = sharedPreferences.getInt("impressionsLevel",impressionsLevel);

        isBubbleAllowedSwitch.setChecked(isBubbleAllowed);
        isNotificationAllowedSwitch.setChecked(isNotificationAllowed);
        showMasbahaSwitch.setChecked(showMasbaha);
        athkarAlMasaaTimeTxtView.setText(athkarAlMasaaTime);
        athkarAlSabahTimeTxtView.setText(athkarAlSabaahTime);
        impressionsLevelSpinner.setSelection(impressionsLevel);

        isBubbleAllowedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Build.VERSION.SDK_INT >= 23 && isChecked) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("لتتمكن من اظهار الفقاعة الرجاء السماح للتطبيق بالظهور فوق التطبيقات")
                                .setTitle("لا يمكنك اظهار الفقاعة!");

                        builder.setCancelable(false)
                                .setPositiveButton("السماح", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 1237);
                                    }
                                })
                                .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        isBubbleAllowedSwitch.setChecked(false);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {

                        isBubbleAllowed = isChecked;
                        sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();

                        if (isChecked){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(Calendar.getInstance().getTime());

                            calendar.set(Calendar.HOUR_OF_DAY, 17);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            athkarAlMasaaTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                            athkarAlMasaaTimeTxtView.setText(athkarAlMasaaTime);
                            sharedPreferences.edit().putString("athkarAlMasaaTime",athkarAlMasaaTime).apply();

                            startAlarm(calendar,true);

                            calendar.set(Calendar.HOUR_OF_DAY, 5);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            athkarAlSabaahTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                            athkarAlSabahTimeTxtView.setText(athkarAlSabaahTime);
                            sharedPreferences.edit().putString("athkarAlSabaahTime",athkarAlSabaahTime).apply();

                            startAlarm(calendar, false);

                        } else {
                            cancelAlarm(1);
                            cancelAlarm(2);
                        }
                    }
                } else {

                    isBubbleAllowed = isChecked;
                    sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();

                    if (isChecked){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(Calendar.getInstance().getTime());

                        calendar.set(Calendar.HOUR_OF_DAY, 17);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        athkarAlMasaaTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        athkarAlMasaaTimeTxtView.setText(athkarAlMasaaTime);
                        sharedPreferences.edit().putString("athkarAlMasaaTime",athkarAlMasaaTime).apply();

                        startAlarm(calendar,true);

                        calendar.set(Calendar.HOUR_OF_DAY, 5);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        athkarAlSabaahTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        athkarAlSabahTimeTxtView.setText(athkarAlSabaahTime);
                        sharedPreferences.edit().putString("athkarAlSabaahTime",athkarAlSabaahTime).apply();

                        startAlarm(calendar, false);

                    } else {
                        cancelAlarm(1);
                        cancelAlarm(2);
                    }
                }

            }
        });

        isNotificationAllowedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNotificationAllowed = isChecked;
                sharedPreferences.edit().putBoolean("isNotificationAllowed",isNotificationAllowed).apply();
            }
        });

        showMasbahaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Build.VERSION.SDK_INT >= 23 && isChecked) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("لتتمكن من اظهار المسبحة الرجاء السماح للتطبيق بالظهور فوق التطبيقات")
                                .setTitle("لا يمكنك اظهار المسبحة!");

                        builder.setCancelable(false)
                                .setPositiveButton("السماح", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 1238);
                                    }
                                })
                                .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        showMasbahaSwitch.setChecked(false);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {

                        showMasbaha = isChecked;
                        sharedPreferences.edit().putBoolean("showMasbaha",showMasbaha).apply();

                        if (isChecked){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(Calendar.getInstance().getTime());

                            calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getTime().getHours());
                            calendar.set(Calendar.MINUTE, Calendar.getInstance().getTime().getMinutes() + 1);
                            calendar.set(Calendar.SECOND, 0);

                            startAlarm(calendar,impressionsLevel);

                        } else {
                            cancelAlarm(0);
                        }
                    }
                } else {

                    showMasbaha = isChecked;
                    sharedPreferences.edit().putBoolean("showMasbaha",showMasbaha).apply();

                    if (isChecked){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(Calendar.getInstance().getTime());

                        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getTime().getHours());
                        calendar.set(Calendar.MINUTE, Calendar.getInstance().getTime().getMinutes() + 1);
                        calendar.set(Calendar.SECOND, 0);

                        startAlarm(calendar,impressionsLevel);

                    } else {
                        cancelAlarm(0);
                    }
                }
            }
        });

        athkarAlSabahTimeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBubbleAllowed){
                    DialogFragment timePicker = new timePicker();
                    timePicker.show(getSupportFragmentManager(), "athkarAlSabaahTime picker");
                    athkarAlSabaahTime = "";

                } else  {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("لتتمكن من اظهار الفقاعة الرجاء تفعيل خيار فقاعة الأذكار")
                            .setTitle("فقاعة الأذكار غير مفعلة!");

                    builder.setCancelable(false)
                            .setPositiveButton("تفعيل", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isBubbleAllowed = true;
                                    sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();
                                    isBubbleAllowedSwitch.setChecked(isBubbleAllowed);
                                }
                            })
                            .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });

        athkarAlMasaaTimeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isBubbleAllowed){
                    DialogFragment timePicker = new timePicker();
                    timePicker.show(getSupportFragmentManager(), "athkarAlMasaaTime picker");
                    athkarAlMasaaTime = "";

                } else  {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("لتتمكن من اظهار الفقاعة الرجاء تفعيل خيار فقاعة الأذكار")
                            .setTitle("فقاعة الأذكار غير مفعلة!");

                    builder.setCancelable(false)
                            .setPositiveButton("تفعيل", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isBubbleAllowed = true;
                                    sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();
                                    isBubbleAllowedSwitch.setChecked(isBubbleAllowed);
                                }
                            })
                            .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });

        showAthkarAlSabahBubbleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("لتتمكن من اظهار الفقاعة الرجاء السماح للتطبيق بالظهور فوق التطبيقات")
                                .setTitle("لا يمكنك اظهار الفقاعة!");

                        builder.setCancelable(false)
                                .setPositiveButton("السماح", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 1235);
                                    }
                                })
                                .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        startService(new Intent(MainActivity.this,
                                athkarSabaahFloatingService.class));
                    }
                } else {
                    startService(new Intent(MainActivity.this,
                            athkarSabaahFloatingService.class));
                }
            }
        });

        showAthkarAlMasaaBubbleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("لتتمكن من اظهار الفقاعة الرجاء السماح للتطبيق بالظهور فوق التطبيقات")
                                .setTitle("لا يمكنك اظهار الفقاعة!");

                        builder.setCancelable(false)
                                .setPositiveButton("السماح", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 1234);
                                    }
                                })
                                .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        startService(new Intent(MainActivity.this,
                                athkarAlMasaaFloatingService.class));
                    }
                } else {
                    startService(new Intent(MainActivity.this,
                            athkarAlMasaaFloatingService.class));
                }
            }
        });

        showMasbahaLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("لتتمكن من اظهار المسبحة الرجاء السماح للتطبيق بالظهور فوق التطبيقات")
                                .setTitle("لا يمكنك اظهار المسبحة!");

                        builder.setCancelable(false)
                                .setPositiveButton("السماح", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse("package:" + getPackageName()));
                                        startActivityForResult(intent, 1233);
                                    }
                                })
                                .setNegativeButton("لاحقا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        startService(new Intent(MainActivity.this,
                                MasbahaFloatingService.class));
                    }
                } else {
                    startService(new Intent(MainActivity.this,
                            MasbahaFloatingService.class));
                }
            }
        });

        impressionsLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impressionsLevel = position;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("impressionsLevel", impressionsLevel);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Settings.canDrawOverlays(getApplicationContext())){

            setFirstTime();

            isBubbleAllowed = true;
            sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();
            isBubbleAllowedSwitch.setChecked(isBubbleAllowed);

            showMasbaha = true;
            sharedPreferences.edit().putBoolean("showMasbaha",showMasbaha).apply();
            showMasbahaSwitch.setChecked(showMasbaha);

            if (requestCode == 1234) {
                startService(new Intent(MainActivity.this, athkarAlMasaaFloatingService.class));
            } else if (requestCode == 1233) {
                startService(new Intent(MainActivity.this, MasbahaFloatingService.class));
            } else if (requestCode == 1235) {
                startService(new Intent(MainActivity.this, athkarSabaahFloatingService.class));
            }


            allowFloatingServiceCv.setVisibility(View.GONE);
        } else {

            if (requestCode == 1237) {

                isBubbleAllowed = false;
                sharedPreferences.edit().putBoolean("isBubbleAllowed",isBubbleAllowed).apply();
                isBubbleAllowedSwitch.setChecked(isBubbleAllowed);
            } else if (requestCode == 1238) {

                showMasbaha = false;
                sharedPreferences.edit().putBoolean("showMasbaha",showMasbaha).apply();
                showMasbahaSwitch.setChecked(showMasbaha);
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (athkarAlSabaahTime.matches("")){

            athkarAlSabaahTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
            sharedPreferences.edit().putString("athkarAlSabaahTime",athkarAlSabaahTime).apply();
            athkarAlSabahTimeTxtView.setText(athkarAlSabaahTime);
            startAlarm(calendar,false);

        } else if (athkarAlMasaaTime.matches("")){

            athkarAlMasaaTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
            sharedPreferences.edit().putString("athkarAlMasaaTime",athkarAlMasaaTime).apply();
            athkarAlMasaaTimeTxtView.setText(athkarAlMasaaTime);
            startAlarm(calendar,true);
        }

    }

    private void setFirstTime(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());

        calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getTime().getHours());
        calendar.set(Calendar.MINUTE, Calendar.getInstance().getTime().getMinutes() + 1);
        calendar.set(Calendar.SECOND, 0);

        startAlarm(calendar,impressionsLevel);

        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        athkarAlMasaaTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        athkarAlMasaaTimeTxtView.setText(athkarAlMasaaTime);
        sharedPreferences.edit().putString("athkarAlMasaaTime",athkarAlMasaaTime).apply();

        startAlarm(calendar,true);

        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        athkarAlSabaahTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        athkarAlSabahTimeTxtView.setText(athkarAlSabaahTime);
        sharedPreferences.edit().putString("athkarAlSabaahTime",athkarAlSabaahTime).apply();

        startAlarm(calendar, false);

    }

    private void startAlarm(Calendar calendar,boolean isItMasaa) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent;
        if (isItMasaa){
            Intent intent = new Intent(this, athkarAlMasaaReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        } else {
            Intent intent = new Intent(this, athkarAlSabaahReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 2, intent, 0);
        }

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        long interval = 24 * 60 * 60 * 1000 ;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
    }

    private void startAlarm(Calendar calendar,int level) {

        Intent intent = new Intent(this, masbahaReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        long interval;
        if (level == 0){
           interval = (24 * 60 * 60 * 1000) / 20;
        } else if (level == 1) {
            interval = (24 * 60 * 60 * 1000) / 12;
        } else
            interval = (24 * 60 * 60 * 1000) / 6;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
    }

    private void cancelAlarm(int request) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent;

        if (request == 0)
            intent = new Intent(this, masbahaReceiver.class);
        else if (request == 1)
            intent = new Intent(this, athkarAlMasaaReceiver.class);
        else
            intent = new Intent(this, athkarAlSabaahReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, request, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

}