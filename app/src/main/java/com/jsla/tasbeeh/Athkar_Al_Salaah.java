package com.jsla.tasbeeh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

public class Athkar_Al_Salaah extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Theker> athkar;
    private AdView mAdView;
    private athkar_al_salaah_adapter.OnCountBtnClickListener onCountBtnClickListener;
    private InterstitialAd mInterstitialAd;
    private AppOpenManager appOpenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athkar_al_salaah);

        recyclerView = findViewById(R.id.recycleView);
        mAdView = findViewById(R.id.adView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpenManager = new AppOpenManager(this);
        }

        onCountBtnClickListener = new athkar_al_salaah_adapter.OnCountBtnClickListener() {
            @Override
            public void onClicked2(int position) {

            }

            @Override
            public void onDeleteItem2(athkar_al_salaah_adapter athkarAlSalaahAdapter, int position) {
                athkar.remove(position);
                athkarAlSalaahAdapter.notifyItemRemoved(position);
            }
        };

        initialize_athkar();
        initialize_ads();
        setArabicFont();
    }

    private void initialize_athkar(){

        athkar = new ArrayList<Theker>();

        athkar.add(new Theker("أَسْـتَغْفِرُ الله، أَسْـتَغْفِرُ الله، أَسْـتَغْفِرُ الله.\n" +
                "اللّهُـمَّ أَنْـتَ السَّلامُ ، وَمِـنْكَ السَّلام ، تَبارَكْتَ يا ذا الجَـلالِ وَالإِكْـرام . ",1));
        athkar.add(new Theker("لا إلهَ إلاّ اللّهُ وحدَهُ لا شريكَ لهُ، لهُ المُـلْكُ ولهُ الحَمْد، وهوَ على كلّ شَيءٍ قَدير، اللّهُـمَّ لا مانِعَ لِما أَعْطَـيْت، وَلا مُعْطِـيَ لِما مَنَـعْت، وَلا يَنْفَـعُ ذا الجَـدِّ مِنْـكَ الجَـد. ",1));
        athkar.add(new Theker("لا إلهَ إلاّ اللّه, وحدَهُ لا شريكَ لهُ، لهُ الملكُ ولهُ الحَمد، وهوَ على كلّ شيءٍ قدير، لا حَـوْلَ وَلا قـوَّةَ إِلاّ بِاللهِ، لا إلهَ إلاّ اللّـه، وَلا نَعْـبُـدُ إِلاّ إيّـاه, لَهُ النِّعْـمَةُ وَلَهُ الفَضْل وَلَهُ الثَّـناءُ الحَـسَن، لا إلهَ إلاّ اللّهُ مخْلِصـينَ لَـهُ الدِّينَ وَلَوْ كَـرِهَ الكـافِرون. ",1));
        athkar.add(new Theker("سُـبْحانَ اللهِ، والحَمْـدُ لله ، واللهُ أكْـبَر. ",33));
        athkar.add(new Theker("لا إلهَ إلاّ اللّهُ وَحْـدَهُ لا شريكَ لهُ، لهُ الملكُ ولهُ الحَمْد، وهُوَ على كُلّ شَيءٍ قَـدير. ",1));
        athkar.add(new Theker("بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ هُوَ ٱللَّهُ أَحَدٌ، ٱللَّهُ ٱلصَّمَدُ، لَمْ يَلِدْ وَلَمْ يُولَدْ، وَلَمْ يَكُن لَّهُۥ كُفُوًا أَحَدٌۢ.\n" +
                "بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ أَعُوذُ بِرَبِّ ٱلْفَلَقِ، مِن شَرِّ مَا خَلَقَ، وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ، وَمِن شَرِّ ٱلنَّفَّٰثَٰتِ فِى ٱلْعُقَدِ، وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ.\n" +
                "بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n" +
                "قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ، مَلِكِ ٱلنَّاسِ، إِلَٰهِ ٱلنَّاسِ، مِن شَرِّ ٱلْوَسْوَاسِ ٱلْخَنَّاسِ، ٱلَّذِى يُوَسْوِسُ فِى صُدُورِ ٱلنَّاسِ، مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ.\n" +
                " مرة بعد كل صلاة.",3));
        athkar.add(new Theker("لا إلهَ إلاّ اللّهُ وحْـدَهُ لا شريكَ لهُ، لهُ المُلكُ ولهُ الحَمْد، يُحيـي وَيُمـيتُ وهُوَ على كُلّ شيءٍ قدير." +
                "عَشْر مَرّات بَعْدَ المَغْرِب وَالصّـبْح.\n ",10));// فيه خلاف وعادي لو ظل
        athkar.add(new Theker("اللّهُـمَّ إِنِّـي أَسْأَلُـكَ عِلْمـاً نافِعـاً وَرِزْقـاً طَيِّـباً ، وَعَمَـلاً مُتَقَـبَّلاً. \n" +
                "بَعْد السّلامِ من صَلاةِ الفَجْر.",1));

        athkar.add(new Theker("اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ. ",1));

        athkar_al_salaah_adapter athkarAlSalaahAdapter = new athkar_al_salaah_adapter(athkar, Athkar_Al_Salaah.this,onCountBtnClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(Athkar_Al_Salaah.this));
        recyclerView.setAdapter(athkarAlSalaahAdapter);
    }

    private void initialize_ads() {
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

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });

        AdRequest adRequest1 = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-2373057914394698/3124911770", adRequest1,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }

    private void setArabicFont() {
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "Neo-Sans-Arabic-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }

    private void loadInterstitialAd(int adNum){

        if (mInterstitialAd != null && adNum == 1) {
            mInterstitialAd.show(Athkar_Al_Salaah.this);
        }
    }

}