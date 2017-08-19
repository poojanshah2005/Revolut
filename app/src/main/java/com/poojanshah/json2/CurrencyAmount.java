package com.poojanshah.json2;

/**
 * Created by shahp on 18/08/2017.
 */
public class CurrencyAmount {
    private Double currency = 0.0;
    private ChangeListener listener;

    public Double getCurrency() {
        return currency;
    }

    public void setCurrency(double boo) {
        this.currency = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
