package com.example.moneymanagerapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnGoogle;
    TextView tvGoToRegister,tvForgetPassword;
    EditText etEmail1,etPassword1;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    //Firebase

    FirebaseAuth authentication;
    FirebaseUser user;

    SharedPreferences sharedpreferences;
    int autoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalArgumentException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);

        //Default is 0 so autologin is disabled
        if(j > 0){
            Intent activity = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(activity);
            finish();
        }

        authentication=FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign In");

        btnSignIn=findViewById(R.id.btnSignIn);
        btnGoogle=findViewById(R.id.btnGoogle);
        tvGoToRegister=findViewById(R.id.tvGoToRegister);
        etEmail1=findViewById(R.id.etEmail1);
        etPassword1=findViewById(R.id.etPassword1);
        tvForgetPassword=findViewById(R.id.tvForgetPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String email1=etEmail1.getText().toString().trim();
                String password1=etPassword1.getText().toString().trim();

                try {
                if(TextUtils.isEmpty(email1)){
                    etEmail1.setError("Email required");
                }

                if(TextUtils.isEmpty(password1)){
                    etPassword1.setError("Password can not be empty");
                }

                if(TextUtils.isEmpty(email1) && TextUtils.isEmpty(password1)){
                    throw new IllegalArgumentException("Invalid registration");
                }

                Dialog progressBar1=new Dialog(MainActivity.this);
                progressBar1.setContentView(R.layout.progress_dialog);
                progressBar1.setCancelable(false);

                progressBar1.show();


                authentication.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar1.dismiss();

                        if (task.isSuccessful()) {

                                Toast.makeText(MainActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();

                                autoSave = 1;
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt("key", autoSave);
                                editor.apply();

                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finishAffinity();

                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed...", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }catch (IllegalArgumentException e){
                    Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }}
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,ResetActivity.class));
                finish();
            }
        });

        // Google Sign In

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account !=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult.launch(signInIntent);
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityForResult=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                        // getting signed in account after user selected an account from google account dialog
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                }


            });

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            //getting account data
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
            }

            //opening Home Activity
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        } catch (ApiException e) {
            Log.d("Google Error",e.getMessage());
        }
    }
}