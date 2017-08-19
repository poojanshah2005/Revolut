package com.poojanshah.json2.MVP.interactor;

import com.poojanshah.json2.model.Rates;

import io.reactivex.Observable;

/**
 * Created by shahp on 14/07/2017.
 */

public interface Interactor {
    Observable<Rates> getUSD();
    Observable<Rates> getEUR();
    Observable<Rates> getGBP();
    Observable<Rates> get(String symbol);
}
