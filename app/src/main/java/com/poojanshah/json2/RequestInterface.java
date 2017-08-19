package com.poojanshah.json2;

import com.poojanshah.json2.model.Rates;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Poojan on 12/07/2017.
 */
public  interface RequestInterface {
    @GET(Consts.USD)
    Observable<Rates> getUSD();
    @GET(Consts.EUR)
    Observable<Rates> getEUR();
    @GET(Consts.GBP)
    Observable<Rates> getGBP();
}
