package com.example.expensemng.IFireBase;

import com.example.expensemng.Models.Expense;

import java.util.List;

public interface ListExpenseCallBack {
    void onSuccess(List<Expense> expenseList);
    void onFailed(Exception e);
}
