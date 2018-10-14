package com.example.vishn.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FinancingList extends AppCompatActivity {
    FinancingRecyclerAdapter FinanceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financing_list);
        RecyclerView finance_recycler = findViewById(R.id.recycler_view_financing);
        CollectionReference transRef= FirebaseFirestore.getInstance().collection("financing");
        Query query=transRef.orderBy("current",Query.Direction.DESCENDING).whereEqualTo("pharmacy", FirebaseAuth.getInstance().getUid());
        FirestoreRecyclerOptions<FinancingDTO> FinancingRecyclerOptions=new FirestoreRecyclerOptions.Builder<FinancingDTO>().setQuery(query,FinancingDTO.class).build();
        FinanceAdapter=new FinancingRecyclerAdapter(FinancingRecyclerOptions);
        finance_recycler.setHasFixedSize(true);
        finance_recycler.setLayoutManager(new LinearLayoutManager(this));
        finance_recycler.setAdapter(FinanceAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FinanceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (FinanceAdapter != null) {
            FinanceAdapter.stopListening();
        }
    }
}
