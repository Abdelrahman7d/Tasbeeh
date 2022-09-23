package com.jsla.tasbeeh;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Athkar_Al_Noom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Theker> athkar;
    private AdView mAdView;
    private athkar_al_noom_adapter.OnCountBtnClickListener countBtnClickListener;
    private InterstitialAd mInterstitialAd;
    private AppOpenManager appOpenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athkar_al_noom);

        recyclerView = findViewById(R.id.recycleView);
        mAdView = findViewById(R.id.adView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpenManager = new AppOpenManager(this);
        }

        countBtnClickListener = new athkar_al_noom_adapter.OnCountBtnClickListener() {
            @Override
            public void onClicked3(int position) {

            }

            @Override
            public void onDeleteItem3(athkar_al_noom_adapter athkarAlNoomAdapter, int position) {
                athkar.remove(position);
                athkarAlNoomAdapter.notifyItemRemoved(position);
            }
        };

        initialize_athkar();
        initialize_ads();
        setArabicFont();
    }

    private void initialize_athkar(){
        athkar = new ArrayList<Theker>();

        athkar.add(new Theker("يجمع كفيه ثم ينفث فيهما والقراءة فيهما\u200F:\u200F \u200F{\u200Fقل هو الله أحد\u200F}\u200F و\u200F{\u200Fقل أعوذ برب الفلق\u200F}\u200F و\u200F{\u200Fقل أعوذ برب الناس\u200F}\u200F ومسح ما استطاع من الجسد يبدأ بهما على رأسه ووجه وما أقبل من جسده.",3));
        athkar.add(new Theker("بِاسْمِكَ رَبِّـي وَضَعْـتُ جَنْـبي ، وَبِكَ أَرْفَعُـه، فَإِن أَمْسَـكْتَ نَفْسـي فارْحَـمْها ، وَإِنْ أَرْسَلْتَـها فاحْفَظْـها بِمـا تَحْفَـظُ بِه عِبـادَكَ الصّـالِحـين",1));
        athkar.add(new Theker("اللّهُـمَّ إِنَّـكَ خَلَـقْتَ نَفْسـي وَأَنْـتَ تَوَفّـاهـا لَكَ ممَـاتـها وَمَحْـياها ، إِنْ أَحْيَيْـتَها فاحْفَظْـها ، وَإِنْ أَمَتَّـها فَاغْفِـرْ لَـها . اللّهُـمَّ إِنَّـي أَسْـأَلُـكَ العـافِـيَة. ",1));
        athkar.add(new Theker("اللّهُـمَّ قِنـي عَذابَـكَ يَـوْمَ تَبْـعَثُ عِبـادَك.",1));
        athkar.add(new Theker("بِاسْـمِكَ اللّهُـمَّ أَمـوتُ وَأَحْـيا. ",1));
        athkar.add(new Theker("الـحَمْدُ للهِ الَّذي أَطْـعَمَنا وَسَقـانا، وَكَفـانا، وَآوانا، فَكَـمْ مِمَّـنْ لا كـافِيَ لَـهُ وَلا مُـؤْوي.",1));
        athkar.add(new Theker("اللّهُـمَّ عالِـمَ الغَـيبِ وَالشّـهادةِ فاطِـرَ السّماواتِ وَالأرْضِ رَبَّ كُـلِّ شَـيءٍ وَمَليـكَه، أَشْهـدُ أَنْ لا إِلـهَ إِلاّ أَنْت، أَعـوذُ بِكَ مِن شَـرِّ نَفْسـي، وَمِن شَـرِّ الشَّيْـطانِ وَشِـرْكِه، وَأَنْ أَقْتَـرِفَ عَلـى نَفْسـي سوءاً أَوْ أَجُـرَّهُ إِلـى مُسْـلِم .",1));
        athkar.add(new Theker("سُبْحَانَ اللَّهِ. ",33));
        athkar.add(new Theker("الْحَمْدُ لِلَّهِ. ",33));
        athkar.add(new Theker("اللَّهُ أَكْبَرُ. ",34));
        athkar.add(new Theker("أَعُوذُ بِاللهِ مِنْ الشَّيْطَانِ الرَّجِيمِ\n" +
                "اللّهُ لاَ إِلَـهَ إِلاَّ هُوَ الْحَيُّ الْقَيُّومُ لاَ تَأْخُذُهُ سِنَةٌ وَلاَ نَوْمٌ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الأَرْضِ مَن ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلاَّ بِإِذْنِهِ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ وَلاَ يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلاَّ بِمَا شَاء وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالأَرْضَ وَلاَ يَؤُودُهُ حِفْظُهُمَا وَهُوَ الْعَلِيُّ الْعَظِيمُ. [البقرة 255]",1));

        athkar.add(new Theker("أَعُوذُ بِاللهِ مِنْ الشَّيْطَانِ الرَّجِيمِ\n" +
                "آمَنَ الرَّسُولُ بِمَا أُنْزِلَ إِلَيْهِ مِنْ رَبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِنْ رُسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ. لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ رَبَّنَا لَا تُؤَاخِذْنَا إِنْ نَسِينَا أَوْ أَخْطَأْنَا رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِنْ قَبْلِنَا رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا أَنْتَ مَوْلَانَا فَانْصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ. [البقرة 285 - 286] ",1));
        athkar.add(new Theker("أذكار من قلق في فراشه ولم ينم\n" +
                "عن بريدة رضي الله عنه، قال\u200F:\u200F شكا خالد بن الوليد رضي الله عنه إلى النبي صلى الله عليه وسلم فقال\u200F:\u200F يا رسول الله\u200F!\u200F ما أنام الليل من الأرق، فقال النبي صلى الله عليه وسلم\u200F:\u200F \u200F\"\u200Fإذا أويت إلى فراشك فقل\u200F:\u200F اللهم رب السموات السبع وما أظلت، ورب الأرضين وما أقلت، ورب الشياطين وما أضلت، كن لي جارا من خلقك كلهم جميعا أن يفرط علي أحد منهم أو أن يبغي علي، عز جارك، وجل ثناؤك ولا إله غيرك، ولا إله إلا أنت\u200F\"\n" +
                "\n" +
                "عن عمرو بن شعيب، عن أبيه، عن جده: أن رسول الله صلى الله عليه وسلم كان يعلمهم من الفزع كلمات\u200F:\u200F \u200F\"\u200Fأعوذ بكلمات الله التامة من غضبه وشر عباده، ومن همزات الشياطين وأن يحضرون\u200F\"",1));
        athkar.add(new Theker("أذكار الأحلام\n" +
                "عن أبي قتادة رضي الله عنه قال\u200F:\u200F قال رسول الله صلى الله عليه وسلم\u200F:\u200F \u200F\"\u200Fالرؤيا الصالحة\u200F\"\u200F وفي رواية \u200F\"\u200Fالرؤيا الحسنة من الله، والحلم من الشيطان، فمن رأى شيئا يكرهه فلينفث عن شماله ثلاثا وليتعوذ من الشيطان، فإنها لا تضره\u200F\".",1));
        athkar.add(new Theker("اللّهُـمَّ أَسْـلَمْتُ نَفْـسي إِلَـيْكَ، وَفَوَّضْـتُ أَمْـري إِلَـيْكَ، وَوَجَّـهْتُ وَجْـهي إِلَـيْكَ، وَأَلْـجَـاْتُ ظَهـري إِلَـيْكَ، رَغْبَـةً وَرَهْـبَةً إِلَـيْكَ، لا مَلْجَـأَ وَلا مَنْـجـا مِنْـكَ إِلاّ إِلَـيْكَ، آمَنْـتُ بِكِتـابِكَ الّـذي أَنْزَلْـتَ وَبِنَبِـيِّـكَ الّـذي أَرْسَلْـت.",1));

        athkar_al_noom_adapter athkarAlNoomAdapter = new athkar_al_noom_adapter(athkar, Athkar_Al_Noom.this,countBtnClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(Athkar_Al_Noom.this));
        recyclerView.setAdapter(athkarAlNoomAdapter);

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
            mInterstitialAd.show(Athkar_Al_Noom.this);
        }
    }

}