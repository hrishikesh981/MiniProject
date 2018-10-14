package com.example.vishn.miniproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinancingRecyclerAdapter extends FirestoreRecyclerAdapter<FinancingDTO,FinancingRecyclerAdapter.NoteHolderFinancing> {


    public FinancingRecyclerAdapter(@NonNull FirestoreRecyclerOptions<FinancingDTO> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolderFinancing holder, int position, @NonNull FinancingDTO model) {
        Date current=model.getCurrent();
        double amount=model.getAmount();
        holder.transaction_amount.setText("Rs."+amount);
        if(amount<0){
            holder.transaction_amount.setTextColor(Color.RED);
        }else {
            holder.transaction_amount.setTextColor(Color.GREEN);
        }
        String name=model.getMedicine();
        holder.transaction_name.setText(name);
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        holder.transaction_date.setText(formatter1.format(current));
        holder.transaction_time.setText(formatter2.format(current));
//        holder.transaction_date.setText(current.getDate());
//        holder.transaction_time.setText(current.getTime()+"");

    }

    @Override
    public void onViewRecycled(@NonNull NoteHolderFinancing holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public NoteHolderFinancing onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_details,parent,false);
        return new NoteHolderFinancing(v);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class NoteHolderFinancing extends RecyclerView.ViewHolder{

        TextView transaction_name;
        TextView transaction_amount;
        TextView transaction_time;
        TextView transaction_date;
        public NoteHolderFinancing(View itemView) {
            super(itemView);
            transaction_name=itemView.findViewById(R.id.transaction_name);
            transaction_amount=itemView.findViewById(R.id.transaction_amount);
            transaction_time=itemView.findViewById(R.id.transaction_time);
            transaction_date=itemView.findViewById(R.id.transaction_date);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(itemView.getContext(),medicine_name.getText(),Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(itemView.getContext(),MedicineDetails.class);
//                    String[] ss_cost=medicine_cost.getText().toString().split("s");
//                    intent.putExtra("medicine_name",medicine_name.getText().toString());
//                    intent.putExtra("medicine_cost",ss_cost[1]);
//                    intent.putExtra("medicine_stock",medicine_qty.getText().toString());
//                    itemView.getContext().startActivity(intent);
//
//                }
//            });

        }

    }


}
