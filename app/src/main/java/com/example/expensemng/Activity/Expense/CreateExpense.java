package com.example.expensemng.Activity.Expense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import androidx.fragment.app.DialogFragment;

import com.example.expensemng.IFireBase.ExpenseCallBack;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class CreateExpense extends AppCompatActivity {
    LinearLayout back;
    EditText txtDate, note, amount;
    Button btnCreate;
    FirebaseFirestore db;
    FirebaseUser user;
    Spinner group;
    //class DatePickerFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_expense);
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
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);

                int month = calendar.get(Calendar.MONTH);

                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("DatePicker", "Year: " + year + ", Month: " + month + ", Day: " + dayOfMonth);
                        txtDate.setText(String.format("%d/%d/%d", dayOfMonth, month+1, year));
                    }
                }, year, month, day);
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

                    Expense expense = new Expense(amountint, txtDate.getText().toString(), noteString, groupString);

                    IgetInfor igetInfor = new firebaseService(db);
                    igetInfor.addExpense(user.getUid(), expense, new ExpenseCallBack() {
                        @Override
                        public void onSuccess(Expense expense) {
                            Toast.makeText(CreateExpense.this, "Add Expense Successful", Toast.LENGTH_SHORT).show();
                            ClearExpense(amount,group,note,txtDate);

                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(CreateExpense.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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