package com.developers.EarnOn_Admin.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.EarnOn_Admin.Models.notificationModel;
import com.developers.EarnOn_Admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notificationAdaptor extends RecyclerView.Adapter<notificationAdaptor.ViewHolder> {

    Context context;
    ArrayList<notificationModel> notificationLists;

    public notificationAdaptor(Context context, ArrayList<notificationModel> notificationLists) {
        this.context = context;
        this.notificationLists = notificationLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noti_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        notificationModel notify = notificationLists.get(position);
        holder.title.setText(notify.getTitle());
        holder.description.setText(notify.getDescription());
        Picasso.get().load(notify.getImage()).fit().into(holder.smallImg);
        if (notify.getLarge_Image().contains("null")){
            holder.largeImg_banner.setVisibility(View.GONE);
        }else{
            Picasso.get().load(notify.getLarge_Image()).fit().into(holder.fullImg);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNotification(notify.getId());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

    }

    private void deleteNotification(String id) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("Admin");
        mReference.child("Notification").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView smallImg;
        ImageView fullImg;
        TextView title;
        TextView description;
        LinearLayout largeImg_banner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            smallImg = itemView.findViewById(R.id.LogooId);
            fullImg = itemView.findViewById(R.id.ImageId);
            title = itemView.findViewById(R.id.titleId);
            description = itemView.findViewById(R.id.messageId);
            largeImg_banner = itemView.findViewById(R.id.BannerPanelId);
        }
    }
}

