package com.wmartinez.devep.trackme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wmartinez.devep.trackme.restApi.EndPoints;
import com.wmartinez.devep.trackme.restApi.adapter.RestApiAdapter;
import com.wmartinez.devep.trackme.restApi.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSingIn, btnSingUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Get a Firebase Auth instance
        auth = FirebaseAuth.getInstance();

        btnSingIn = (Button)findViewById(R.id.sign_in_button);
        btnSingUp = (Button)findViewById(R.id.sign_up_button);
        inputEmail = (EditText)findViewById(R.id.emailS);
        inputPassword = (EditText)findViewById(R.id.passwordS);
        progressBar = (ProgressBar)findViewById(R.id.progressBarS);

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), R.string.enter_email_address, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6){
                    Toast.makeText(getApplicationContext(), R.string.minimum_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                // Create a user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editorAccount = preferencesAccount.edit();
                                    String reformatAccount = email;
                                    String token = FirebaseInstanceId.getInstance().getToken();
                                    reformatAccount = reformatAccount.replace('.', '_');
                                    editorAccount.putString("edit_account", reformatAccount);
                                    editorAccount.putString("device_id", token);
                                    editorAccount.putBoolean("bStart", false);
                                    if (editorAccount.commit()) {
                                        Snackbar.make(view, "Cuenta guardada!!!", Snackbar.LENGTH_LONG)
                                                .setAction("EditAction", null).show();
                                        sendRecordToken(token, reformatAccount);
                                        //sendRecordToken(token, email);
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Snackbar.make(view, "Fallo al guardar cuenta", Snackbar.LENGTH_LONG)
                                                .setAction("EditAction", null).show();
                                    }

                                }
                            }
                        });
            }
        });
    }

    private void sendRecordToken(String token, String account) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPoints endPoints = restApiAdapter.setConnectionRestAPI();
        Call<UserResponse> userResponseCall = endPoints.recordDevice(token, account);

        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
//                Log.d("DEVICE_ID", userResponse.getDevice_id());
                Log.d("USER_NAME", userResponse.getUser_name());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
