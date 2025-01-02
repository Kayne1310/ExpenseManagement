package com.example.expensemng.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    TextView routeLogin;
    Button btnFg;
    FirebaseAuth auth;
    TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Route Forgot Password to Login


        routeLogin = findViewById(R.id.routeLogin);
        routeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iten = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(iten);
            }
        });

        auth = FirebaseAuth.getInstance();
        btnFg = findViewById(R.id.btnFg);
        email = findViewById(R.id.email);
        btnFg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAuth = email.getText().toString();
                if (emailAuth.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(emailAuth).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Send email Succesful Pleas Check Email !", Toast.LENGTH_SHORT).show();
                                Intent itent=new Intent(ForgotPassword.this,LoginActivity.class);
                                startActivity(itent);
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}