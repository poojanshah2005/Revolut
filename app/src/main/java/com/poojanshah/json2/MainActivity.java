package com.poojanshah.json2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.poojanshah.json2.interactor.Interactor;
import com.poojanshah.json2.interactor.InteractorImpl;
import com.poojanshah.json2.model.Rates;
import com.poojanshah.json2.variables.CURRENCIES;
import com.poojanshah.json2.variables.CurrencyAmount;
import com.poojanshah.json2.variables.CurrencyVariableType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import me.relex.circleindicator.CircleIndicator;

import static com.poojanshah.json2.variables.CURRENCIES.getCURRENCIES;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "updateUITop";
    public final static String TAG2 = "updateUIBottom";
    public final static String TAG3 = "updateRate";
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
    private static CurrencyVariableType currencyVariableTypeTop;
    private static CurrencyVariableType currencyVariableTypeBottom;
    private static double topRate = 1;
    private static double bottomRate = 1;
    private static CurrencyAmount currencyAmountTop = new CurrencyAmount();
    private static CurrencyAmount currencyAmountBottom = new CurrencyAmount();
    private static Rates usdRates;
    private static Rates eurRates;
    private static Rates gbpRates;
    Interactor interactor = new InteractorImpl();
    private SectionsPagerAdapterBottom sectionsPagerAdapterBottom;

    private static void setTopRate(Context context) {
        try {
            CURRENCIES currencyBottom = currencyVariableTypeBottom.getCurrency();
            CURRENCIES currencyTop = currencyVariableTypeTop.getCurrency();

            if (currencyTop.equals(currencyBottom)) {
                topRate = 1;
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
                    topRate = rates.getRates().getEUR();
                    break;
                case USD:
                    topRate = rates.getRates().getUSD();
                    break;
                case GBP:
                    topRate = rates.getRates().getGBP();
            }
        } catch (NullPointerException exc) {
            Log.e("Error", exc.getMessage());
            bottomRate = 1;
        }
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(TAG3);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static void setBottomRate(Context context) {
        CURRENCIES currencyBottom = currencyVariableTypeBottom.getCurrency();
        CURRENCIES currencyTop = currencyVariableTypeTop.getCurrency();
        try {
            if (currencyTop.equals(currencyBottom)) {
                bottomRate = 1;
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
                    bottomRate = rates.getRates().getEUR();
                    break;
                case USD:
                    bottomRate = rates.getRates().getUSD();
                    break;
                case GBP:
                    bottomRate = rates.getRates().getGBP();
            }
        } catch (NullPointerException exc) {
            Log.e("Error", exc.getMessage());
            bottomRate = 1;
        }
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(TAG3);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateUITop(Context c) {
        setBottomRate(c);
        setTopRate(c);
        double amount = currencyAmountBottom.getCurrency() * bottomRate;
        Log.i("updateUITop", currencyVariableTypeTop.getCurrency().toString());
        Log.i("updateUITop", currencyVariableTypeBottom.getCurrency().toString());
        Log.i("updateUITop", String.valueOf(bottomRate));
        currencyAmountTop.setCurrency(amount);
        Log.d("sender", "Broadcasting message");

        Intent intent = new Intent(TAG);
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
        double amount = currencyAmountTop.getCurrency() * topRate;
        Log.i("updateUIBottom", String.valueOf(topRate));
        currencyAmountBottom.setCurrency(amount);

        Intent intent = new Intent(TAG2);
        // You can also include some extra data.
        intent.putExtra("amount", amount);
        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
    }

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

        currencyAmountTop = new CurrencyAmount();
        currencyAmountBottom = new CurrencyAmount();


        currencyAmountTop.setCurrency(0.0);
        currencyAmountBottom.setCurrency(0.0);

        currencyVariableTypeTop = new CurrencyVariableType();
        currencyVariableTypeBottom = new CurrencyVariableType();

        mViewPagerTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        mViewPagerBottom = (ViewPager) findViewById(R.id.containerBottom);
        mViewPagerBottom.setAdapter(sectionsPagerAdapterBottom);

        mViewPagerBottom.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


                Log.i("RateT", String.valueOf(topRate));
                Log.i("RateB", String.valueOf(bottomRate));

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
                .subscribe(new Consumer<Timed<Long>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void accept(@NonNull Timed<Long> longTimed) throws Exception {
                        //your code here.
                        setCurrencyRates();
                        Log.d("sender", "Broadcasting message");
                        Intent intent = new Intent(TAG3);
                        // You can also include some extra data.
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        Log.i("setCurrencyRates", "setCurrencyRates");
                        setTopRate(getApplicationContext());
                        setBottomRate(getApplicationContext());
                        updateUITop(getApplicationContext());
                        updateUIBottom(getApplicationContext());
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragmentTop extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static EditText etAmount;
        private static TextView tvCurrency;
        private static TextView tvRate;
        CircleIndicator indicator;

        public PlaceholderFragmentTop() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param sectionNumber
         */
        public static PlaceholderFragmentTop newInstance(String sectionNumber) {
            PlaceholderFragmentTop fragment = new PlaceholderFragmentTop();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public static void updateRate() {
            StringBuilder sb = new StringBuilder();
            sb.append(currencyVariableTypeTop.getCurrency().toString());
            sb.append(" 1 = ");
            sb.append(bottomRate);
            sb.append(" ");
            sb.append(currencyVariableTypeBottom.getCurrency().toString());

            tvRate.setText(sb.toString());
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    double message = intent.getDoubleExtra("amount", 0);
                    etAmount = view.findViewById(R.id.etAmount);
                    etAmount.setText(String.format("%.2f", message));
                    updateRate();
                }
            };

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                    new IntentFilter(TAG));

            BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    updateRate();
                }
            };

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver2,
                    new IntentFilter(TAG3));

            tvCurrency = view.findViewById(R.id.tvCurrency);
            tvRate = view.findViewById(R.id.tvRate);
            etAmount = view.findViewById(R.id.etAmount);
            indicator = view.findViewById(R.id.indicator);
            indicator.setViewPager(mViewPagerTop);
            updateRate();

            // Register to receive messages.
            // We are registering an observer (mMessageReceiver) to receive Intents
            // with actions named "custom-event-name".

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable editable) {
                    if (etAmount.getText().hashCode() != editable.hashCode()) {
                        try {
                            double d = Double.parseDouble(editable.toString());
                            currencyAmountTop.setCurrency(d);
                            setTopRate(getContext());
                            setBottomRate(getContext());
                            updateUIBottom(getContext());
                        } catch (NumberFormatException exc) {
                            Log.e("Error", exc.getMessage());
                        }
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
            tvCurrency = rootView.findViewById(R.id.tvCurrency);
            tvRate = rootView.findViewById(R.id.tvRate);
            tvCurrency.setText(getArguments().getString(ARG_SECTION_NUMBER));
            indicator = rootView.findViewById(R.id.indicator);
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragmentBottom extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static EditText etAmount;
        public static View view;
        private static TextView tvCurrency;
        private static TextView tvRate;
        CircleIndicator indicator;

        public PlaceholderFragmentBottom() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragmentBottom newInstance(String sectionNumber) {
            PlaceholderFragmentBottom fragment = new PlaceholderFragmentBottom();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public static void updateRate() {
            StringBuilder sb = new StringBuilder();
            sb.append(currencyVariableTypeBottom.getCurrency().toString());
            sb.append(" 1 = ");
            sb.append(topRate);
            sb.append(" ");
            sb.append(currencyVariableTypeTop.getCurrency().toString());
            tvRate.setText(sb.toString());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
            tvCurrency = rootView.findViewById(R.id.tvCurrency);
            tvRate = rootView.findViewById(R.id.tvRate);
            etAmount = rootView.findViewById(R.id.etAmount);
            indicator = rootView.findViewById(R.id.indicator);
            tvCurrency.setText(getArguments().getString(ARG_SECTION_NUMBER));
            this.view = rootView;
            return rootView;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.view = view;
            tvCurrency = view.findViewById(R.id.tvCurrency);
            tvRate = view.findViewById(R.id.tvRate);
            etAmount = view.findViewById(R.id.etAmount);
            indicator.setViewPager(mViewPagerBottom);
            updateRate();
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable editable) {
                    if (etAmount.getText().hashCode() != editable.hashCode()) {
                        try {
                            double d = Double.parseDouble(editable.toString());
                            setTopRate(getContext());
                            setBottomRate(getContext());
                            Log.i("currencyAmountBottom", String.valueOf(d));
                            currencyAmountBottom.setCurrency(d);
                            updateUITop(getContext());
                        } catch (NumberFormatException exc) {
                            Log.e("Error", exc.getMessage());
                        }
                    }
                }
            });

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    updateUITop(getContext());
                }
            }, 500);// 0.5 seconds delay

            // Our handler for received Intents. This will be called whenever an Intent
            // with an action named "custom-event-name" is broadcasted.
            BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    double message = intent.getDoubleExtra("amount", 1);
                    etAmount = view.findViewById(R.id.etAmount);
                    etAmount.setText(String.format("%.2f", message));
                    updateRate();
                }
            };

            // Register to receive messages.
            // We are registering an observer (mMessageReceiver) to receive Intents
            // with actions named "custom-event-name".
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                    new IntentFilter(TAG2));
            BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    updateRate();
                }
            };
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver2,
                    new IntentFilter(TAG3));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapterTop extends FragmentPagerAdapter {

        public SectionsPagerAdapterTop(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            currencyAmountTop = new CurrencyAmount();
            currencyAmountBottom = new CurrencyAmount();
            return PlaceholderFragmentTop.newInstance(CURRENCIES.getCURRENCIES(position).toString());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return CURRENCIES.values().length;
        }
    }

    public class SectionsPagerAdapterBottom extends FragmentPagerAdapter {

        public SectionsPagerAdapterBottom(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            currencyAmountTop = new CurrencyAmount();
            currencyAmountBottom = new CurrencyAmount();
            return PlaceholderFragmentBottom.newInstance(CURRENCIES.getCURRENCIES(position).toString());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return CURRENCIES.values().length;
        }
    }
}
