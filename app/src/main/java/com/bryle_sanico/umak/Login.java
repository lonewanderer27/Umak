package com.bryle_sanico.umak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.card.MaterialCardView;

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

        MaterialCardView usernameCardView = findViewById(R.id.usernameEditText);
        MaterialCardView passwordCardView = findViewById(R.id.passwordEditText);
        EditText inputUsername = findViewById(R.id.inputUsername);
        EditText inputPassword = findViewById(R.id.inputPassword);

        inputUsername.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
//                // Apply the custom background drawable without underline
//                inputUsername.setBackgroundResource(R.drawable.edittext_remove_underline);
                // Apply drop shadow to username card view
                applyDropShadow(usernameCardView);
                // Remove drop shadow from password card view if it was applied earlier
                removeDropShadow(passwordCardView);
            } else {
                // Reapply default background or any other background when focus is lost
                // inputUsername.setBackgroundResource(R.drawable.default_edittext_bg);
                // Remove drop shadow from username card view if the focus is lost
                removeDropShadow(usernameCardView);
            }
        });

        inputPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
//                // Apply the custom background drawable without underline
//                inputPassword.setBackgroundResource(R.drawable.edittext_remove_underline);
                // Apply drop shadow to password card view
                applyDropShadow(passwordCardView);
                // Remove drop shadow from username card view if it was applied earlier
                removeDropShadow(usernameCardView);
            } else {
                // Reapply default background or any other background when focus is lost
                // inputPassword.setBackgroundResource(R.drawable.default_edittext_bg);
                // Remove drop shadow from password card view if the focus is lost
                removeDropShadow(passwordCardView);
            }
        });

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