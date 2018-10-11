package com.example.vishn.miniproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MedicineDetails extends AppCompatActivity {
    TextView medicine;
    EditText stock,cost;
    FloatingActionButton add_stock,sub_stock,add_cost,sub_cost;
    FirebaseFirestore db;
    String medicine_pharma_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        medicine=findViewById(R.id.medicine_details_heading);
        stock=findViewById(R.id.stock_details);
        cost=findViewById(R.id.cost_details);
        add_stock=findViewById(R.id.add_stock_details);
        sub_stock=findViewById(R.id.sub_stock_details);
        add_cost=findViewById(R.id.add_cost_details);
        sub_cost=findViewById(R.id.sub_cost_details);
        medicine.setText(getIntent().getStringExtra("medicine_name"));
        cost.setText(getIntent().getStringExtra("medicine_cost"));
        stock.setText(getIntent().getStringExtra("medicine_stock"));
        add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedNum=Integer.parseInt(stock.getText().toString());
                updatedNum+=1;
                stock.setText(""+updatedNum);
            }
        });
        sub_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedNum=Integer.parseInt(stock.getText().toString());
                updatedNum-=1;
                stock.setText(""+updatedNum);
            }
        });
        add_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double updatedNum=Double.parseDouble(cost.getText().toString());
                updatedNum=updatedNum+0.5;
                cost.setText(""+updatedNum);
            }
        });
        sub_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double updatedNum=Double.parseDouble(cost.getText().toString());
                updatedNum=updatedNum-0.5;
                cost.setText(""+updatedNum);
            }
        });
        db=FirebaseFirestore.getInstance();
    }

    public void updateMedicineDetails(View view) {
        db.collection("medicines")
                .whereEqualTo("name",medicine.getText().toString())
                .whereEqualTo("pharmacy",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG::", document.getId() + " => " + document.getData());
                                Toast.makeText(MedicineDetails.this, document.getId(),Toast.LENGTH_LONG).show();
                                db.collection("medicines").document(document.getId())
                                        .update("cost",Double.parseDouble(cost.getText().toString()),"stock",Integer.parseInt(stock.getText().toString()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG::", "DocumentSnapshot successfully updated!");
                                                Toast.makeText(MedicineDetails.this,"Details Updated",Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG::", "Error updating document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d("TAG::", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
