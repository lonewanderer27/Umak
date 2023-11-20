package com.bryle_sanico.umak.ui.adruino;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdruinoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AdruinoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Adruino fragment TEST");
    }

    public LiveData<String> getText() {
        return mText;
    }
}