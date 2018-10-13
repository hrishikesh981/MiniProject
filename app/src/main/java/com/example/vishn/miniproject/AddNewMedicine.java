package com.example.vishn.miniproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNewMedicine extends AppCompatActivity {
    EditText name,weight,strength,dosage,after_effects;
    String[] mednames;
    TextView textViewConfirmation;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medicine);
        mednames=getIntent().getStringArrayExtra("mednames");
        name=findViewById(R.id.checkIfPresent_text);
        textViewConfirmation=findViewById(R.id.textViewConfirmation);
        weight=findViewById(R.id.add_details_weight);
        strength=findViewById(R.id.add_details_strength);
        dosage=findViewById(R.id.add_details_dosage);
        after_effects=findViewById(R.id.add_details_afterEffects);
        weight.setEnabled(false);
        strength.setEnabled(false);
        after_effects.setEnabled(false);
        dosage.setEnabled(false);
        db=FirebaseFirestore.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddNewMedicine.this,HomePageActivity.class);
        startActivity(intent);
    }

    public void checkIfMedicineAlreadyPresent(View view) {
        List<String> mednames_list= Arrays.asList(mednames);
        if(mednames_list.contains(name.getText().toString())){
            Toast.makeText(this,"Already Present",Toast.LENGTH_LONG).show();
            textViewConfirmation.setText("Already Present");
            textViewConfirmation.setTextColor(Color.RED);
            weight.setEnabled(false);
            strength.setEnabled(false);
            after_effects.setEnabled(false);
            dosage.setEnabled(false);
        }
        else {
            textViewConfirmation.setText("Not Present. Enter details");
            textViewConfirmation.setTextColor(Color.GREEN);
            weight.setEnabled(true);
            strength.setEnabled(true);
            after_effects.setEnabled(true);
            dosage.setEnabled(true);

        }
    }

    public void addMedicine_master(View view) {
        CollectionReference cref=db.collection("medicine_master");
        cref.add(new MedicineMaster(name.getText().toString(),after_effects.getText().toString(),Integer.parseInt(dosage.getText().toString()),Double.parseDouble(strength.getText().toString()),Double.parseDouble(weight.getText().toString())))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddNewMedicine.this,"Medicine mdata added",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(AddNewMedicine.this,HomePageActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewMedicine.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
