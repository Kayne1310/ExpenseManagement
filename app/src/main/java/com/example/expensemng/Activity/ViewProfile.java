package com.example.expensemng.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.OnUserCallBack;
import com.example.expensemng.Models.User;
import com.example.expensemng.R;
import com.example.expensemng.Service.firebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfile extends AppCompatActivity {
    private TextInputEditText username,email,age;
    private FirebaseUser user;
    private Button btnEditProfile;
    private FirebaseFirestore db;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseFirestore.getInstance();

        //back ve setting
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username=findViewById(R.id.userName);
        email=findViewById(R.id.email);
        age=findViewById(R.id.age);

        String userIdString=user.getUid();
        IgetInfor igetInfor=new firebaseService(db);
        igetInfor.getInforUser(userIdString, new OnUserCallBack() {
            @Override
            public void onSuccess(User user) {
                username.setText(user.getUserName());
                email.setText(user.getEmail());
                age.setText(String.valueOf(user.getAge()));
            }

            @Override
            public void onFailure(Exception e) {

                Toast.makeText(ViewProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //onlick btn Editprofile
        btnEditProfile=findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid=true;
                if(username.getText().toString().isEmpty()){
                    username.setError("Please Enter User Name");
                    isValid=false;

                }
                if(age.getText().toString().isEmpty()){
                    age.setError("Please Enter Age");
                    isValid=false;
                }

                if(isValid){
                    User editUser=new User(username.getText().toString(),email.getText().toString(),Integer.parseInt(age.getText().toString()));
                    String userIdString=user.getUid();
                    db.collection("User").document(userIdString).set(editUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ViewProfile.this, "Update Sucessful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }
}