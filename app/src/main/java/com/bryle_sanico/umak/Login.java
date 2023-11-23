package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import com.bryle_sanico.umak.User;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button btnGoogle, btnCreate, btnLogin;
    public static ProgressDialog progressdialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private Intent directMain, directRetrieveAccount;
    User user;
    private EditText inputUsername, inputPassword;
    private String URL="https://plant-iot.vercel.app/_api/", PHPFile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressdialog = new ProgressDialog(Login.this);
        requestQueue = Volley.newRequestQueue(this);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnCreate = findViewById(R.id.btnCreate);
        btnLogin = findViewById(R.id.btnLogin);
        inputUsername = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        TextView myTextView = findViewById(R.id.forgotPassword);
        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directRetrieveAccount = new Intent(Login.this, RetrieveAccount.class);
                startActivity(directRetrieveAccount);
            }
        });
        directMain = new Intent(Login.this, HomeActivity.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.setMessage("Sending Information...");
                progressdialog.show();
                String str_username = inputUsername.getText().toString();
                String str_password = inputPassword.getText().toString();

                Log.i("email", str_username);
                Log.i("pass", str_password);

                // SUPPLY THE USERNAME AND PASSWORD DATA FROM THE TEXT FIELD
                if(!LoginAccount("login",str_username,str_password)){
                    Toast.makeText(Login.this,"Login Failed! Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnGoogle.setOnClickListener(view -> {
            startActivity(directMain);
            finish(); // Finish the current activity
        });

        btnCreate.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        MaterialCardView usernameCardView = findViewById(R.id.usernameEditText);
        MaterialCardView passwordCardView = findViewById(R.id.passwordEditText);
        EditText inputUsername = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);

        inputUsername.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                // Apply drop shadow to username card view
                applyDropShadow(usernameCardView);
            } else {
                // Remove drop shadow from username card view if the focus is lost
                removeDropShadow(usernameCardView);
            }
        });

        inputPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                // Apply drop shadow to password card view
                applyDropShadow(passwordCardView);
            } else {
                // Remove drop shadow from password card view if the focus is lost
                removeDropShadow(passwordCardView);
            }
        });

        // Check if the user is already logged in
        if (getUserData().id != 0) {
            user = getUserData();
            directMain.putExtra("user", user);
            startActivity(directMain);
            finish();
        }
    }

    public User getUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        int age = sharedPreferences.getInt("age", 0);
        String firstName = sharedPreferences.getString("first_name", "");
        String middleName = sharedPreferences.getString("middle_name", "");
        String lastName = sharedPreferences.getString("last_name", "");
        String address = sharedPreferences.getString("address", "");
        String contactNo = sharedPreferences.getString("contact_no", "");
        String email = sharedPreferences.getString("email", "");

        return new User(id, age, firstName, middleName, lastName, address, contactNo, email);
    }

    public void saveUserData(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", user.id);
        editor.putString("first_name", user.first_name);
        editor.putString("middle_name", user.middle_name);
        editor.putString("last_name", user.last_name);
        editor.putString("address", user.address);
        editor.putString("contact_no", user.contact_no);
        editor.putString("email", user.email);
        editor.putInt("age", user.age);
        editor.apply();
    }


    public boolean LoginAccount(String PHPFile, String UserName, String PassWord){
        stringRequest=new StringRequest(Request.Method.POST, (URL+PHPFile), response -> {

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(response.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.i("response", jsonObject.toString());
            try {
                if (jsonObject.getBoolean("success")) {  // user is logged in
                    JSONObject userObj = jsonObject.getJSONObject("user");

                    // create user object
                    user = new User(
                            userObj.getInt("id"),
                            userObj.getInt("age"),
                            userObj.getString("first_name"),
                            userObj.getString("middle_name"),
                            userObj.getString("last_name"),
                            userObj.getString("address"),
                            userObj.getString("contact_no"),
                            userObj.getString("email")
                    );

                    directMain.putExtra("user", user);
                    saveUserData(user);

                    startActivity(directMain);
                    finish();
                } else {
                    Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            progressdialog.hide();

        }, error -> {
            Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
            progressdialog.hide();
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", UserName);
                params.put("password", PassWord);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        requestQueue.add(stringRequest);
                        Log.i("string req: ", stringRequest.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }; timer.start();
        return true;
    }

    private void applyDropShadow(MaterialCardView cardView) {
        if (cardView != null) {
            cardView.setCardElevation(18);
            cardView.setStrokeWidth(8);
            cardView.setCardBackgroundColor(Color.LTGRAY);
        }
    }

    private void removeDropShadow(MaterialCardView cardView) {
        if (cardView != null) {
            cardView.setCardElevation(0);
            cardView.setStrokeWidth(0);
            cardView.setCardBackgroundColor(Color.WHITE);
        }
    }

}