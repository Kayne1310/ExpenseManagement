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

import com.example.expensemng.Models.User;
import com.example.expensemng.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private TextView routeLogin;
    private FirebaseAuth auth;
    private Button btnres;
    private TextInputEditText username, email, password, age;
    private User user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Route Login
        routeLogin = findViewById(R.id.routeLogin);
        routeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        db = FirebaseFirestore.getInstance();
        username = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        auth = FirebaseAuth.getInstance();
        btnres = findViewById(R.id.btnRes);
        btnres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAuth = email.getText().toString();
                String passwordAuth = password.getText().toString();
                int ageAuth = Integer.parseInt(age.getText().toString());
                String userName = username.getText().toString();

                auth.createUserWithEmailAndPassword(emailAuth, passwordAuth).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                            FirebaseUser username=FirebaseAuth.getInstance().getCurrentUser();
                            String userId=username.getUid();
                            User user = new User(ageAuth, passwordAuth, userName, emailAuth);
                            db.collection("User").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(RegisterActivity.this, "Regester Sucessful !", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}