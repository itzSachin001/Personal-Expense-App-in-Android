package com.example.moneymanagerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymanagerapp.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.text.DateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FloatingActionButton fbtnExpense,fbtnIncome,fbtn;
    private TextView ft_text_Expense,ft_text_Income;
    private TextView resultIncome,resultExpense;

    private boolean isOpen=false;

    Animation fade_open,fade_close;

    private FirebaseAuth auth;
    private DatabaseReference incomeDatabase;
    private DatabaseReference expenseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView= inflater.inflate(R.layout.fragment_dashboard, container, false);

        fbtnExpense=myView.findViewById(R.id.fbtnExpense);
        fbtnIncome=myView.findViewById(R.id.fbtnIncome);
        fbtn=myView.findViewById(R.id.fbtn);
        ft_text_Expense=myView.findViewById(R.id.ft_text_Expense);
        ft_text_Income=myView.findViewById(R.id.ft_text_Income);

        resultIncome=myView.findViewById(R.id.resultIncome);
        resultExpense=myView.findViewById(R.id.resultExpenses);

        fade_open= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fade_close=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        //firebase

        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        assert user != null;
        String uid=user.getUid();

        incomeDatabase= FirebaseDatabase.getInstance().getReference().child("Income Data").child(uid);
        expenseDatabase=FirebaseDatabase.getInstance().getReference().child("Expense Data").child(uid);

        incomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double incomeResult=0.00;

                for(DataSnapshot ds:snapshot.getChildren()){

                    Data data=ds.getValue(Data.class);
                    assert data != null;
                    incomeResult+=data.getAmount();

                    String incomeAmm=String.valueOf(incomeResult);

                    resultIncome.setText(incomeAmm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        expenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double expenseResult=0.00;

                for(DataSnapshot ds:snapshot.getChildren()){

                    Data data=ds.getValue(Data.class);
                    assert data != null;
                    expenseResult+=data.getAmount();

                    String incomeAmm=String.valueOf(expenseResult);

                    resultExpense.setText(incomeAmm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addData();
                ftAnimation();

            }
        });

        return myView;
    }

    private void addData()
    {
        fbtnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog incomeDialog= new Dialog(getActivity());
                View myView=LayoutInflater.from(getActivity()).inflate(R.layout.custom_layout_insertdata,null);

                incomeDialog.setContentView(myView);
                incomeDialog.show();
                incomeDialog.setCancelable(false);

                EditText etIncomeAmount=myView.findViewById(R.id.etAmount);
                EditText etIncomeType=myView.findViewById(R.id.etType);
                EditText etIncomeNote=myView.findViewById(R.id.etNote);

                Button btnSave=myView.findViewById(R.id.btnSave_InsertData);
                Button btnCancel=myView.findViewById(R.id.btnCancel_InsertData);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String inAmount=etIncomeAmount.getText().toString().trim();
                        String inType=etIncomeType.getText().toString().trim();
                        String inNote=etIncomeNote.getText().toString().trim();

                        try {
                            if (TextUtils.isEmpty(inAmount)) {
                                etIncomeAmount.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(inType)) {
                                etIncomeType.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(inNote)) {
                                etIncomeNote.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(inAmount) && TextUtils.isEmpty(inType) && TextUtils.isEmpty(inNote)) {
                                throw new IllegalArgumentException("Invalid Data");
                            }



                        int ourAmount=Integer.parseInt(inAmount);

                        String id=incomeDatabase.push().getKey();
                        String date= DateFormat.getDateInstance().format(new Date());

                        Data data=new Data(ourAmount,inType,inNote,id,date);

                        assert id != null;
                        incomeDatabase.child(id).setValue(data);

                        Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                        incomeDialog.dismiss();
                        ftAnimation();
                    }catch (IllegalArgumentException e){
                        Toast.makeText(getActivity(), "Invalid Data", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }}
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ftAnimation();
                        incomeDialog.dismiss();
                    }
                });
            }
        });


        fbtnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog expenseDialog= new Dialog(getActivity());
                View myView=LayoutInflater.from(getActivity()).inflate(R.layout.custom_layout_insertdata,null);

                expenseDialog.setContentView(myView);
                expenseDialog.show();
                expenseDialog.setCancelable(false);

                EditText etExpenseAmount=myView.findViewById(R.id.etAmount);
                EditText etExpenseType=myView.findViewById(R.id.etType);
                EditText etExpenseNote=myView.findViewById(R.id.etNote);

                Button btnSave=myView.findViewById(R.id.btnSave_InsertData);
                Button btnCancel=myView.findViewById(R.id.btnCancel_InsertData);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String exAmount=etExpenseAmount.getText().toString().trim();
                        String exType=etExpenseType.getText().toString().trim();
                        String exNote=etExpenseNote.getText().toString().trim();

                        try {

                            if (TextUtils.isEmpty(exAmount)) {
                                etExpenseAmount.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(exType)) {
                                etExpenseType.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(exNote)) {
                                etExpenseNote.setError("Required Field");
                            }

                            if (TextUtils.isEmpty(exAmount) && TextUtils.isEmpty(exType) && TextUtils.isEmpty(exNote)) {
                                throw new IllegalArgumentException("Invalid Data");
                            }


                        int ourAmount=Integer.parseInt(exAmount);

                        String id=expenseDatabase.push().getKey();
                        String date= DateFormat.getDateInstance().format(new Date());

                        Data data=new Data(ourAmount,exType,exNote,id,date);

                        assert id != null;
                        expenseDatabase.child(id).setValue(data);

                        Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                        expenseDialog.dismiss();
                        ftAnimation();
                    }catch (IllegalArgumentException e){
                        Toast.makeText(getActivity(), "Invalid Data", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }}
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ftAnimation();
                        expenseDialog.dismiss();
                    }
                });
            }
        });
    }

    private void ftAnimation()
    {
        if(isOpen)
        {
            fbtnIncome.startAnimation(fade_close);
            fbtnIncome.setVisibility(View.INVISIBLE);
            fbtnExpense.startAnimation(fade_close);
            fbtnExpense.setVisibility(View.INVISIBLE);

            fbtnIncome.setClickable(false);
            fbtnExpense.setClickable(false);

            ft_text_Expense.startAnimation(fade_close);
            ft_text_Expense.setVisibility(View.INVISIBLE);
            ft_text_Income.startAnimation(fade_close);
            ft_text_Income.setVisibility(View.INVISIBLE);

            ft_text_Expense.setClickable(false);
            ft_text_Income.setClickable(false);

            isOpen=false;
        }
        else
        {
            fbtnIncome.startAnimation(fade_open);
            fbtnIncome.setVisibility(View.VISIBLE);
            fbtnExpense.startAnimation(fade_open);
            fbtnExpense.setVisibility(View.VISIBLE);

            fbtnIncome.setClickable(true);
            fbtnExpense.setClickable(true);

            ft_text_Income.startAnimation(fade_open);
            ft_text_Income.setVisibility(View.VISIBLE);
            ft_text_Expense.startAnimation(fade_open);
            ft_text_Expense.setVisibility(View.VISIBLE);

            ft_text_Income.setClickable(true);
            ft_text_Expense.setClickable(true);

            isOpen=true;
        }
    }
}