package com.example.expensemng.Adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemng.R;


public class BudgetViewHolder extends RecyclerView.ViewHolder {
    TextView group,date,amount;
    LinearLayout item;
    public BudgetViewHolder(@NonNull View itemView) {
        super(itemView);

        group=itemView.findViewById(R.id.groupList);
        date=itemView.findViewById(R.id.dateList);
        amount=itemView.findViewById(R.id.amountList);
        item=itemView.findViewById(R.id.budgetItem);


    }
}
