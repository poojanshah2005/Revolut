package com.poojanshah.json2.MVP;

import com.poojanshah.json2.model.Flower;

import java.util.List;

/**
 * Created by shahp on 14/07/2017.
 */

public interface IFlowerListView extends MVPView {

    //mvp step 3

    void onFetchDataSuccess(List<Flower> cakesModel);
    void onFetchDataFailure(Throwable throwable);
    void onFetchDataCompleted();
    void onFetchDataInProgress();

}
