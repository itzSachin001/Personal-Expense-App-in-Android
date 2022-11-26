package com.example.moneymanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister,btnGoogle;
    TextView tvGoToLogin;
    EditText etEmail,etPassword,etUsername,etMobileNumber;

    //Firebase Activity

    FirebaseAuth authentication;

    String email;
    String password;
    String username;
    String mobileNo;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");

        btnRegister=findViewById(R.id.btnRegister);
        btnGoogle=findViewById(R.id.btnGoogle);
        tvGoToLogin=findViewById(R.id.tvGoToLogin);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etUsername=findViewById(R.id.etUsername);
        etMobileNumber=findViewById(R.id.etMobileNo);

        authentication=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkInfo();

                try {
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email required");
                }

                if(TextUtils.isEmpty(password) && password.length()<8){
                    etPassword.setError("Password should be  greater than 8 characters");
                }

                if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Username Required");
                }

                if (TextUtils.isEmpty(mobileNo)){
                    etMobileNumber.setError("Please enter valid phone number");
                }
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username) && TextUtils.isEmpty(mobileNo)){
                    throw new IllegalArgumentException("Invalid registration");
                }

                Dialog progressBar=new Dialog(RegisterActivity.this);
                progressBar.setContentView(R.layout.progress_dialog);
                progressBar.setCancelable(false);

                progressBar.show();


                authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()) {

                                progressBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                finishAffinity();
                            }else {
                            user.sendEmailVerification();
                            Toast.makeText(RegisterActivity.this, "Check your email to verify...", Toast.LENGTH_SHORT).show();
                        }
                        }else{
                            progressBar.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }catch (IllegalArgumentException e){
                    Toast.makeText(RegisterActivity.this, "Invalid registration", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
            }}
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });

    }
    public void checkInfo(){
        email=etEmail.getText().toString().trim();
        password=etPassword.getText().toString().trim();
        username=etUsername.getText().toString().trim();
        mobileNo=etMobileNumber.getText().toString().trim();
    }
}