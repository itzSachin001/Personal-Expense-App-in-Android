package com.example.moneymanagerapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneymanagerapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class IncomeFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference incomeInfo;

    RecyclerView rvIncome;

    TextView incomeResult;

    FirebaseRecyclerAdapter<Data, MyViewHolder> firebaseUsersAdapter = null;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_income, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        //
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(getActivity(),gso);

        //
        assert user != null;
        String uid = user.getUid();

        //
        incomeInfo = FirebaseDatabase.getInstance().getReference().child("Income Data").child(uid);

        rvIncome = myView.findViewById(R.id.rvIncome);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());

        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rvIncome.setHasFixedSize(true);
        rvIncome.setLayoutManager(lm);


        incomeResult=myView.findViewById(R.id.tvIncomeFrag);

            FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                    .setQuery(incomeInfo, Data.class)
                    .build();

            firebaseUsersAdapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                    options) {


                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {

                    holder.setType(model.getType());
                    holder.setNote(model.getNote());
                    holder.setDate(model.getDate());
                    holder.setIncome(model.getAmount());

                }


                @Override
                public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_income, container, false);
                    return new MyViewHolder(view);
                }
            };

            rvIncome.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvIncome.setAdapter(firebaseUsersAdapter);
            firebaseUsersAdapter.startListening();

            incomeInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    double incomeAmount=0.00;

                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        Data data=ds.getValue(Data.class);

                        assert data != null;
                        incomeAmount+=data.getAmount();

                        String incomeAmm=String.valueOf(incomeAmount);

                        incomeResult.setText(incomeAmm);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return myView;
    }


        public static class MyViewHolder extends RecyclerView.ViewHolder {
            View view;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;

            }

            private void setType(String type) {
                TextView incomeType = view.findViewById(R.id.tvTypeIncome);
                incomeType.setText(type);
            }

            private void setNote(String note) {
                TextView incomeTitle = view.findViewById(R.id.tvTitleIncome);
               incomeTitle.setText(note);
            }

            private void setDate(String date) {
                TextView incomeDate = view.findViewById(R.id.tvDateIncome);
                incomeDate.setText(date);
            }

            private void setIncome(int income) {
                TextView setIncome = view.findViewById(R.id.tvIncome);
                String mIncome = String.valueOf(income);
                setIncome.setText(mIncome);
            }
        }
//    }
}