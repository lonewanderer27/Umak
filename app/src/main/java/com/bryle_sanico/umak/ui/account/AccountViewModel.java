package com.bryle_sanico.umak.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bryle_sanico.umak.User;

public class AccountViewModel extends ViewModel {

    private User user = null;
    private final MutableLiveData<String> mText;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Account fragment TEST");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}