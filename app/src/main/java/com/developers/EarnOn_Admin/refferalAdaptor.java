package com.developers.EarnOn_Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.EarnOn_Admin.Models.referalModel;
import com.developers.EarnOn_Admin.R;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;


public class refferalAdaptor extends RecyclerView.Adapter<refferalAdaptor.ViewHolder> implements Filterable {


    private OnAmountClickListener amountClickListener;

    ArrayList<referalModel> refferalList;
    Context context;

    private ArrayList<referalModel> refferalListFull; // For keeping the full data

    public refferalAdaptor(ArrayList<referalModel> refferalList, Context context, OnAmountClickListener listener) {
        this.refferalList = refferalList;
        this.context = context;
        this.amountClickListener = listener;
        refferalListFull = new ArrayList<>();
        refferalListFull.addAll(refferalList);
    }

    @NonNull
    @Override
    public refferalAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.refer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull refferalAdaptor.ViewHolder holder, int position) {

        referalModel item = refferalList.get(position);
        holder.code.setText(item.getCode());
        holder.amount.setText(item.getAmount());
        holder.orders.setText(item.getrefered_by());

    }

    @Override
    public int getItemCount() {
        return refferalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView code, orders, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.ReferalordersId);
            orders = itemView.findViewById(R.id.ReferalCodeId);
            amount = itemView.findViewById(R.id.ReferalamountId);

            amount.setOnClickListener(v -> {
                if (amountClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        amountClickListener.onAmountClick(position, refferalList.get(position).getAmount());
                    }
                }
            });

        }
    }


    @Override
    public Filter getFilter() {
        return refferalFilter;
    }

    private Filter refferalFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<referalModel> filteredList = new ArrayList<>();


            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(refferalListFull);
            } else {

                for (referalModel item : refferalListFull) {
                    if (item.getUperName().toLowerCase().contains(constraint.toString().toLowerCase()) || item.getrefered_by().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            refferalList.clear();
            refferalList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public void updateData(ArrayList<referalModel> newData) {
        refferalListFull.clear();
        refferalListFull.addAll(newData);
        notifyDataSetChanged();
    }

}

