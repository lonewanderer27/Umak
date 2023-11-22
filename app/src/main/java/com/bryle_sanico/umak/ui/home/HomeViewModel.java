package com.bryle_sanico.umak.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bryle_sanico.umak.DeviceUserPairing;
import com.bryle_sanico.umak.Reading;
import com.bryle_sanico.umak.User;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<DeviceUserPairing> pairingData = new MutableLiveData<>();
    private final MutableLiveData<Reading> readingData = new MutableLiveData<>();
    private final MutableLiveData<User> userData = new MutableLiveData<>();

    public HomeViewModel() {

    }

    public void setPairing(DeviceUserPairing pairing) {
        pairingData.setValue(pairing);
    }

    public void setReading(Reading reading) {
        readingData.setValue(reading);
    }

    public void setUser(User user) {
        userData.setValue(user);
    }

    public LiveData<DeviceUserPairing> getPairing() {
        return pairingData;
    }

    public LiveData<Reading> getReading() {
        return readingData;
    }

    public LiveData<User> getUser() {
        return userData;
    }
}