package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class Register extends AppCompatActivity {
    public static ProgressDialog progressdialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private Intent directLogin;
    User user;
    private String URL="https://plant-iot.vercel.app/_api/", PHPFile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressdialog = new ProgressDialog(Register.this);
        requestQueue = Volley.newRequestQueue(this);
        directLogin = new Intent(Register.this, Login.class);
        MaterialCardView cpasswordCardView = findViewById(R.id.cpasswordEditText);
        MaterialCardView passwordCardView = findViewById(R.id.passwordEditText);
        MaterialCardView fnameCardView = findViewById(R.id.fNameEditText);
        MaterialCardView mnameCardView = findViewById(R.id.mNameEditText);
        MaterialCardView lnameCardView = findViewById(R.id.lNameEditText);
        MaterialCardView ageCardView = findViewById(R.id.ageEditText);
        MaterialCardView contactCardView = findViewById(R.id.contactEditText);
        MaterialCardView emailCardView = findViewById(R.id.emailEditText);
        MaterialCardView addressCardView = findViewById(R.id.addressEditText);
        EditText inputFname = findViewById(R.id.inputFname);
        EditText inputMname = findViewById(R.id.inputMname);
        EditText inputLname = findViewById(R.id.inputLname);
        EditText inputAge = findViewById(R.id.inputAge);
        EditText inputAddress = findViewById(R.id.inputAddress);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputContact = findViewById(R.id.inputContact);
        EditText inputPassword = findViewById(R.id.inputPassword);
        EditText inputCPassword = findViewById(R.id.inputCPassword);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Resize the CPassword hint to fit in the cardview
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.confirm_password));
        spannableString.setSpan(new AbsoluteSizeSpan(15, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        inputCPassword.setHint(spannableString);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.setMessage("Sending Information...");
                progressdialog.show();

                String str_email = inputEmail.getText().toString();
                String str_password = inputPassword.getText().toString();
                String str_fname = inputFname.getText().toString();
                String str_mname = inputMname.getText().toString();
                String str_lname = inputLname.getText().toString();
                String str_age = inputAge.getText().toString();
                String str_contact = inputContact.getText().toString();
                String str_address = inputAddress.getText().toString();
                String str_cpassword = inputCPassword.getText().toString();
                // Check if passwords match
                if (!str_password.equals(str_cpassword)) {
                    progressdialog.dismiss();
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // SUPPLY THE USERNAME AND PASSWORD DATA FROM THE TEXT FIELD
                if(!CreateAccount("users",str_fname, str_mname, str_lname, str_email, str_contact, str_address,str_age, str_cpassword)){
                    Toast.makeText(Register.this,"Register Failed! Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });

        // On focus functions event
        inputFname.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(fnameCardView);
            } else {
                removeDropShadow(fnameCardView);
            }
        });

        inputMname.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(mnameCardView);
            } else {
                removeDropShadow(mnameCardView);
            }
        });

        inputLname.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(lnameCardView);
            } else {
                removeDropShadow(lnameCardView);
            }
        });

        inputAge.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(ageCardView);
            } else {
                removeDropShadow(ageCardView);
            }
        });

        inputAddress.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(addressCardView);
            } else {
                removeDropShadow(addressCardView);
            }
        });

        inputEmail.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(emailCardView);
            } else {
                removeDropShadow(emailCardView);
            }
        });

        inputContact.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(contactCardView);
            } else {
                removeDropShadow(contactCardView);
            }
        });

        inputPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(passwordCardView);
            } else {
                removeDropShadow(passwordCardView);
            }
        });

        inputCPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(cpasswordCardView);
            } else {
                removeDropShadow(cpasswordCardView);
            }
        });
    }



    public boolean CreateAccount(String PHPFile, String Fname, String Mname,String Lname, String Email, String Contact, String Address, String Age,String Password){
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
//                    user = new User(
//                            userObj.getInt("id"),
//                            userObj.getString("first_name"),
//                            userObj.getString("middle_name"),
//                            userObj.getString("last_name"),
//                            userObj.getString("address"),
//                            userObj.getString("contact_no"),
//                            userObj.getString("email")
//                    );
//
//                    directLogin.putExtra("user", user);
                    startActivity(directLogin);
                    finish();
                } else {
                    Toast.makeText(Register.this, response, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            progressdialog.hide();
        }, error -> {
            Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_LONG).show();
            progressdialog.hide();
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", Fname);
                params.put("middle_name", Mname);
                params.put("last_name", Lname);
                params.put("email", Email);
                params.put("contact_no", Contact);
                params.put("address", Address);
                params.put("age", Age);
                params.put("password", Password);

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