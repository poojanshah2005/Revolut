package com.poojanshah.json2;

/**
 * Created by shahp on 18/08/2017.
 */

public enum CURRENCIES {
    EUR(0),
    USD(1),
    GBP(2);

    private final int value;

    CURRENCIES(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }



    public static CURRENCIES getCURRENCIES(int legIndex) {
        for (CURRENCIES l : CURRENCIES.values()) {
            if (l.value == legIndex) return l;
        }
        throw new IllegalArgumentException("CURRENCY not found. Amputated?");
    }
}
