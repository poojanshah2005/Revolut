package com.poojanshah.json2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.poojanshah.json2.MVP.FlowerListPresenterImpl;
import com.poojanshah.json2.MVP.IFlowerListView;
import com.poojanshah.json2.MVP.interactor.InteractorImpl;
import com.poojanshah.json2.model.Flower;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IFlowerListView {

    FlowerListPresenterImpl flowerListPresenter;
    InteractorImpl interactor_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        interactor_ = new InteractorImpl();
        flowerListPresenter = new FlowerListPresenterImpl(interactor_);

        flowerListPresenter.attachView(this);

        flowerListPresenter.performFlowerListDisplay();
    }

    @Override
    public void onFetchDataSuccess(List<Flower> flowers) {
    }

    @Override
    public void onFetchDataFailure(Throwable throwable) {

    }

    @Override
    public void onFetchDataCompleted() {

    }

    @Override
    public void onFetchDataInProgress() {

    }

}
