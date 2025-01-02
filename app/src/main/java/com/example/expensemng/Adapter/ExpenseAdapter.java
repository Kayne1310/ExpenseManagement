package com.example.expensemng.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemng.Models.Expense;
import com.example.expensemng.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {
    private List<Expense> listExpense;
    private Context context;
    private IClickItem iClickItem;

    public ExpenseAdapter(Context context) {
        this.context = context;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setListExpense(List<Expense> listExpense) {
        this.listExpense = listExpense;
        notifyDataSetChanged();
    }

    public ExpenseAdapter(IClickItem iClickItem, Context context) {
        this.iClickItem = iClickItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expense = listExpense.get(position);
        if (expense == null) {
            return;
        }
        holder.amount.setText(String.valueOf(expense.getAmount()) + " VND");
        holder.date.setText(expense.getDate());
        holder.group.setText(expense.getGroup());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItem.onClickItem(expense);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listExpense != null ? listExpense.size() : 0;
    }

    public interface IClickItem {
        void onClickItem(Expense expense);
    }
}



