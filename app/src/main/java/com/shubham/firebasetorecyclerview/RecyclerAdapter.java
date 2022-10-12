package com.shubham.firebasetorecyclerview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<Student,RecyclerAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Student model) {

        holder.name.setText(model.getName());
        holder.branch.setText(model.getBranch());
        holder.usn.setText(model.getUSN());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext()).load(model.getPicUrl()).into(holder.img);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.img.getContext()).setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,1500)
                        .create();
                View myView = dialogPlus.getHolderView();
                EditText imgUrl = myView.findViewById(R.id.imageEditUrl);
                EditText nameEdit = myView.findViewById(R.id.nameEdit);
                EditText branchEdit = myView.findViewById(R.id.branchEdit);
                EditText usnEdit = myView.findViewById(R.id.editUsn);
                EditText emailEdit = myView.findViewById(R.id.editEmail);
                Button updateBtn = myView.findViewById(R.id.updateBtn);
                imgUrl.setText(model.getPicUrl());
                nameEdit.setText(model.getName());
                branchEdit.setText(model.getBranch());
                usnEdit.setText(model.getUSN());
                emailEdit.setText(model.getEmail());
                dialogPlus.show();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("picUrl",imgUrl.getText().toString());
                        map.put("Branch",branchEdit.getText().toString());
                        map.put("Name",nameEdit.getText().toString());
                        map.put("Email",emailEdit.getText().toString());
                        map.put("USN",usnEdit.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("students").child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        TastyToast.makeText(holder.name.getContext(), "Data Updated Successfully",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                                        dialogPlus.dismiss();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        dialogPlus.dismiss();
                                        TastyToast.makeText(holder.name.getContext(), "Error : "+e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                                        Log.w("UpdateError",e.getMessage());

                                    }
                                });


                    }
                });

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setIcon(R.drawable.dustbin);
                builder.setTitle("Delete User?");
                builder.setMessage("Are you Sure , You want to delete this ? ");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("students").child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
                        TastyToast.makeText(holder.name.getContext(), "User Deleted Successfully..",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row,parent,false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        ImageView edit,delete;
        TextView name,usn,branch,email;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.contactPic);
            name = itemView.findViewById(R.id.ContactName);
            usn = itemView.findViewById(R.id.usn);
            branch = itemView.findViewById(R.id.branch);
            email = itemView.findViewById(R.id.email);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
