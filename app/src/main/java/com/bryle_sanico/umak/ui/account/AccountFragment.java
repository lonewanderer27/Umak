package com.bryle_sanico.umak.ui.account;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bryle_sanico.umak.Login;
import com.bryle_sanico.umak.R;
import com.bryle_sanico.umak.User;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private User user;

    private final String URL = "https://plant-iot.vercel.app/_api/";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        setUpEditTextFocusListeners(rootView);
        // Resize hint text for inputCPassword EditText
        EditText cPasswordEditText = rootView.findViewById(R.id.inputCPassword);
        resizeEditTextHint(cPasswordEditText);

        AccountViewModel accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        User user = accountViewModel.getUser();
        this.user = user;

        Log.i("user in Account Fragment", user.toString());

        // Get references to the text fields
        EditText firstNameEditText = rootView.findViewById(R.id.inputFname);
        EditText middleNameEditText = rootView.findViewById(R.id.inputMname);
        EditText lastNameEditText = rootView.findViewById(R.id.inputLname);
        EditText ageEditText = rootView.findViewById(R.id.inputAge);
        EditText contactEditText = rootView.findViewById(R.id.inputContact);
        EditText addressEditText = rootView.findViewById(R.id.inputAddress);
        EditText emailEditText = rootView.findViewById(R.id.inputEmail);
        Button updateButton = rootView.findViewById(R.id.btnSubmit);


        // Update the text fields with the user data
        firstNameEditText.setText(user.first_name);
        middleNameEditText.setText(user.middle_name);
        lastNameEditText.setText(user.last_name);
        ageEditText.setText(String.valueOf(user.age));
        contactEditText.setText(user.contact_no);
        addressEditText.setText(user.address);
        emailEditText.setText(user.email);

        updateButton.setOnClickListener(view -> {
            try {
                if (updateInfo()) {
                    // Update the user in accountviewmodel
                    accountViewModel.setUser(user);

                    // update user preferences
                    saveUserData(user);
                }
            } catch (AuthFailureError e) {
//                throw new RuntimeException(e);
                Log.e("AuthFailureError", e.getMessage());
            }
        });

        return rootView;
    }

    public void saveUserData(User user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
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

    public boolean updateInfo() throws AuthFailureError {
        // Get references to the text fields
        EditText firstNameEditText = requireView().findViewById(R.id.inputFname);
        EditText middleNameEditText = requireView().findViewById(R.id.inputMname);
        EditText lastNameEditText = requireView().findViewById(R.id.inputLname);
        EditText ageEditText = requireView().findViewById(R.id.inputAge);
        EditText contactEditText = requireView().findViewById(R.id.inputContact);
        EditText addressEditText = requireView().findViewById(R.id.inputAddress);
        EditText emailEditText = requireView().findViewById(R.id.inputEmail);
        EditText passwordEditText = requireView().findViewById(R.id.inputPassword);
        EditText passwordCEditText = requireView().findViewById(R.id.inputCPassword);

        // Get the text from the text fields
        String firstName = firstNameEditText.getText().toString();
        String middleName = middleNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String contact = contactEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordC = passwordCEditText.getText().toString();

        // Validate the text fields
        if (firstName.isEmpty()) {
            firstNameEditText.setError("First name is required");
            firstNameEditText.requestFocus();
            return false;
        }

        if (middleName.isEmpty()) {
            middleNameEditText.setError("Middle name is required");
            middleNameEditText.requestFocus();
            return false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            lastNameEditText.requestFocus();
            return false;
        }

        if (age.isEmpty()) {
            ageEditText.setError("Age is required");
            ageEditText.requestFocus();
            return false;
        }

        if (contact.isEmpty()) {
            contactEditText.setError("Contact number is required");
            contactEditText.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            addressEditText.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        if (passwordC.isEmpty()) {
            passwordCEditText.setError("Confirm password is required");
            passwordCEditText.requestFocus();
            return false;
        }

        if (!password.equals(passwordC)) {
            passwordCEditText.setError("Passwords do not match");
            passwordCEditText.requestFocus();
            return false;
        }

        // TODO: Update the user data in the database
        if (updateOnDb("users", user.id, firstName, middleName, lastName, age, contact, address, email, password)) {
            Toast.makeText(requireContext(), "Successfully updated user info", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(requireContext(), "Failed to update user info", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean updateOnDb(String PHPFile, Integer user_id, String FirstName, String MiddleName, String LastName, String Age, String Contact, String Address, String Email, String Password) throws AuthFailureError {
        String route = "/update/id/" + user_id;
        final boolean[] success = {false};

        StringRequest srs = new StringRequest(Request.Method.POST, (URL + PHPFile + route), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);
                    Log.i("response", jsonObject.toString());
                } catch (JSONException e) {
                    Log.e("JSON Exception", e.getMessage());
                    throw new RuntimeException(e);
                }

                Log.i("response", jsonObject.toString());

                try {
                    if (jsonObject.getBoolean("success")) { // pairing exists
                        JSONObject newUser = jsonObject.getJSONObject("user");

                        // create new pairing object
                        user = new User(newUser.getInt("id"), newUser.getInt("age"), newUser.getString("first_name"), newUser.getString("middle_name"), newUser.getString("last_name"), newUser.getString("address"), newUser.getString("contact_no"), newUser.getString("email"));

                        // update the user in accountviewmodel
                        AccountViewModel accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
                        accountViewModel.setUser(user);

                        success[0] = true;
                    }
                } catch (JSONException e) {
                    Log.e("JSON Exception", e.getMessage());
                    Toast.makeText(AccountFragment.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            Log.e("Volley Error", error.getMessage());
            Toast.makeText(AccountFragment.this.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", FirstName);
                params.put("middle_name", MiddleName);
                params.put("last_name", LastName);
                params.put("email", Email);
                params.put("contact_no", Contact);
                params.put("address", Address);
                params.put("age", Age);
                params.put("password", Password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", "base64:0ixCx28dCv5xpHbovzpCV5KEq/1rKfC8U4Ac40NYztI=");
                return params;
            }
        };

        Log.i("request", srs.getBody().toString());

        return success[0];
}

    private void setUpEditTextFocusListeners(View rootView) {
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputFname), rootView.findViewById(R.id.fNameEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputMname), rootView.findViewById(R.id.mNameEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputLname), rootView.findViewById(R.id.lNameEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputAge), rootView.findViewById(R.id.ageEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputContact), rootView.findViewById(R.id.contactEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputAddress), rootView.findViewById(R.id.addressEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputEmail), rootView.findViewById(R.id.emailEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputPassword), rootView.findViewById(R.id.passwordEditText));
        setUpEditTextFocusListener(rootView.findViewById(R.id.inputCPassword), rootView.findViewById(R.id.cpasswordEditText));
    }

    private void setUpEditTextFocusListener(EditText editText, MaterialCardView cardView) {
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                applyDropShadow(cardView);
            } else {
                removeDropShadow(cardView);
            }
        });
    }

    private void resizeEditTextHint(EditText editText) {
        // Change the hint text size for the EditText
        int textSizeInPixels = convertSpToPixels(4); // Convert 16sp to pixels
        editText.setTextSize(textSizeInPixels);
    }

    private int convertSpToPixels(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
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