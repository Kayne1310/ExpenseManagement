package com.example.expensemng.IFireBase;

import com.example.expensemng.Models.Budget;

import java.util.List;

public interface ListBudgetCallBack {
    void    onSuccess(List<Budget> budgetList);
    void onFailed(Exception e);
}










