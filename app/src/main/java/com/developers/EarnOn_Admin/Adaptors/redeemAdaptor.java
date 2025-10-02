package com.developers.EarnOn_Admin.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.EarnOn_Admin.Models.redeemModel;
import com.developers.EarnOn_Admin.Models.redeemModel;
import com.developers.EarnOn_Admin.R;
import com.developers.EarnOn_Admin.redeemDetails;

import java.util.ArrayList;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;

public class redeemAdaptor extends RecyclerView.Adapter<redeemAdaptor.ViewHolder> implements Filterable {
    ArrayList<redeemModel> redeemList;
    Context context;
    private ArrayList<redeemModel> redeemListFull;

    public redeemAdaptor(ArrayList<redeemModel> redeemList, Context context) {
        this.redeemList = redeemList;
        this.context = context;
        this.redeemListFull = new ArrayList<>(redeemList);
    }

    @NonNull
    @Override
    public redeemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.redeem_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull redeemAdaptor.ViewHolder holder, int position) {

        redeemModel item = redeemList.get(position);
        holder.amount.setText(item.getAmount());
        holder.date.setText(item.getDate());
        holder.number.setText(item.getNumber());
        holder.msg.setText(item.getMsg());
        holder.status.setText(item.getStatus());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, redeemDetails.class);
                intent.putExtra("code",item.getReg_no());
                intent.putExtra("id",item.getPush_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return redeemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, msg, date, status, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.redeemAccountNooId);
            msg = itemView.findViewById(R.id.redeemStatusMsgId);
            date = itemView.findViewById(R.id.redeemDateId);
            status = itemView.findViewById(R.id.redeemStatusId);
            amount = itemView.findViewById(R.id.redeemAmountId);
        }
    }



    @Override
    public Filter getFilter() {
        return leadFilter;
    }

    private Filter leadFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<redeemModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(redeemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (redeemModel item : redeemListFull) {
                    if (item.getNumber().toLowerCase().contains(filterPattern) ||
                            item.getReg_no().toLowerCase().contains(filterPattern) || item.getStatus().toLowerCase().contains(filterPattern)
                    ) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            redeemList.clear();
            redeemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateData(ArrayList<redeemModel> newData) {
        redeemListFull.clear();
        redeemListFull.addAll(newData);
        notifyDataSetChanged();
    }
    

}
