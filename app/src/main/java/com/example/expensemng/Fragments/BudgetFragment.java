package com.example.expensemng.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.expensemng.Activity.Budget.CreateBudget;
import com.example.expensemng.Activity.Budget.EditBudget;
import com.example.expensemng.Activity.Expense.EditExpense;
import com.example.expensemng.Adapter.BudgetAdapter;
import com.example.expensemng.Adapter.ExpenseAdapter;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.ListBudgetCallBack;
import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private RecyclerView budgetRecylerView;
    private BudgetAdapter budgetAdapter;
    private FloatingActionButton btnCreate;


    public BudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        btnCreate = view.findViewById(R.id.btnAddBudget);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CreateBudget.class);
                startActivity(intent);
            }
        });
        budgetRecylerView = view.findViewById(R.id.budgetRecyclerView);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

      budgetAdapter=new BudgetAdapter(new BudgetAdapter.IClickItem() {
          @Override
          public void onClickitem(Budget budget) {
              Intent intent = new Intent(getActivity(), EditBudget.class);
              Bundle bundle = new Bundle();
              bundle.putSerializable("budget", budget);
              intent.putExtras(bundle);
              startActivity(intent);
          }
      });



        loadData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {

        IgetInfor igetInfor = new firebaseService(db);
        igetInfor.getBudget(user.getUid(), new ListBudgetCallBack() {
            @Override
            public void onSuccess(List<Budget> budgetList) {
                budgetRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                budgetAdapter.setBudgetList(budgetList);
                budgetRecylerView.setAdapter(budgetAdapter);


            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }
}