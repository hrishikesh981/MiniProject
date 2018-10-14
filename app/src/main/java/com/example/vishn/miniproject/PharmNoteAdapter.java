package com.example.vishn.miniproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PharmNoteAdapter extends FirestoreRecyclerAdapter<PharmNote,PharmNoteAdapter.NoteHolder> {


    public PharmNoteAdapter(@NonNull FirestoreRecyclerOptions<PharmNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull PharmNote model) {
        holder.pharm_name.setText(model.getPharm_name());
        holder.pharm_address.setText(model.getPharm_address());
        holder.pharm_phone.setText(model.getPharm_phone1()+"");

    }

    @Override
    public void onViewRecycled(@NonNull NoteHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.actual_pharm_details,parent,false);
        return new NoteHolder(v);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView pharm_name;
        TextView pharm_address;
        TextView pharm_phone;
        public NoteHolder(View itemView) {
            super(itemView);
            pharm_name=itemView.findViewById(R.id.pharmacy_name);
            pharm_address=itemView.findViewById(R.id.pharmacy_address);
           pharm_phone=itemView.findViewById(R.id.pharmacy_phone);


        }

    }


}
