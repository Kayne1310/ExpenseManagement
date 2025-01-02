package com.example.expensemng.IFireBase;


import com.example.expensemng.Models.Budget;
import com.example.expensemng.Models.Expense;

import java.util.List;

public interface  IgetInfor {



   void getInforUser(String userId,OnUserCallBack callBack);

   void addExpense(String userId,Expense expense, ExpenseCallBack callBack);

   void addBudget(String userId, Budget budget, BudgetCallBack callBack);
   void getExpense(String userId, ListExpenseCallBack callBack);

   void getBudget(String userId,ListBudgetCallBack callBack);
   void updateExpense(String user,String expenseId,Expense expense,ExpenseCallBack callBack);

   void updateBudget(String userId,String budgetId,Budget budget,BudgetCallBack callBack);

}
