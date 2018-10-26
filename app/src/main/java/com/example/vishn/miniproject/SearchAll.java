package com.example.vishn.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchAll extends AppCompatActivity {
    String[] mednames;
    PharmNoteAdapter adapter;
    AutoCompleteTextView search_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        mednames=getIntent().getStringArrayExtra("mednames");
        search_query=findViewById(R.id.searchAll_query);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,mednames);
        search_query.setThreshold(1);
        search_query.setAdapter(arrayAdapter);
    }

    public void searchAllAndPutIntoRecyclerView(View view) {
            // Query query=pharmRef.orderBy("name").whereArrayContains(FieldPath.documentId(),pharmaids);

            Query query= FirebaseFirestore.getInstance().collection("pharm_name").orderBy("name");
            FirestoreRecyclerOptions<PharmNote> options=new FirestoreRecyclerOptions.Builder<PharmNote>().setQuery(query,PharmNote.class).build();
            adapter= new PharmNoteAdapter(options);
            adapter.startListening();
            RecyclerView recyclerView=findViewById(R.id.recycler_view_search_all);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.stopListening();
    }
}
