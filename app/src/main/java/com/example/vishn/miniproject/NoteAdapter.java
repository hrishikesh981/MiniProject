package com.example.vishn.miniproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Medicine,NoteAdapter.NoteHolder> {


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Medicine> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Medicine model) {
        holder.medicine_name.setText(model.getName());
        holder.medicine_cost.setText(model.getCost()+"");
        holder.medicine_qty.setText(model.getStock()+"");


    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharm_details,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

           TextView medicine_name;
        TextView medicine_cost;
        TextView medicine_qty;
        TextView textView_priority;
        public NoteHolder(View itemView) {
            super(itemView);
            medicine_name=itemView.findViewById(R.id.medicine_name);
            medicine_cost=itemView.findViewById(R.id.medicine_cost);
            medicine_qty=itemView.findViewById(R.id.medicine_qty);
            textView_priority=itemView.findViewById(R.id.text_view_priority);

        }
    }


}
