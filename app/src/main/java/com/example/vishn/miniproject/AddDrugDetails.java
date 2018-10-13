package com.example.vishn.miniproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class AddDrugDetails extends AppCompatActivity {
    EditText stock,cost;
    AutoCompleteTextView name;
    FirebaseFirestore db;
    String[] mednames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_details);
        name=findViewById(R.id.name_of_drug);
        stock=findViewById(R.id.stock_of_drug);
        cost=findViewById(R.id.cost_of_drug);
        db=FirebaseFirestore.getInstance();
        mednames=getIntent().getStringArrayExtra("mednames");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,mednames);
        name.setThreshold(1);
        name.setAdapter(arrayAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,HomePageActivity.class);
        startActivity(intent);
    }

    public void addToBD(View view) {
        CollectionReference cref=db.collection("medicines");
        cref.add(new Medicine(name.getText().toString(), Integer.parseInt(stock.getText().toString()),Double.parseDouble(cost.getText().toString()), FirebaseAuth.getInstance().getUid()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddDrugDetails.this, "ADD HUA", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(AddDrugDetails.this,HomePageActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddDrugDetails.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
