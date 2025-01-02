package com.example.expensemng.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expensemng.Activity.Expense.CreateExpense;
import com.example.expensemng.Activity.Expense.EditExpense;
import com.example.expensemng.Adapter.ExpenseAdapter;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.ListExpenseCallBack;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton btnAddExpense;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private FirebaseFirestore db;
    private FirebaseUser user;


    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        btnAddExpense = view.findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateExpense.class);
                startActivity(intent);

            }
        });

        recyclerView = view.findViewById(R.id.expenseRecyclerView);

        expenseAdapter = new ExpenseAdapter(getActivity());
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        expenseAdapter = new ExpenseAdapter(new ExpenseAdapter.IClickItem() {
            @Override
            public void onClickItem(Expense expense) {
                Intent intent = new Intent(getActivity(), EditExpense.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("expense",  expense);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, getActivity());
        fetchExpenses();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchExpenses();

    }

    private void fetchExpenses() {
        IgetInfor igetInfor = new firebaseService(db);

        // Check if user is logged in
        if (user != null) {
            igetInfor.getExpense(user.getUid(), new ListExpenseCallBack() {
                @Override
                public void onSuccess(List<Expense> expenseList) {
                    // Update the adapter with the new list of expenses
                    expenseAdapter.setListExpense(expenseList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(expenseAdapter);

                }

                @Override
                public void onFailed(Exception e) {
                    // Handle failure (e.g., show error message)
                }
            });
        }
    }
}