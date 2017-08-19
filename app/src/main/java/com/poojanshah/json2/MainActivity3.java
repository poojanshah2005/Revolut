package com.poojanshah.json2;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import com.poojanshah.json2.MVP.interactor.Interactor;
import com.poojanshah.json2.MVP.interactor.InteractorImpl;
import com.poojanshah.json2.model.Rates;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

import static com.poojanshah.json2.CURRENCIES.*;

public class MainActivity3 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapterTop sectionsPagerAdapterTop;
    private SectionsPagerAdapterBottom sectionsPagerAdapterBottom;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPagerTop;
    private static ViewPager mViewPagerBottom;

    private static CurrencyVariable currencyVariableTop;
    private static CurrencyVariable currencyVariableBottom;

    private double topRate = 1;
    private double bottomRate = 1;

    private Rates usdRates;
    private Rates eurRates;
    private Rates gbpRates;

    Interactor interactor = new InteractorImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setCurrencyRates();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapterTop = new SectionsPagerAdapterTop(getSupportFragmentManager());
        sectionsPagerAdapterBottom = new SectionsPagerAdapterBottom(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPagerTop = (ViewPager) findViewById(R.id.container);
        mViewPagerTop.setAdapter(sectionsPagerAdapterTop);

        currencyVariableTop = new CurrencyVariable();
        currencyVariableBottom = new CurrencyVariable();

        mViewPagerTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Do not place code here
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelectedB", String.valueOf(getCURRENCIES(position)));
                currencyVariableTop.setCurrency(getCURRENCIES(position));
                Log.i("onPageSelectedTV", String.valueOf(currencyVariableTop.getCurrency().getValue()));
                PlaceholderFragmentTop.getEtAmount().setText("0.0");
                PlaceholderFragmentBottom.getEtAmount().setText("0.0");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mViewPagerBottom = (ViewPager) findViewById(R.id.containerBottom);
        mViewPagerBottom.setAdapter(sectionsPagerAdapterBottom);




        mViewPagerBottom.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Do not place code here
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelectedB", String.valueOf(getCURRENCIES(position)));
                currencyVariableBottom.setCurrency(getCURRENCIES(position));
                Log.i("onPageSelectedBV", String.valueOf(currencyVariableBottom.getCurrency().getValue()));
                PlaceholderFragmentTop.getEtAmount().setText("0.0");
                PlaceholderFragmentBottom.getEtAmount().setText("0.0");
                setTopRate();
                setBottomRate();
    
                Log.i("RateT", String.valueOf(topRate));
                Log.i("RateB", String.valueOf(bottomRate));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



        currencyVariableTop.setListener(() -> Log.i("OnChange", String.valueOf(currencyVariableTop.getCurrency())));
        currencyVariableBottom.setListener(() -> Log.i("OnChange", String.valueOf(currencyVariableTop.getCurrency())));
    }

    public void setCurrencyRates(){
        interactor.getGBP()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity3.this::onSuccessGBP, MainActivity3.this::OnError);
        interactor.getEUR()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity3.this::onSuccessEUR, MainActivity3.this::OnError);
        interactor.getUSD()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(MainActivity3.this::onSuccessUSD, MainActivity3.this::OnError);
    }

    private void onSuccessGBP(Rates rates) {
        this.gbpRates = rates;
    }

    private void onSuccessEUR(Rates rates) {
        this.eurRates = rates;
    }

    private void onSuccessUSD(Rates rates) {
        this.usdRates = rates;
    }

    private void OnError(Throwable throwable) {
        Log.e("Error",throwable.getMessage());
    }

    private void setTopRate(){
        CURRENCIES currencyBottom = currencyVariableBottom.getCurrency();
        CURRENCIES currencyTop = currencyVariableTop.getCurrency();

        if(currencyTop.equals(currencyBottom)){
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

    }

    private void setBottomRate(){
        CURRENCIES currencyBottom = currencyVariableBottom.getCurrency();
        CURRENCIES currencyTop = currencyVariableTop.getCurrency();
        if(currencyTop.equals(currencyBottom)){
            topRate = 1;
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

        public static TextView getTvRate() {
            return tvRate;
        }

        public static EditText getEtAmount() {
            return etAmount;
        }

        public PlaceholderFragmentTop() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         * @param sectionNumber
         */
        public static PlaceholderFragmentTop newInstance(String sectionNumber) {
            PlaceholderFragmentTop fragment = new PlaceholderFragmentTop();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

//            private static ViewPager mViewPagerTop;
//            private ViewPager mViewPagerBottom;


            indicator.setViewPager(mViewPagerTop);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity3, container, false);
            tvCurrency = rootView.findViewById(R.id.tvCurrency);
            tvRate= rootView.findViewById(R.id.tvRate);
            etAmount = rootView.findViewById(R.id.etAmount);
            tvCurrency.setText(getArguments().getString(ARG_SECTION_NUMBER));

            indicator = rootView.findViewById(R.id.indicator);

            Bundle args = getArguments();
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
        private static TextView tvCurrency;
        private static TextView tvRate;
        CircleIndicator indicator;

        public static TextView getTvRate() {
            return tvRate;
        }

        public static EditText getEtAmount() {
            return etAmount;
        }


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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity3, container, false);
            tvCurrency = rootView.findViewById(R.id.tvCurrency);
            tvRate= rootView.findViewById(R.id.tvRate);
            etAmount = rootView.findViewById(R.id.etAmount);

            indicator = rootView.findViewById(R.id.indicator);

            tvCurrency.setText(getArguments().getString(ARG_SECTION_NUMBER));
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

//            private static ViewPager mViewPagerTop;
//            private ViewPager mViewPagerBottom;

            indicator.setViewPager(mViewPagerBottom);
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

            switch (position) {
                case 0:
                    currencyVariableTop.setCurrency(GBP);
                    break;
                case 1:
                    currencyVariableTop.setCurrency(EUR);
                    break;
                case 2:
                    currencyVariableTop.setCurrency(USD);
            }
                return PlaceholderFragmentTop.newInstance(currencyVariableTop.getCurrency().toString());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
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
            switch (position) {
                case 0:
                    currencyVariableBottom.setCurrency(GBP);
                    break;
                case 1:
                    currencyVariableBottom.setCurrency(EUR);
                    break;
                case 2:
                    currencyVariableBottom.setCurrency(USD);
                    break;
            }
                return PlaceholderFragmentBottom.newInstance(currencyVariableBottom.getCurrency().toString());

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
