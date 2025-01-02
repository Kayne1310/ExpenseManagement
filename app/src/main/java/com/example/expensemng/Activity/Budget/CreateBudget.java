package com.example.expensemng.Activity.Budget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensemng.Activity.Expense.CreateExpense;
import com.example.expensemng.IFireBase.BudgetCallBack;
import com.example.expensemng.IFireBase.ExpenseCallBack;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class CreateBudget extends AppCompatActivity {

    LinearLayout back;
    EditText txtDate, note, amount;
    Button btnCreate;
    FirebaseFirestore db;
    FirebaseUser user;
    Spinner group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //onclick date
        txtDate = findViewById(R.id.date);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);

                int month = calendar.get(Calendar.MONTH);

                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateBudget.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDate.setText(String.format("%d/%d/%d", dayOfMonth, month, year));
                    }
                }, year, month + 1, day);
                datePickerDialog.show();
            }
        });

        group = findViewById(R.id.group);
        note = findViewById(R.id.note);
        amount = findViewById(R.id.amount);
        btnCreate = findViewById(R.id.btnCreate);
        db = FirebaseFirestore.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;
                int amountint = 0;

                String groupString = group.getSelectedItem().toString();
                String noteString = note.getText().toString();
                String amountString = amount.getText().toString();
                if (amountString.isEmpty()) {
                    amount.setError("Please write amount");
                    isValid = false;
                }
                else {
                    amountint = Integer.parseInt(amountString);
                }
                if (amountint <= 0) {
                    amount.setError("Please enter amount better than 0");
                    isValid = false;
                }

                if (noteString.isEmpty()) {
                    note.setError("Please write note");
                    isValid = false;
                }

                if (txtDate.toString().isEmpty()) {
                    isValid = false;
                    txtDate.setError("Please Enter date");
                }

                if (isValid == true) {

                    Budget budget = new Budget(amountint, txtDate.getText().toString(), noteString, groupString);

                    IgetInfor igetInfor = new firebaseService(db);
                    igetInfor.addBudget(user.getUid(), budget, new BudgetCallBack() {
                        @Override
                        public void onSucess(Budget expense) {
                            Toast.makeText(CreateBudget.this, "Add Budget Successful", Toast.LENGTH_SHORT).show();
                            ClearExpense(amount,group,note,txtDate);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(CreateBudget.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        });

    }

    public static void ClearExpense(EditText amount,Spinner group,EditText note,EditText date ){

        amount.setText("");
        group.setSelection(0);
        note.setText("");
        date.setText("");

    }
}