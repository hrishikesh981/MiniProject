package com.example.vishn.miniproject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    private static HashMap<String, List<String>> expandableListDetail = new HashMap<>();
    public static HashMap<String, List<String>> getData(String medicine_name) {



        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("medicine_master").whereEqualTo("name",medicine_name).get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<String> ae=new ArrayList<>();
                    List<String> d=new ArrayList<>();
                    List<String> w=new ArrayList<>();
                    List<String> s=new ArrayList<>();
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("----", document.getId() + " => " + document.get("name"));
                        ae.add(document.get("after_effect")+"");
                        d.add(document.get("dosage")+"");
                        w.add(document.get("weight_mg")+"");
                        s.add(document.get("strength")+"");
                        expandableListDetail.put("After Effects",ae);
                        expandableListDetail.put("Dosage",d);
                        expandableListDetail.put("Weight",w);
                        expandableListDetail.put("Strength",s);
                    }
                }
            }
        });

//        List<String> cricket = new ArrayList<String>();
//        cricket.add("India");
//
//        List<String> football = new ArrayList<String>();
//        football.add("Brazil");
//
//        List<String> basketball = new ArrayList<String>();
//        basketball.add("United States");
//
//        expandableListDetail.put("CRICKET TEAMS", cricket);
//        expandableListDetail.put("FOOTBALL TEAMS", football);
//        expandableListDetail.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail;
    }
}
