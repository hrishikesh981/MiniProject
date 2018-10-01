package com.example.vishn.miniproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.awt.font.TextAttribute;

public class NoteAdapter extends FirestoreRecyclerAdapter<PharmNote,NoteAdapter.NoteHolder> {


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<PharmNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull PharmNote model) {
        holder.textView_pharm_name.setText(model.getPharm_name());
        holder.textView_pharm_contact.setText(model.getPharm_contact());
        holder.textView_pharm_address.setText(model.getPharm_address());
        holder.textView_priority.setText(String.valueOf(model.getPriority()));

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharm_details,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

           TextView textView_pharm_name;
        TextView textView_pharm_contact;
        TextView textView_pharm_address;
        TextView textView_priority;
        public NoteHolder(View itemView) {
            super(itemView);
            textView_pharm_name=itemView.findViewById(R.id.pharmacy_name);
            textView_pharm_contact=itemView.findViewById(R.id.pharmacy_contact);
            textView_pharm_address=itemView.findViewById(R.id.pharmacy_address);
            textView_priority=itemView.findViewById(R.id.text_view_priority);

        }
    }


}
