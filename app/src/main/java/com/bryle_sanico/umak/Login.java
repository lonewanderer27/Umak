package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    private Button btnGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }


}