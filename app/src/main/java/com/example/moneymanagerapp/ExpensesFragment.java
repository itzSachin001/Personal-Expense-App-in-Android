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

import com.bumptech.glide.load.model.Model;
import com.example.moneymanagerapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ExpensesFragment extends Fragment {

    FirebaseAuth auth;
    DatabaseReference expenseInfo;

    RecyclerView rvExpense;

    TextView expenseResult;

    //myAdapter adapter;

    FirebaseRecyclerAdapter<Data, MyViewHolder> firebaseUsersAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_expenses, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        String uid = user.getUid();

        expenseInfo = FirebaseDatabase.getInstance().getReference().child("Expense Data").child(uid);

        rvExpense = myView.findViewById(R.id.rvExpense);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());

        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rvExpense.setHasFixedSize(true);
        rvExpense.setLayoutManager(lm);

        expenseResult=myView.findViewById(R.id.tvExpenseFrag);


        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(expenseInfo, Data.class)
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


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_expense, container, false);
                return new MyViewHolder(view);
            }
        };

        rvExpense.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvExpense.setAdapter(firebaseUsersAdapter);
        firebaseUsersAdapter.startListening();

        expenseInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double expenseAmount=0.00;

                for(DataSnapshot ds:snapshot.getChildren()){

                    Data data=ds.getValue(Data.class);
                    assert data != null;
                    expenseAmount+=data.getAmount();

                    String expenseAmm=String.valueOf(expenseAmount);

                    expenseResult.setText(expenseAmm);
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
            TextView expenseType = view.findViewById(R.id.tvTypeExpense);
            expenseType.setText(type);
        }

        private void setNote(String note) {
            TextView expenseTitle = view.findViewById(R.id.tvTitleExpense);
            expenseTitle.setText(note);
        }

        private void setDate(String date) {
            TextView expenseDate = view.findViewById(R.id.tvDateExpense);
            expenseDate.setText(date);
        }

        private void setIncome(int expense) {
            TextView setExpense = view.findViewById(R.id.tvExpense);
            String mExpense = String.valueOf(expense);
            setExpense.setText(mExpense);
        }
    }
//    }
}