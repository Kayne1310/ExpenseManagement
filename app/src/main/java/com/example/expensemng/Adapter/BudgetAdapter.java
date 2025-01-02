package com.example.expensemng.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemng.Models.Budget;
import com.example.expensemng.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetViewHolder> {
    private List<Budget> budgetList=new ArrayList<>();
    private Context context;
    private IClickItem iClickItem;

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_budget_item,parent,false);

        return new BudgetViewHolder(view);

    }

    public BudgetAdapter(Context context) {
        this.context = context;
    }

    public BudgetAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBudgetList(List<Budget> budgetList) {
        this.budgetList = budgetList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget= budgetList.get(position);
        if(budget==null){
            return;
        }
        holder.amount.setText(String.valueOf(budget.getAmount()));
        holder.date.setText(budget.getDate());
        holder.group.setText(budget.getGroup());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItem.onClickitem(budget);
            }
        });


    }

    @Override
    public int getItemCount() {
        return budgetList!=null ?budgetList.size() :0 ;
    }

    public interface  IClickItem{
        void onClickitem(Budget budget);
    }
}
