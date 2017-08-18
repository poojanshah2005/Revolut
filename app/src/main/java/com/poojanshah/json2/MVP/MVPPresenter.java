package com.poojanshah.json2.MVP;

/**
 * Created by shahp on 14/07/2017.
 */

public interface MVPPresenter <v extends MVPView> {

    void attachView(v MVPView);

    void detachView();

}
