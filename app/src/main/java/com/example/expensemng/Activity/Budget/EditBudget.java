package com.example.expensemng.Activity.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensemng.Activity.Expense.EditExpense;
import com.example.expensemng.Adapter.BudgetAdapter;
import com.example.expensemng.IFireBase.BudgetCallBack;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditBudget extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private EditText amount, note, date;
    private Spinner group;
    private Button btnEdit, btnDelete;
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        amount = findViewById(R.id.amount);
        note = findViewById(R.id.note);
        date = findViewById(R.id.date);
        group = findViewById(R.id.group);
        btnEdit = findViewById(R.id.btnEditBudget);
        btnDelete = findViewById(R.id.btnDeleteBudget);
        back=findViewById(R.id.back);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Budget budget = (Budget) bundle.getSerializable("budget");
            if (budget != null) {

                String[] groupArray = getResources().getStringArray(R.array.group);
                for (int i = 0; i < groupArray.length; i++) {
                    if (groupArray[i].equals(budget.getGroup())) {
                        group.setSelection(i);
                    }
                }

                amount.setText(String.valueOf(budget.getAmount()));
                note.setText(budget.getNote());
                date.setText(budget.getDate());


                //Button Edit budget
                btnEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        boolean isValid = true;
                        if (amount.getText().toString().isEmpty()) {
                            amount.setError("Please Enter amount");
                            isValid = false;
                        }
                        if (note.getText().toString().isEmpty()) {
                            note.setError("Please Enter Note");
                            isValid = false;
                        }
                        if (date.getText().toString().isEmpty()) {
                            date.setError("Please Enter date");
                            isValid = false;
                        }
                        if (isValid) {

                            Budget newBudget=new Budget(budget.getId(),date.getText().toString(),note.getText().toString(),group.getSelectedItem().toString(),Integer.parseInt(amount.getText().toString()));

                            IgetInfor igetInfor = new firebaseService(db);
                            igetInfor.updateBudget(user.getUid(), budget.getId(), newBudget, new BudgetCallBack() {
                                @Override
                                public void onSucess(Budget budget) {
                                    Toast.makeText(EditBudget.this, "Update Budget Successul", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    Toast.makeText(EditBudget.this, "Update Failed Successul"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });


                //btn Delete

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("User").document(user.getUid()).collection("Budget").document(budget.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditBudget.this, "Delete successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditBudget.this, "Add Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }

        }


        //btn Back

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}