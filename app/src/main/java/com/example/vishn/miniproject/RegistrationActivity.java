package com.example.vishn.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mFirebaseAuth= FirebaseAuth.getInstance();
    }

    @Override

    protected void onStart() {
        super.onStart();
        FirebaseUser currFirebaseUser=mFirebaseAuth.getCurrentUser();
        //TextView tset=findViewById(R.id.textView4);
        if (currFirebaseUser != null) {
            //tset.setText(currFirebaseUser.getEmail());
            Toast.makeText(this,"already logged in",Toast.LENGTH_SHORT).show();
            gotoHome(currFirebaseUser);
        }
    }
    public void gotoLogin(View view)
    {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void registration(View view) {
        EditText euname=findViewById(R.id.editTextEmailR);
        EditText epwd=findViewById(R.id.editTextPasswordR);
        EditText epwd2=findViewById(R.id.editTextPasswordR2);
        //final TextView tset=findViewById(R.id.textView4);
        if(epwd.getText().toString().equals(epwd2.getText().toString()))
        {
            mFirebaseAuth.createUserWithEmailAndPassword(euname.getText().toString(),epwd.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("createUserWihEmail:", "Successful");
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                if (user != null) {
                                    gotoHome(user);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("createUserWithEmail:", task.getException().toString());
                                Toast.makeText(RegistrationActivity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
//                                tset.setText(task.getException().toString());
                            }
                        }
                    });
        }

    }

    private void gotoHome( FirebaseUser currFirebaseUser) {

        Intent intent=new Intent(this,EditPharmaDetails.class);
//        TextView tset1=findViewById(R.id.navbar_email);
//        tset1.setText(currFirebaseUser.getEmail());
        startActivity(intent);
    }

}
