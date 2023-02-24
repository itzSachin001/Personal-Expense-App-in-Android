package com.example.moneymanagerapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignOutFragment extends Fragment {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView tvUsername,tvEmail;

    Button btnSignOut;
    CircleImageView profileImage;

    FirebaseAuth auth;
    FirebaseUser user;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView=inflater.inflate(R.layout.fragment_sign_out, container, false);

        profileImage=myView.findViewById(R.id.profile_image);
        tvUsername= myView.findViewById(R.id.tvUsername);
        tvEmail= myView.findViewById(R.id.tvEmail);
        btnSignOut=myView.findViewById(R.id.btnSignOut);

        sharedPreferences =requireContext().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(requireActivity(), gso);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity());



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog=new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Sign Out");
                dialog.setMessage("Are you sure want to sign out?");
                dialog.setIcon(R.drawable.logout);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(user!=null){

                            auth.signOut();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("key", 0);
                            editor.apply();

                            Intent goToMain=new Intent(getActivity(),MainActivity.class);
                            startActivity(goToMain);
                            requireActivity().finish();
                        }



                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", 0);
                        editor.apply();

                        Intent signOut = new Intent(getActivity(), MainActivity.class);
                        startActivity(signOut);
                        requireActivity().finish();

                        gsc.signOut();
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });


        if(googleSignInAccount != null){
            final String username = googleSignInAccount.getDisplayName();
            final String email = Objects.requireNonNull(googleSignInAccount.getEmail()).toUpperCase();
            final Uri photo = googleSignInAccount.getPhotoUrl();

            tvUsername.setText("Name: " + username);
            tvEmail.setText("Email: " + email);
            profileImage.setImageURI(photo);
        }else if(user !=null){
            final String username = user.getUid();
            final String email =user.getEmail().toUpperCase();
            final Uri photo = user.getPhotoUrl();

            tvUsername.setText("User ID: " + username);
            tvEmail.setText("Email: " + email);
            profileImage.setImageURI(photo);
        }

        return myView;
    }
}