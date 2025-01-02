package com.example.expensemng.Service;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.expensemng.IFireBase.BudgetCallBack;
import com.example.expensemng.IFireBase.ExpenseCallBack;
import com.example.expensemng.IFireBase.IgetInfor;
import com.example.expensemng.IFireBase.ListBudgetCallBack;
import com.example.expensemng.IFireBase.ListExpenseCallBack;
import com.example.expensemng.IFireBase.OnUserCallBack;
import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;
import com.example.expensemng.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class firebaseService implements IgetInfor {
    private FirebaseFirestore firestore;

    public firebaseService(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }


    @Override
    public void getInforUser(String userId, OnUserCallBack callBack) {
        firestore.collection("User").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("userName");
                    String email = documentSnapshot.getString("email");
                    int age = Math.toIntExact(documentSnapshot.getLong("age"));
                    User user = new User(name, email, age);
                    callBack.onSuccess(user);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e);
            }
        });

    }

    @Override
    public void addExpense(String userId, Expense expense, ExpenseCallBack callBack) {
        firestore.collection("User").document(userId).collection("Expense").add(expense).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String ExpenseId = documentReference.getId();
                expense.setId(ExpenseId);
                documentReference.set(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onSuccess(expense);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailed(e);
                    }
                });
            }
        });

    }

    @Override
    public void getExpense(String userId, ListExpenseCallBack callBack) {
        firestore.collection("User").document(userId).collection("Expense").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Expense> expenseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Expense newExpense = document.toObject(Expense.class);
                        expenseList.add(newExpense);
                    }
                    callBack.onSuccess(expenseList);
                }
            }
        });

    }

    @Override
    public void getBudget(String userId, ListBudgetCallBack callBack) {
        firestore.collection("User").document(userId).collection("Budget").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Budget> budgetList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Budget budget = documentSnapshot.toObject(Budget.class);
                        budgetList.add(budget);
                    }
                    callBack.onSuccess(budgetList);
                }
            }
        });
    }

    @Override
    public void updateExpense(String user, String expenseId, Expense expense, ExpenseCallBack callBack) {
        firestore.collection("User").document(user).collection("Expense").document(expenseId).set(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callBack.onSuccess(expense);
                }
                else {
                    callBack.onFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void updateBudget(String userId, String budgetId, Budget budget, BudgetCallBack callBack) {

        firestore.collection("User").document(userId).collection("Budget").document(budgetId).set(budget).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        callBack.onSucess(budget);
                    }
                    else{
                        callBack.onFailed(task.getException());
                    }
            }
        });

    }




    @Override
    public void addBudget(String userId, Budget budget, BudgetCallBack callBack) {
        firestore.collection("User").document(userId).collection("Budget").add(budget).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String budgetId=documentReference.getId();
                budget.setId(budgetId);
                documentReference.set(budget).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     callBack.onSucess(budget);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailed(e);
            }
        });


    }







}
