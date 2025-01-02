package com.example.expensemng.Activity.Expense;

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

import com.example.expensemng.IFireBase.ExpenseCallBack;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.Models.User;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditExpense extends AppCompatActivity {
    private EditText amount, note, date;
    private Spinner group;
    private LinearLayout back;
    private Button edit, delete;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        amount = findViewById(R.id.amount);
        note = findViewById(R.id.note);
        date = findViewById(R.id.date);
        group = findViewById(R.id.group);
        back = findViewById(R.id.back);
        //Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //nhan data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Expense expense = (Expense) bundle.getSerializable("expense");
            if (expense != null) {

                amount.setText(String.valueOf(expense.getAmount()));
                note.setText(expense.getNote());
                date.setText(expense.getDate());

                String expenseGroup = expense.getGroup();
                String[] expenseArray = getResources().getStringArray(R.array.group);

                if (expenseGroup != null) {
                    for (int i = 0; i < expenseArray.length; i++) {
                        if (expenseArray[i].equals(expenseGroup)) {
                            group.setSelection(i); // Đặt giá trị mặc định
                            break;
                        }
                    }
                }

                //btn Edit
                edit = findViewById(R.id.btnEditExpense);
                db=FirebaseFirestore.getInstance();
                user= FirebaseAuth.getInstance().getCurrentUser();
                edit.setOnClickListener(new View.OnClickListener() {
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
                            IgetInfor igetInfor=new firebaseService(db);
                            Expense expenseupdate=new Expense(expense.getId(),Integer.parseInt(amount.getText().toString()),group.getSelectedItem().toString(),note.getText().toString(),date.getText().toString());
                            igetInfor.updateExpense(user.getUid(), expense.getId(), expenseupdate, new ExpenseCallBack() {
                                @Override
                                public void onSuccess(Expense expense) {
                                    Toast.makeText(EditExpense.this, "Update Expense Successful", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(Exception e) {
                                        Toast.makeText(EditExpense.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

                //btn Delete
                delete=findViewById(R.id.btnDeleteExpense);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("User").document(user.getUid()).collection("Expense").document(expense.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditExpense.this, "Delete successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditExpense.this, "Add Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }

        }
    }
}