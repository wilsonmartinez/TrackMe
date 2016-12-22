package com.wmartinez.devep.trackme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSingup, btnLogin, btnReset;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_login);
        // Get Firebase Auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        // Set the view now
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText)findViewById(R.id.emailL);
        inputPassword = (EditText)findViewById(R.id.passwordL);
        progressBar = (ProgressBar)findViewById(R.id.progressBarL);
        btnSingup = (Button)findViewById(R.id.btn_signup);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnReset = (Button)findViewById(R.id.btn_reset_passwordL);
        // Get Firebase authentication instance
        auth = FirebaseAuth.getInstance();

        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                // Authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()){
                                    // There was an error
                                    if(password.length() < 6 ){
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    }else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    SharedPreferences preferencesAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editorAccount = preferencesAccount.edit();
                                    String reformatAccount = email;
                                    String token = FirebaseInstanceId.getInstance().getToken();
                                    reformatAccount = reformatAccount.replace('.', '_');
                                    editorAccount.putString("edit_account", reformatAccount);
                                    editorAccount.putString("device_id", token);
                                    editorAccount.putBoolean("bStart", false);
                                    //String editAccount = preferencesAccount.getString("edit_account", email);
                                    if (editorAccount.commit()) {
                                        Snackbar.make(view, "Cuenta guardada!!!", Snackbar.LENGTH_LONG)
                                                .setAction("EditAction", null).show();
                                        sendRecordToken(token, reformatAccount);
                                        //sendRecordToken(token, email);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
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
}
