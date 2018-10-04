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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth= FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currFirebaseUser=mFirebaseAuth.getCurrentUser();
        //TextView tset=findViewById(R.id.textView3);
        if (currFirebaseUser != null) {
            //tset.setText(currFirebaseUser.getEmail());
            Toast.makeText(this,"already loggged in",Toast.LENGTH_SHORT).show();
            gotoHome(currFirebaseUser);
        }
    }

    public void gotoRegistration(View view)
    {
        Intent intent=new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void checkLogin(View view) {
        EditText euname= findViewById(R.id.editTextEmail);
        EditText epwd= findViewById(R.id.editTextPassword);
        //final TextView tset=findViewById(R.id.textView3);
        mFirebaseAuth.signInWithEmailAndPassword(euname.getText().toString(),epwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithEmail:", "success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            if (user != null) {
                                gotoHome(user);
                            }
                        }else{
                            // If sign in fails, display a message to the user.
                            Log.d("signInWithEmail:", task.getException().toString());
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(),
                                    Toast.LENGTH_SHORT).show();
                            //tset.setText(Objects.requireNonNull(task.getException()).toString());
                        }
                    }
                });
    }
    private void gotoHome( FirebaseUser currFirebaseUser) {

        Intent intent=new Intent(this,HomePageActivity.class);
//        TextView tset1=findViewById(R.id.navbar_email);
//        tset1.setText(currFirebaseUser.getEmail());
        startActivity(intent);
    }

    public void googleSignIn(View view) {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
