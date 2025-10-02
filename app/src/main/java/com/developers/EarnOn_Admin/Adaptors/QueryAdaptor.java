package com.developers.EarnOn_Admin.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.EarnOn_Admin.Models.queryModel;
import com.developers.EarnOn_Admin.R;
import com.developers.EarnOn_Admin.queryDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QueryAdaptor extends RecyclerView.Adapter<QueryAdaptor.ViewHolder> {

    Context context;
    ArrayList<queryModel> queryList;

    public QueryAdaptor(Context context, ArrayList<queryModel> queryList) {
        this.context = context;
        this.queryList = queryList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        queryModel item = queryList.get(position);

        holder.status.setText(item.getStatus());
        holder.msg.setText(item.getMsg());
        holder.rply.setText(item.getRply());
        if (!item.getImg_url().contains("null")) {
            Picasso.get().load(item.getImg_url()).into(holder.img);
        } else {
            holder.img.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, queryDetails.class);
                intent.putExtra("reg_no", item.getReg_no());
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.help_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return queryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        TextView rply;
        TextView status;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.queryyId);
            rply = itemView.findViewById(R.id.revertId);
            status = itemView.findViewById(R.id.statusIdd);
            img = itemView.findViewById(R.id.screenShotIdd);
        }
    }
}
