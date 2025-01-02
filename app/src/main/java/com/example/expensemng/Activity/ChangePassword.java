package com.example.expensemng.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensemng.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    LinearLayout back;
    TextInputEditText oldPassword, newPasswordm, reNewPassword;
    Button btnChangePassword;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //back ve setting
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //anh xa
        oldPassword = findViewById(R.id.oldPassword);
        newPasswordm = findViewById(R.id.newPassword);
        reNewPassword = findViewById(R.id.reNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        boolean isValid = true;
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                boolean isValid = true;
                if (oldPassword.getText().toString().isEmpty()) {
                    oldPassword.setError("Please Enter old Password");
                    isValid = false;
                }
                if (newPasswordm.getText().toString().isEmpty()) {
                    newPasswordm.setError("Please Enter New password");
                    isValid = false;
                }
                if (reNewPassword.getText().toString().isEmpty()) {
                    reNewPassword.setError("Please Enter Re New Password");
                    isValid = false;
                }

                if (reNewPassword.getText().toString().equals(newPasswordm.getText().toString())) {
                    if (isValid) {


                        auth.signInWithEmailAndPassword(user.getEmail(), oldPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPasswordm.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChangePassword.this, "Update Password Successul", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ChangePassword.this, "Please Enter Correct old Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                else{
                    Toast.makeText(ChangePassword.this, "Please Enter correct new Pass and renewpass", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}