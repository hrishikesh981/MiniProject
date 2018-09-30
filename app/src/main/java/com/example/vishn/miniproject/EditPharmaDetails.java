package com.example.vishn.miniproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class EditPharmaDetails extends Activity {
    private static final int GET_FROM_GALLERY = 2 ,PLACE_PICKER_REQUEST=1;
    EditText pharma_name,pharma_phone1,pharma_phone2,pharma_addr;String pharma_addr_coords,dp_url;
    Uri selectImage;
    Bitmap bitmap;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharma_details);

        pharma_name=findViewById(R.id.pharmacy_name_edit);
        pharma_phone1=findViewById(R.id.pharmacy_phone1_edit);
        pharma_phone2=findViewById(R.id.pharmacy_phone2_edit);
        pharma_addr=findViewById(R.id.pharmacy_address_edit);

        Button button=findViewById(R.id.button_confirm_changes);


    }

    public void addDataFun(View view) {
        
        storageRef = FirebaseStorage.getInstance().getReference();

        Uri file = selectImage;
        StorageReference riversRef = storageRef.child(""+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"/dp.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        dp_url=taskSnapshot.getUploadSessionUri().toString();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        Map<String,String> pharmacy=new HashMap<>();
        pharmacy.put("pharma_name",pharma_name.getText().toString());
        pharmacy.put("pharma_phone1",pharma_phone1.getText().toString());
        pharmacy.put("pharma_phone2",pharma_phone2.getText().toString());
        pharmacy.put("pharma_addr",pharma_addr_coords);
        pharmacy.put("pharma_dp_url",dp_url);
        


        db.collection("pharmacies").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(pharmacy)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPharmaDetails.this,"success",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPharmaDetails.this,"fail",Toast.LENGTH_SHORT).show();
                    }
                });
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
                Place place = PlacePicker.getPlace(this,data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                pharma_addr.setText(place.getAddress());
                pharma_addr_coords=place.getLatLng().toString();
            }
        }
        if(requestCode==GET_FROM_GALLERY&&resultCode==RESULT_OK){
            selectImage=data.getData();

            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectImage);
                ImageView dp=findViewById(R.id.display_picture);
                dp.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void selectImage(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),GET_FROM_GALLERY);
    }
}
