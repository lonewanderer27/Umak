package com.bryle_sanico.umak.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bryle_sanico.umak.DeviceUserPairing;
import com.bryle_sanico.umak.R;
import com.bryle_sanico.umak.Reading;
import com.bryle_sanico.umak.User;
import com.bryle_sanico.umak.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    private StringRequest sr;
    private DeviceUserPairing pairing;
    private User user;
    private Reading reading;
    private FragmentHomeBinding binding;
    private TextView Humidity, Moisture, Temperature, WaterLevel;
    private ImageView Refresh;
    private final String URL = "https://plant-iot.vercel.app/_api/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });
        homeViewModel.getPairing().observe(getViewLifecycleOwner(), pairing -> {
            this.pairing = pairing;
        });
        homeViewModel.getReading().observe(getViewLifecycleOwner(), reading -> {
            this.reading = reading;
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Humidity = root.findViewById(R.id.humidity);
        Moisture = root.findViewById(R.id.moisture);
        Temperature = root.findViewById(R.id.temperature);
        WaterLevel = root.findViewById(R.id.waterLevel);
        Refresh = root.findViewById(R.id.btnRefresh);

        Refresh.setOnClickListener(view -> {
            // fetch the latest pairing
            if (GetPairing("pairings", 3, 1)) {
                homeViewModel.setPairing(pairing);
                // fetch the latest reading
                if (GetLatestReading("readings", 1)) {
                    homeViewModel.setReading(reading);

                    Humidity.setText(reading.humidity.toString() + "%");
                    Moisture.setText(reading.soil_moisture.toString() + "%");
                    Temperature.setText(reading.temperature.toString() + "°C");
                    WaterLevel.setText(reading.water_level.toString() + "%");
                } else {
                    Log.e("refresh", "getlatestreading failed");
                }
            } else {
                Log.e("refresh", "getpairing failed");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean GetLatestReading(String PHPFile, Integer device_id) {
        String route = "/device/" + device_id;
        AtomicReference<Boolean> successful = new AtomicReference<>(false);

        sr = new StringRequest(Request.Method.POST, (URL + PHPFile + route), response -> {
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("GetLatestReading response", jsonObject.toString());

            try {
                if (jsonObject.getBoolean("success")) { // pairing exists
                    JSONObject readingObj = jsonObject.getJSONObject("reading");

                    // create new reading object
                    reading = new Reading(
                            readingObj.getInt("id"),
                            readingObj.getInt("device_id"),
                            readingObj.getDouble("temperature"),
                            readingObj.getDouble("humidity"),
                            readingObj.getDouble("moisture"),
                            readingObj.getDouble("temperature"),
                            readingObj.getDouble("water_level")
                    );

                    successful.set(true);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        return successful.get();
    }

    public boolean GetPairing(String PHPFile, Integer user_id, Integer device_id) {
        String route = "/user/" + user_id + "/device/" + device_id;
        AtomicReference<Boolean> successful = new AtomicReference<>(false);
        sr = new StringRequest(Request.Method.POST, (URL + PHPFile + route), response -> {
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("GetPairing response", jsonObject.toString());

            try {
                if (jsonObject.getBoolean("success")) { // pairing exists
                    JSONObject pairingObj = jsonObject.getJSONObject("pairing");

                    // create new pairing object
                    pairing = new DeviceUserPairing(
                            pairingObj.getInt("id"),
                            pairingObj.getInt("device_id"),
                            pairingObj.getInt("user_id")
                    );

                    successful.set(true);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        return successful.get();
    }
}