package com.example.vishn.miniproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayPharmacies extends AppCompatActivity {
//    private String[] pharmaids;
//    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private PharmNoteAdapter adapter;
    private RecyclerView recyclerView;
    FirebaseFirestore db1=FirebaseFirestore.getInstance();
    CollectionReference pharmRef=db1.collection("pharmacies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_pharmacies);
        recyclerView=findViewById(R.id.recycler_view_pharm);
//        Bundle extras=getIntent().getExtras();
//        if(extras==null){
//            return;
//
//        }

//        String medname=extras.getString("medname");





//        db.collection("medicines")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            ArrayList<String> mn= new ArrayList<>();
//                            for(QueryDocumentSnapshot document : task.getResult()){
//                                if(document.get("name")==medname)
//                                mn.add(document.get("pharmacy")+"");
//                                else
//                                    continue;
//                            }
//                            pharmaids=new String[mn.size()];
//                            pharmaids=mn.toArray(pharmaids);
//
//                        }
//                    }
//                });

        setUpRecyclerview();






    }

    private void setUpRecyclerview()
    {
       // Query query=pharmRef.orderBy("name").whereArrayContains(FieldPath.documentId(),pharmaids);
        Query query=pharmRef.orderBy("name");
        FirestoreRecyclerOptions<PharmNote> options=new FirestoreRecyclerOptions.Builder<PharmNote>().setQuery(query,PharmNote.class).build();
        adapter= new PharmNoteAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
