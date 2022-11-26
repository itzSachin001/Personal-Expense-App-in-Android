package com.example.moneymanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.regex.Pattern;

public class ResetActivity extends AppCompatActivity {

    EditText etEmailText;
    Button btnSendLink;

    FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        etEmailText = findViewById(R.id.etEmailText);
        btnSendLink = findViewById(R.id.btnSendLink);

        authProfile = FirebaseAuth.getInstance();

        btnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmailText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmailText.setError("Please enter registered email.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(ResetActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                } else {
                    Dialog progressBar = new Dialog(ResetActivity.this);
                    progressBar.setContentView(R.layout.progress_dialog);
                    progressBar.setCancelable(false);
                    progressBar.show();

                    authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                progressBar.dismiss();

                                Toast.makeText(ResetActivity.this, "Reset link sent to your registered email. ", Toast.LENGTH_SHORT).show();
                                Intent goToMain = new Intent(ResetActivity.this, MainActivity.class);
                                startActivity(goToMain);
                                finishAffinity();

                            }
                            else{

                                progressBar.dismiss();

                                Toast.makeText(ResetActivity.this, "Please enter registered email" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }
        });

    }
}
