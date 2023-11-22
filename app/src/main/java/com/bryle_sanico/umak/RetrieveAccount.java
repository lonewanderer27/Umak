package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class RetrieveAccount extends AppCompatActivity {
    public static ProgressDialog progressdialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private Intent directLogin;

    private EditText inputEmail;
    private String URL="http://192.168.0.32/umak/", PHPFile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_account);
        progressdialog = new ProgressDialog(RetrieveAccount.this);
        requestQueue = Volley.newRequestQueue(this);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        inputEmail = findViewById(R.id.inputEmail); // Assuming you have an EditText in your layout with id inputEmail

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.setMessage("Sending Information...");
                progressdialog.show();
                String str_email = inputEmail.getText().toString();
                // SUPPLY THE USERNAME AND PASSWORD DATA FROM THE TEXT FIELD
                if (!Retrieve("reset.php", str_email)) {
                    Toast.makeText(RetrieveAccount.this, "Failed! Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean Retrieve(String PHPFile, String Email) {
        stringRequest = new StringRequest(Request.Method.POST, (URL + PHPFile), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Password sent, Check your Email!!")) {  // Email Sent
                    Toast.makeText(RetrieveAccount.this, response, Toast.LENGTH_LONG).show();
                    // Start your intended activity
                    startActivity(directLogin); // Replace directMain with your Intent
                    finish();
                } else {
                    Toast.makeText(RetrieveAccount.this, response, Toast.LENGTH_LONG).show();
                }
                progressdialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RetrieveAccount.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressdialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("recipient_email", Email);
                return params;
            }
        };
        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(10);
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
}