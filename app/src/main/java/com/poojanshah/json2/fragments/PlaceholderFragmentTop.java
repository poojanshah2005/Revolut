package com.poojanshah.json2.fragments;

/**
 * Created by shahp on 22/08/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.poojanshah.json2.Consts;
import com.poojanshah.json2.MainActivity;
import com.poojanshah.json2.R;

import me.relex.circleindicator.CircleIndicator;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentTop extends Fragment {
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
        sb.append(MainActivity.getCurrencyVariableTypeTop().getCurrency().toString());
        sb.append(" 1 = ");
        sb.append(MainActivity.getBottomRateValue());
        sb.append(" ");
        sb.append(MainActivity.getCurrencyVariableTypeBottom().getCurrency().toString());

        tvRate.setText(sb.toString());
    }
//
//    public static void updateRate() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(currencyVariableTypeTop.getCurrency().toString());
//        sb.append(" 1 = ");
//        sb.append(bottomRateValue);
//        sb.append(" ");
//        sb.append(currencyVariableTypeBottom.getCurrency().toString());
//
//        tvRate.setText(sb.toString());
//    }

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
                new IntentFilter(Consts.TAG));

        BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                updateRate();
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver2,
                new IntentFilter(Consts.TAG3));

        tvCurrency = view.findViewById(R.id.tvCurrency);
        tvRate = view.findViewById(R.id.tvRate);
        etAmount = view.findViewById(R.id.etAmount);
        indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(MainActivity.getmViewPagerTop());
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
                        MainActivity.getCurrencyAmountTop().setCurrency(d);
                        MainActivity.setTopRate(getContext());
                        MainActivity.setBottomRate(getContext());
                        MainActivity.updateUIBottom(getContext());
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
