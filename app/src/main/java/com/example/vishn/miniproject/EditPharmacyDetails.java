package com.example.vishn.miniproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class EditPharmacyDetails extends Fragment {
    public static final int GET_FROM_GALLERY = 3, PLACE_PICKER_REQUEST = 1;
    private String addressCoords=null;
    private Uri downloadUrl, selectedImage;
    private StorageReference storageReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_edit_pharmacy_details, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Edit Details");

        storageReference= FirebaseStorage.getInstance().getReference();
        ImageButton button=view.findViewById(R.id.change_logo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
        Button confirmChanges=view.findViewById(R.id.button_confirm_changes);
        confirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    fireStoreUpdate();
                }
                else {

                }

            }
        });
        Button mapViewbtn=view.findViewById(R.id.pharmacy_location_map_edit);
        mapViewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fireStoreUpdate() {
    //first firebase storage image uploaded.
        Uri file=selectedImage;
        StorageReference display_pictures=storageReference.child("dp/"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+".jpg");
        display_pictures.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl=taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String tmsg1=e.getStackTrace().toString();
                        Toast.makeText(getActivity(), tmsg1, Toast.LENGTH_LONG).show();
                    }
                });
//        EditText pharmaName=getView().findViewById(R.id.pharmacy_name_edit);
//        EditText pharmaPhone1=getView().findViewById(R.id.pharmacy_phone1_edit);
//        EditText pharmaPhone2=getView().findViewById(R.id.pharmacy_phone2_edit);
//        FirebaseFirestore db=FirebaseFirestore.getInstance();
//        Map<String,Object> user=new HashMap<>();
//        user.put("display_picture",downloadUrl);
//        user.put("pharma_name",pharmaName);
//        user.put("phone", Arrays.asList(pharmaPhone1,pharmaPhone2));
//        user.put("address",addressCoords);
//
//        db.collection("pharmacies").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
//        .set(user)
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w("-","nao");
//            }
//        })
//        .addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getActivity(), "thankgod", Toast.LENGTH_LONG).show();
//            }
//        });


   }

    private boolean validateFields() {
        EditText pharmaName=getView().findViewById(R.id.pharmacy_name_edit);
        EditText pharmaPhone1=getView().findViewById(R.id.pharmacy_phone1_edit);
        EditText pharmaPhone2=getView().findViewById(R.id.pharmacy_phone2_edit);
        EditText pharmaAddress=getView().findViewById(R.id.pharmacy_address_edit);
        if(pharmaName.getText().equals("")){
            Toast.makeText(getActivity(), "The name field is empty", Toast.LENGTH_LONG).show();
            return  false;
        }
        if(pharmaPhone1.getText().equals("")&&pharmaPhone2.getText().equals("")){
            Toast.makeText(getActivity(), "The phone field is empty", Toast.LENGTH_LONG).show();
            return  false;
        }
        if(pharmaAddress.getText().equals("")){
            Toast.makeText(getActivity(), "The Address field is empty", Toast.LENGTH_LONG).show();
            return  false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY&&resultCode== RESULT_OK){
            selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
                ImageView dp=this.getView().findViewById(R.id.display_picture);
                dp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(),data);
                String toastMsg = String.format("Place: %s", place.getName());
                EditText editText=getView().findViewById(R.id.pharmacy_address_edit);
                editText.setText(place.getAddress());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                addressCoords=toastMsg;
            }
        }
    }
}
