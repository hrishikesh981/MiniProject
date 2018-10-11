package com.example.vishn.miniproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference medRef=db.collection("medicines");
    private NoteAdapter adapter;
    private StorageReference storageRef;
    private PharmNote pharmacy;
    private Medicine allMeds;
    private RecyclerView recyclerView;
    private String m_Text = "";
    private String[] mednames={"medicine1","medicine2","medicine3","medicine4","medicine5"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DATABASE");
        setContentView(R.layout.activity_home_page);
        recyclerView=findViewById(R.id.recycler_view);
        setUpRecyclerview();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View header=navigationView.getHeaderView(0);
        final ImageView dp=header.findViewById(R.id.display_pic);
        final TextView pharma_name=header.findViewById(R.id.header_name);
        final long THREE_MB=3*1024*1024;
        storageRef=FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/dp.jpg");
        storageRef.getBytes(THREE_MB)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        dp.setImageBitmap(Bitmap.createScaledBitmap(bmp, dp.getWidth(),
                                dp.getHeight(), false));
//                        Toast.makeText(HomePageActivity.this,"DP loaded",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(HomePageActivity.this,"Couldnt load dp",Toast.LENGTH_SHORT).show();
                    }
                });
        pharmacy=new PharmNote();
        db.collection("pharmacies").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null){
                            pharmacy=documentSnapshot.toObject(PharmNote.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomePageActivity.this,"Pharmacy details not present",Toast.LENGTH_LONG).show();
                    }
                });

        pharma_name.setText(pharmacy.getPharm_name());
        TextView tset1=(TextView)header.findViewById(R.id.header_email);
        tset1.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
 //     welcome();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void setUpRecyclerview()
    {
        Query query=medRef.orderBy("name").whereEqualTo("pharmacy",FirebaseAuth.getInstance().getUid());
        FirestoreRecyclerOptions<Medicine>options=new FirestoreRecyclerOptions.Builder<Medicine>().setQuery(query,Medicine.class).build();
        adapter= new NoteAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
    }
    private void setUpRecyclerview(String name)
    {
        Query query=medRef.orderBy("name").whereEqualTo("pharmacy",FirebaseAuth.getInstance().getUid()).whereEqualTo("name",name);
        FirestoreRecyclerOptions<Medicine>options=new FirestoreRecyclerOptions.Builder<Medicine>().setQuery(query,Medicine.class).build();
        adapter= new NoteAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
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

    void welcome()
    {
        FirebaseAuth fauth=FirebaseAuth.getInstance();
        FirebaseUser curr=fauth.getCurrentUser();
        TextView tset1=findViewById(R.id.header_email);
        tset1.setText(curr.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment=null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_pharma_details) {
            Intent intent=new Intent(this,EditPharmaDetails.class);
            intent.putExtra("previousDetails",pharmacy);
            startActivity(intent);


        } else if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addMedicine(View view) {
        Intent intent=new Intent(this,AddDrugDetails.class);
        intent.putExtra("mednames",mednames);
        startActivity(intent);
    }

    public void searchMedicine(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");

// Set up the input
        final AutoCompleteTextView input = new AutoCompleteTextView(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mednames);
        input.setThreshold(1);
        input.setAdapter(arrayAdapter);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                setUpRecyclerview(m_Text);
                FloatingActionButton search= findViewById(R.id.search_data);
                FloatingActionButton add= findViewById(R.id.add_data);

                onStop();
                onStart();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        while (m_Text!=""){
        }

    }
}
