package com.example.vishn.miniproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class EditPharmaDetails extends Activity {
    private static final int GET_FROM_GALLERY = 2, PLACE_PICKER_REQUEST = 1;
    EditText pharma_name, pharma_phone1, pharma_phone2, pharma_addr;
    String pharma_addr_coords, dp_url;
    ImageView display_picture;
    Uri selectImage;
    Bitmap bitmap;
    PharmNote pharmacy;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharma_details);
        pharmacy= (PharmNote) getIntent().getSerializableExtra("previousDetails");
        pharma_name = findViewById(R.id.pharmacy_name_edit);pharma_name.setText(pharmacy.getPharm_name());
        pharma_phone1 = findViewById(R.id.pharmacy_phone1_edit);pharma_phone1.setText(pharmacy.getPharm_phone1());
        pharma_phone2 = findViewById(R.id.pharmacy_phone2_edit);pharma_phone2.setText(pharmacy.getPharm_phone2());
        pharma_addr = findViewById(R.id.pharmacy_address_edit);
        String[] latlong=pharmacy.getPharm_address().split(",");
        double lat=Double.parseDouble(latlong[0]);
        double lng=Double.parseDouble(latlong[1]);
        Geocoder geocoder;
        List<Address> address=null;
        geocoder=new Geocoder(this, Locale.getDefault());
        try {
            address=geocoder.getFromLocation(lat,lng,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pharma_addr.setText(address.get(0).getAddressLine(0));

        display_picture = findViewById(R.id.display_picture);


    }

    public void addDataFun(View view) {

        storageRef = FirebaseStorage.getInstance().getReference();

        Uri file = selectImage;
        if (file != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference dp_upload_ref = storageRef.child("" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/dp.jpg");
            dp_upload_ref.putFile(selectImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    })
            ;

        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show();
        }

//        FirebaseFirestore db=FirebaseFirestore.getInstance();
//        CollectionReference pharmRef=FirebaseFirestore.getInstance().collection("pharmacies");
//        pharmRef.add(new PharmNote(pharma_name.getText(),pharma_phone1.getText(),pharma_addr.getText(),pharma_phone2));
//        Map<String,String> pharmacy=new HashMap<>();
//        pharmacy.put("pharma_name",pharma_name.getText().toString());
//        pharmacy.put("pharma_phone1",pharma_phone1.getText().toString());
//       // pharmacy.put("pharma_phone2",pharma_phone2.getText().toString());
//        pharmacy.put("pharma_addr",pharma_addr_coords);
//       // pharmacy.put("pharma_dp_url",dp_url);

        String pname, paddress, phone1,phone2, dp_url;
        pname = pharma_name.getText().toString();
        paddress = pharma_addr_coords;
        phone1 = pharma_phone1.getText().toString();
        dp_url = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/dp.jpg";
        phone2 = pharma_phone2.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference pharmRef = FirebaseFirestore.getInstance().collection("pharmacies").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        pharmRef.set(new PharmNote(pname,phone1,phone2,paddress,dp_url)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditPharmaDetails.this,"Details Updated",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditPharmaDetails.this, HomePageActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditPharmaDetails.this,"Failed to update Details",Toast.LENGTH_LONG).show();
            }
        });


//        db.collection("pharmacies").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
//                .set(pharmacy)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(EditPharmaDetails.this,"success",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(EditPharmaDetails.this,"fail",Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void getLocationFun(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                pharma_addr.setText(place.getAddress());
                pharma_addr_coords = place.getLatLng().toString();
            }
        }
        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            selectImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectImage);
                ImageView dp = findViewById(R.id.display_picture);
                dp.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void selectImage(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }
}
