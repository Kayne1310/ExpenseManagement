package com.example.expensemng.Adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemng.R;

public class ExpenseViewHolder extends RecyclerView.ViewHolder{
     TextView group,date,amount;
     LinearLayout card;
    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);

            group=itemView.findViewById(R.id.groupList);
            date=itemView.findViewById(R.id.dateList);
            amount=itemView.findViewById(R.id.amountList);
            card=itemView.findViewById(R.id.card);
    }
}
