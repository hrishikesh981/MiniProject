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

public class NoteAdapter extends FirestoreRecyclerAdapter<Medicine,NoteAdapter.NoteHolder> {


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Medicine> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Medicine model) {
        holder.medicine_name.setText(model.getName());
        holder.medicine_cost.setText("Rs"+model.getCost());
        holder.medicine_qty.setText(model.getStock()+"");

    }

    @Override
    public void onViewRecycled(@NonNull NoteHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharm_details,parent,false);
        return new NoteHolder(v);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView medicine_name;
        TextView medicine_cost;
        TextView medicine_qty;
        public NoteHolder(View itemView) {
            super(itemView);
            medicine_name=itemView.findViewById(R.id.medicine_name);
            medicine_cost=itemView.findViewById(R.id.medicine_cost);
            medicine_qty=itemView.findViewById(R.id.medicine_qty);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(),medicine_name.getText(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(itemView.getContext(),MedicineDetails.class);
                    String[] ss_cost=medicine_cost.getText().toString().split("s");
                    intent.putExtra("medicine_name",medicine_name.getText().toString());
                    intent.putExtra("medicine_cost",ss_cost[1]);
                    intent.putExtra("medicine_stock",medicine_qty.getText().toString());
                    itemView.getContext().startActivity(intent);

                }
            });

        }

    }


}
