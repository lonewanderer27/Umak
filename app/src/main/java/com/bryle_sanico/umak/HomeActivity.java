package com.bryle_sanico.umak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bryle_sanico.umak.databinding.ActivityHomeBinding;
import com.bryle_sanico.umak.ui.account.AccountViewModel;
import com.bryle_sanico.umak.ui.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HomeActivity extends AppCompatActivity {

    User user;
    DeviceUserPairing pairing;
    Reading reading;
    private StringRequest sr;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private String URL = "https://plant-iot.vercel.app/_api/", PHPFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_adruino, R.id.nav_account).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Log.i("user", user.toString());

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setUser(user);

        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.setUser(user);

        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = headerView.findViewById(R.id.textView2);
        TextView navHeaderSubtitle = headerView.findViewById(R.id.textView);

        navHeaderTitle.setText(user.first_name);
        navHeaderSubtitle.setText(user.email);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.nav_logout) { // replace 'nav_logout' with the actual id of your logout menu item
                // Clear user data
                clearUserData();

                Intent intent2 = new Intent(HomeActivity.this, Login.class);
                startActivity(intent2);
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                // Navigate to Home
                navController.navigate(R.id.nav_home);
                return true;
            } else if (id == R.id.nav_account) {
                // Navigate to Account
                navController.navigate(R.id.nav_account);
                return true;
            }

            return false;
        });

        if (GetPairing("pairings", 3, 1)) {
            // fetch the latest reading
            homeViewModel.setPairing(pairing);
            if (GetLatestReading("readings", 1)) {
                homeViewModel.setReading(reading);
            }
        }
    }

    public void clearUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public boolean GetLatestReading(String PHPFile, Integer device_id) {
        String route = "/device/" + device_id;
        AtomicReference<Boolean> successful = new AtomicReference<>(false);

        Log.i("GetLatestReading", URL + PHPFile + route);
        sr = new StringRequest(Request.Method.POST, (URL + PHPFile + route), response -> {
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("response", jsonObject.toString());

            try {
                if (jsonObject.getBoolean("success")) { // pairing exists
                    JSONObject readingObj = jsonObject.getJSONObject("reading");

                    // create new reading object
                    reading = new Reading(readingObj.getInt("id"), readingObj.getInt("device_id"), readingObj.getDouble("temperature"), readingObj.getDouble("humidity"), readingObj.getDouble("moisture"), readingObj.getDouble("temperature"), readingObj.getDouble("water_level"));

                    successful.set(true);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        return successful.get();
    }

    public boolean GetPairing(String PHPFile, Integer user_id, Integer device_id) {
        String route = "/user/" + user_id + "/device/" + device_id;
        AtomicReference<Boolean> successful = new AtomicReference<>(false);

        Log.i("GetPairing", URL + PHPFile + route);
        sr = new StringRequest(Request.Method.POST, (URL + PHPFile + route), response -> {
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("response", jsonObject.toString());

            try {
                if (jsonObject.getBoolean("success")) { // pairing exists
                    JSONObject pairingObj = jsonObject.getJSONObject("pairing");

                    // create new pairing object
                    pairing = new DeviceUserPairing(pairingObj.getInt("id"), pairingObj.getInt("device_id"), pairingObj.getInt("user_id"));

                    successful.set(true);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        return successful.get();
    }
}