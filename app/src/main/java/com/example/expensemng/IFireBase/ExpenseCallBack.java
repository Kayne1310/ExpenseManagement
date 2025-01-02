package com.example.expensemng.IFireBase;

import com.example.expensemng.Models.Expense;

public interface ExpenseCallBack {
    void onSuccess(Expense expense);
    void onFailed(Exception e);
}
