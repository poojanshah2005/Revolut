package com.poojanshah.json2;

/**
 * Created by shahp on 18/08/2017.
 */

public enum CURRENCIES {
    EUR(1),
    USD(2),
    GBP(3);

    private final int value;

    CURRENCIES(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
