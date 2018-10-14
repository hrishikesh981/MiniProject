package com.example.vishn.miniproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MedicineDetails extends AppCompatActivity {
    TextView medicine;
    EditText stock,cost;
    FloatingActionButton add_stock,sub_stock,add_cost,sub_cost;
    ExpandableListView details;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private double cp;
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
        details=findViewById(R.id.medicine_details_list);
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
        expandableListView=findViewById(R.id.medicine_details_list);
        expandableListDetail = ExpandableListDataPump.getData(getIntent().getStringExtra("medicine_name"));
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    public void updateMedicineDetails(View view) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
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
                                                financing(db);

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

    private void financing(FirebaseFirestore db) {
        int initialStock=Integer.parseInt(getIntent().getStringExtra("medicine_stock"));
        int finalStock=Integer.parseInt(stock.getText().toString());
        if(initialStock==finalStock){
            Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
            startActivity(intent);
        }
        else if(initialStock<finalStock){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Restock Financing");
            final EditText input = new EditText(this);
            input.setHint("Enter cost price");
            input.setHintTextColor(Color.GRAY);
            input.setTextColor(Color.BLACK);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cp = Double.parseDouble(input.getText().toString());
                    db.collection("financing")
                            .add(new FinancingDTO((initialStock-finalStock)*cp, Calendar.getInstance().getTime(),getIntent().getStringExtra("medicine_name"),FirebaseAuth.getInstance().getUid()))
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Double spent=(finalStock-initialStock)*cp;
                                    Toast.makeText(MedicineDetails.this,spent+" spent",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
                                    startActivity(intent);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MedicineDetails.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }else if(initialStock>finalStock){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Restock Financing");
            final TextView moneyEarnt=new TextView(this);
            moneyEarnt.setTextColor(Color.BLACK);
            final double sale=(initialStock-finalStock)*(Double.parseDouble(cost.getText().toString()));
            moneyEarnt.setText("Sale : "+sale);
            builder.setView(moneyEarnt);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseFirestore db2=FirebaseFirestore.getInstance();
                    db2.collection("financing")
                            .add(new FinancingDTO(sale, Calendar.getInstance().getTime(),getIntent().getStringExtra("medicine_name"),FirebaseAuth.getInstance().getUid()))
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(MedicineDetails.this,sale+" earnt",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
                                    startActivity(intent);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MedicineDetails.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MedicineDetails.this,HomePageActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }
}
