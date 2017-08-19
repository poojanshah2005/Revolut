package com.poojanshah.json2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rates implements Parcelable
{

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rates")
    @Expose
    private Rates_ rates;
    public final static Parcelable.Creator<Rates> CREATOR = new Creator<Rates>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Rates createFromParcel(Parcel in) {
            Rates instance = new Rates();
            instance.base = ((String) in.readValue((String.class.getClassLoader())));
            instance.date = ((String) in.readValue((String.class.getClassLoader())));
            instance.rates = ((Rates_) in.readValue((Rates_.class.getClassLoader())));
            return instance;
        }

        public Rates[] newArray(int size) {
            return (new Rates[size]);
        }

    }
            ;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Rates_ getRates() {
        return rates;
    }

    public void setRates(Rates_ rates) {
        this.rates = rates;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(base);
        dest.writeValue(date);
        dest.writeValue(rates);
    }

    public int describeContents() {
        return 0;
    }

}