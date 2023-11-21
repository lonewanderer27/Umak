package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button btnGoogle, btnCreate, btnLogin;
    public static ProgressDialog progressdialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private Intent directMain;

    private EditText inputUsername, inputPassword;
    private String URL="http://192.168.0.32/umak/", PHPFile="";
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

        directMain = new Intent(Login.this, HomeActivity.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.setMessage("Sending Information...");
                progressdialog.show();
                String str_username = inputUsername.getText().toString();
                String str_password = inputPassword.getText().toString();
                // SUPPLY THE USERNAME AND PASSWORD DATA FROM THE TEXT FIELD
                if(!LoginAccount("login.php",str_username,str_password)){
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

    }


    public boolean LoginAccount(String PHPFile, String UserName, String PassWord){
        stringRequest=new StringRequest(Request.Method.POST, (URL+PHPFile), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success!")){  // user is logged in
                    startActivity(directMain);
                    finish();
                } else {
                    Toast.makeText(Login.this,response, Toast.LENGTH_LONG).show();
                }
                progressdialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
                progressdialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", UserName);
                params.put("user_password", PassWord);
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