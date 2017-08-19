package com.poojanshah.json2;

/**
 * Created by shahp on 18/08/2017.
 */
public class CurrencyVariableType {
    private CURRENCIES currency = CURRENCIES.EUR;
    private ChangeListener listener;

    public CURRENCIES getCurrency() {
        return currency;
    }

    public void setCurrency(CURRENCIES boo) {
        this.currency = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener extends CurrencyAmount.ChangeListener {
        void onChange();
    }
}
