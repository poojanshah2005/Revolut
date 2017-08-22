package com.poojanshah.json2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.poojanshah.json2.adapters.SectionsPagerAdapterBottom;
import com.poojanshah.json2.adapters.SectionsPagerAdapterTop;
import com.poojanshah.json2.fragments.PlaceholderFragmentBottom;
import com.poojanshah.json2.fragments.PlaceholderFragmentTop;
import com.poojanshah.json2.interactor.Interactor;
import com.poojanshah.json2.interactor.InteractorImpl;
import com.poojanshah.json2.model.Rates;
import com.poojanshah.json2.variables.CURRENCIES;
import com.poojanshah.json2.variables.CurrencyAmount;
import com.poojanshah.json2.variables.CurrencyVariableType;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static com.poojanshah.json2.variables.CURRENCIES.getCURRENCIES;

public class MainActivity extends AppCompatActivity {

    public static PlaceholderFragmentBottom placeholderFragmentBottom = new PlaceholderFragmentBottom();
    public static PlaceholderFragmentTop placeholderFragmentTop = new PlaceholderFragmentTop();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static SectionsPagerAdapterTop sectionsPagerAdapterTop;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPagerTop;
    private static ViewPager mViewPagerBottom;
    private static CurrencyVariableType currencyVariableTypeTop = new CurrencyVariableType();
    private static CurrencyVariableType currencyVariableTypeBottom = new CurrencyVariableType();
    private static double topRateValue = 1;
    private static double bottomRateValue = 1;
    private static CurrencyAmount currencyAmountTop = new CurrencyAmount();
    private static CurrencyAmount currencyAmountBottom = new CurrencyAmount();
    private static Rates usdRates;
    private static Rates eurRates;
    private static Rates gbpRates;
    Interactor interactor = new InteractorImpl();
    private SectionsPagerAdapterBottom sectionsPagerAdapterBottom;

    public static ViewPager getmViewPagerTop() {
        return mViewPagerTop;
    }

    public static ViewPager getmViewPagerBottom() {
        return mViewPagerBottom;
    }

    public static CurrencyVariableType getCurrencyVariableTypeTop() {
        return currencyVariableTypeTop;
    }

    public static CurrencyVariableType getCurrencyVariableTypeBottom() {
        return currencyVariableTypeBottom;
    }

    public static double getTopRateValue() {
        return topRateValue;
    }

    public static double getBottomRateValue() {
        return bottomRateValue;
    }

    public static CurrencyAmount getCurrencyAmountTop() {
        return currencyAmountTop;
    }

    public static CurrencyAmount getCurrencyAmountBottom() {
        return currencyAmountBottom;
    }

    public static void setTopRate(Context context) {
        try {
            CURRENCIES currencyBottom = currencyVariableTypeBottom.getCurrency();
            CURRENCIES currencyTop = currencyVariableTypeTop.getCurrency();

            if (currencyTop.equals(currencyBottom)) {
                topRateValue = 1;
                return;
            }
            Rates rates;

            switch (currencyTop) {
                case EUR:
                    rates = eurRates;
                    break;
                case USD:
                    rates = usdRates;
                    break;
                default:
                    rates = gbpRates;
            }

            switch (currencyBottom) {
                case EUR:
                    topRateValue = rates.getRates().getEUR();
                    break;
                case USD:
                    topRateValue = rates.getRates().getUSD();
                    break;
                case GBP:
                    topRateValue = rates.getRates().getGBP();
            }
        } catch (NullPointerException exc) {
            Log.e("Error", exc.getMessage());
            bottomRateValue = 1;
        }
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(Consts.TAG3);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void setBottomRate(Context context) {
        CURRENCIES currencyBottom = currencyVariableTypeBottom.getCurrency();
        CURRENCIES currencyTop = currencyVariableTypeTop.getCurrency();
        try {
            if (currencyTop.equals(currencyBottom)) {
                bottomRateValue = 1;
                return;
            }
            Rates rates;

            switch (currencyBottom) {
                case EUR:
                    rates = eurRates;
                    break;
                case USD:
                    rates = usdRates;
                    break;
                default:
                    rates = gbpRates;
            }

            switch (currencyTop) {
                case EUR:
                    bottomRateValue = rates.getRates().getEUR();
                    break;
                case USD:
                    bottomRateValue = rates.getRates().getUSD();
                    break;
                case GBP:
                    bottomRateValue = rates.getRates().getGBP();
            }
        } catch (NullPointerException exc) {
            Log.e("Error", exc.getMessage());
            bottomRateValue = 1;
        }
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(Consts.TAG3);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateUITop(Context c) {
        setBottomRate(c);
        setTopRate(c);
        double amount = currencyAmountBottom.getCurrency() * bottomRateValue;
        Log.i("updateUITop", currencyVariableTypeTop.getCurrency().toString());
        Log.i("updateUITop", currencyVariableTypeBottom.getCurrency().toString());
        Log.i("updateUITop", String.valueOf(bottomRateValue));
        currencyAmountTop.setCurrency(amount);
        Log.d("sender", "Broadcasting message");

        Intent intent = new Intent(Consts.TAG);
        // You can also include some extra data.
        intent.putExtra("amount", amount);
        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateUIBottom(Context c) {
        setBottomRate(c);
        setTopRate(c);
        Log.i("updateUIBottom", currencyVariableTypeTop.getCurrency().toString());
        Log.i("updateUIBottom", currencyVariableTypeBottom.getCurrency().toString());
        double amount = currencyAmountTop.getCurrency() * topRateValue;
        Log.i("updateUIBottom", String.valueOf(topRateValue));
        currencyAmountBottom.setCurrency(amount);

        Intent intent = new Intent(Consts.TAG2);
        // You can also include some extra data.
        intent.putExtra("amount", amount);
        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCurrencyRates();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapterTop = new SectionsPagerAdapterTop(getSupportFragmentManager());
        sectionsPagerAdapterBottom = new SectionsPagerAdapterBottom(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPagerTop = (ViewPager) findViewById(R.id.container);
        mViewPagerTop.setAdapter(sectionsPagerAdapterTop);
        mViewPagerBottom = (ViewPager) findViewById(R.id.containerBottom);
        mViewPagerBottom.setAdapter(sectionsPagerAdapterBottom);

        currencyAmountTop.setCurrency(0.0);
        currencyAmountBottom.setCurrency(0.0);



        mViewPagerTop.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Do not place code here
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPageSelected(int position) {
                setTopRate(getApplicationContext());
                setBottomRate(getApplicationContext());
                Log.i("onPageSelectedB", String.valueOf(getCURRENCIES(position)));
                currencyVariableTypeTop.setCurrency(getCURRENCIES(position));
                Log.i("onPageSelectedTV", String.valueOf(currencyVariableTypeTop.getCurrency().getValue()));
                updateUIBottom(getApplicationContext());
                PlaceholderFragmentBottom.updateRate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("onPageScrollTop", String.valueOf(state));
            }
        });



        mViewPagerBottom.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Do not place code here
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPageSelected(int position) {
                setTopRate(getApplicationContext());
                setBottomRate(getApplicationContext());
                Log.i("onPageSelectedB", String.valueOf(getCURRENCIES(position)));
                currencyVariableTypeBottom.setCurrency(getCURRENCIES(position));
                Log.i("onPageSelectedBV", String.valueOf(currencyVariableTypeBottom.getCurrency().getValue()));
                updateUIBottom(getApplicationContext());
                updateUITop(getApplicationContext());
                PlaceholderFragmentTop.updateRate();

                Log.i("RateT", String.valueOf(topRateValue));
                Log.i("RateB", String.valueOf(bottomRateValue));

                updateUITop(getApplicationContext());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("onPageScrollBottom", String.valueOf(state));
            }
        });

        currencyVariableTypeTop.setListener(() -> Log.i("OnChange", String.valueOf(currencyVariableTypeTop.getCurrency())));
        currencyVariableTypeBottom.setListener(() -> Log.i("OnChange", String.valueOf(currencyVariableTypeTop.getCurrency())));

        mViewPagerTop.setCurrentItem(CURRENCIES.EUR.getValue());
        mViewPagerBottom.setCurrentItem(CURRENCIES.EUR.getValue());
        currencyVariableTypeTop.setCurrency(CURRENCIES.EUR);
        currencyVariableTypeBottom.setCurrency(CURRENCIES.EUR);

        Disposable timer = Observable.interval(30000, TimeUnit.MILLISECONDS)
                .timeInterval()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(longTimed -> {
                    //your code here.
                    setCurrencyRates();
                    Log.d("sender", "Broadcasting message");
                    Intent intent = new Intent(Consts.TAG3);
                    // You can also include some extra data.
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.i("setCurrencyRates", "setCurrencyRates");
                    setTopRate(getApplicationContext());
                    setBottomRate(getApplicationContext());
                    updateUITop(getApplicationContext());
                    updateUIBottom(getApplicationContext());
                });
    }

    public void setCurrencyRates() {
        interactor.getGBP()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity.this::onSuccessGBP, MainActivity.this::OnError);
        interactor.getEUR()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity.this::onSuccessEUR, MainActivity.this::OnError);
        interactor.getUSD()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity.this::onSuccessUSD, MainActivity.this::OnError);
    }

    private void onSuccessGBP(Rates newRates) {
        gbpRates = newRates;
    }

    private void onSuccessEUR(Rates newRates) {
        eurRates = newRates;
    }

    private void onSuccessUSD(Rates newRates) {
        usdRates = newRates;
    }

    private void OnError(Throwable throwable) {
        Log.e("Error", throwable.getMessage());
    }

}
