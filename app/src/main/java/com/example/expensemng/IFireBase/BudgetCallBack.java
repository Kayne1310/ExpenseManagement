package com.example.expensemng.IFireBase;

import com.example.expensemng.Models.Budget;

public interface BudgetCallBack {

    void onSucess(Budget budget);
    void onFailed(Exception e);
}
