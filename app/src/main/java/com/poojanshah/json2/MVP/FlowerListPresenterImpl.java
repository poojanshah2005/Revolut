package com.poojanshah.json2.MVP;

import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.poojanshah.json2.MVP.interactor.InteractorImpl;
import com.poojanshah.json2.model.Flower;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shahp on 14/07/2017.
 */

public class FlowerListPresenterImpl implements IFlowerListPresenter {
    InteractorImpl interactor_;
    IFlowerListView iFlowerListView;

    @Override
    public void attachView(IFlowerListView MVPView) {
        this.iFlowerListView = MVPView;
    }

    public FlowerListPresenterImpl(InteractorImpl interactor_) {
        this.interactor_ = interactor_;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void performFlowerListDisplay() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) {
                        // do something with isConnectedToInternet value
                        if (isConnectedToInternet) {
//                            interactor_.getFlowerList()
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeOn(Schedulers.newThread())
//                                    .subscribe(this::onSuccess, this::OnError);
//                            Toast.makeText(MainActivity.this,"Network is Available",Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(MainActivity.this,"Network is Available",Toast.LENGTH_LONG).show();
                        }
                    }

                    private void onSuccess(List<Flower> flowers) {
//                        for(Flower f:flowers){
//                            Log.i("Flowers:",f.getName());
//                        }
                        iFlowerListView.onFetchDataSuccess(flowers);
                    }

                    private void OnError(Throwable throwable) {
                        Log.i("throwable.getMessage()", throwable.getMessage());
                        Log.i("throwable.getCause()", String.valueOf(throwable.getCause()));
                    }
                });

    }

//    public FlowerListPresenterImpl(InteractorImpl interactor_) {
//        this.interactor_ = interactor_;
//    }
//
//    @Override
//    public void attachView(IFlowerListView MVPView) {
//        this.iFlowerListView = MVPView;
//    }
//
//    @Override
//    public void detachView() {
//
//    }
//
//    @Override
//    public void performFlowerListDisplay() {
//        ReactiveNetwork.observeInternetConnectivity()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean isConnectedToInternet) {
//                        // do something with isConnectedToInternet value
//                        if (isConnectedToInternet) {
//                            interactor_.getFlowerList()
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeOn(Schedulers.newThread())
//                                    .subscribe(this::onSuccess, this::OnError);
////                            Toast.makeText(MainActivity.this,"Network is Available",Toast.LENGTH_LONG).show();
//                        } else {
////                            Toast.makeText(MainActivity.this,"Network is Available",Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    private void onSuccess(List<Flower> flowers) {
//                        for(Flower f:flowers){
//                            Log.i("Flowers:",f.getName());
//                        }
//                    }
//
//                    private void OnError(Throwable throwable) {
//                        Log.i("throwable.getMessage()", throwable.getMessage());
//                        Log.i("throwable.getCause()", String.valueOf(throwable.getCause()));
//                    }
//                });
//    }

}
