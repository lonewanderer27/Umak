package com.bryle_sanico.umak;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class DeviceUserPairing implements Serializable {
    public Integer id, device_id, user_id;

    public DeviceUserPairing(int id, int deviceId, int userId) {
        this.id = id;
        this.device_id = deviceId;
        this.user_id = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeviceUserPairing{id=" + id +
                ", device_id='" + device_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
