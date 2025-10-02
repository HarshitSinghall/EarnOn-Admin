package com.developers.EarnOn_Admin.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.developers.EarnOn_Admin.R;
import com.developers.EarnOn_Admin.leadDetail;
import com.developers.EarnOn_Admin.Models.leadModel;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

public class leadAdaptor extends RecyclerView.Adapter<leadAdaptor.ViewHolder> implements Filterable {

    Context context;
    ArrayList<leadModel> leadList;
    private ArrayList<leadModel> leadListFull;

    public leadAdaptor(Context context, ArrayList<leadModel> leadList) {
        this.context = context;
        this.leadList = leadList;
        this.leadListFull = new ArrayList<>(leadList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lead_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        leadModel item = leadList.get(position);

        holder.pName.setText(item.getNumber());
        holder.uName.setText(item.getName());
        holder.status.setText(item.getStatus());
        holder.code.setText(item.getReg_no());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, leadDetail.class);
                intent.putExtra("number", item.getNumber());
                intent.putExtra("code", item.getReg_no());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pName, uName, status, code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.ProductNameeId);
            uName = itemView.findViewById(R.id.NameeId);
            status = itemView.findViewById(R.id.leadstatus);
            code = itemView.findViewById(R.id.CodeId);
        }
    }


    @Override
    public Filter getFilter() {
        return leadFilter;
    }

    private Filter leadFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<leadModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(leadListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d("harshit", "performFiltering: "+leadListFull.size());
                for (leadModel item : leadListFull) {
                    if (item != null){
                        Log.d("harshit", "performFiltering: 1--"+item.getReg_no());
                        Log.d("harshit", "performFiltering: 2--"+item.getName());
                        if (item.getName().toLowerCase().contains(filterPattern) ||
                                item.getReg_no().toLowerCase().contains(filterPattern) || item.getNumber().toLowerCase().contains(filterPattern) ||
                        item.getStatus().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            leadList.clear();
            if(results.values != null){
            leadList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    };

    public void updateData(ArrayList<leadModel> newData) {
        leadListFull.clear();
        leadListFull.addAll(newData);
        notifyDataSetChanged();
    }



}
