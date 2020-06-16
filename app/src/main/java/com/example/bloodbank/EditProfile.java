package com.example.bloodbank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;

public class EditProfile extends Fragment  {
    View view;
    ZMyDatabaseDataStructure z ;
    String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();









    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        db.collection("users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                         z = documentSnapshot.toObject(ZMyDatabaseDataStructure.class);
                        assert z != null;
                        if(!documentSnapshot.contains("donor"))
                        updateFields(view, z,true);
                        else{
                            updateFields(view, z,documentSnapshot.getBoolean("donor"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return view;
    }

    public void updateFields(View view, @NotNull ZMyDatabaseDataStructure donor,Boolean isdonor) {
        EditText name, address, phone;
        Spinner bdg, genspinner;
        CheckBox checkBox;
        Picasso.get().load(donor.getPhoto()).into((ImageView) view.findViewById(R.id.profile_pic_update));
        bdg = view.findViewById(R.id.spinnerbtype_update);
        genspinner = view.findViewById(R.id.spinner_gender_update);
        name = view.findViewById(R.id.name_update);
        checkBox= view.findViewById(R.id.donorcheckupdate);
        checkBox.setChecked(isdonor);
        address = view.findViewById(R.id.address_update);
        phone = view.findViewById(R.id.phone_update);
        name.setText(donor.getName());
        String bgroup=donor.getBgroup();
        String gen= donor.getGender();
        address.setText(donor.getAddress());
        phone.setText(donor.getPhone());
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                group);
        bdg.setAdapter(adapter1);
        Map<String, Integer> bdgmap = new HashMap<>();
        int i = 0;
        for (String s : group) {
            bdgmap.put(s, i);
            i++;
        }



        bdg.setSelection(bdgmap.get(bgroup));

        String[] gender = new String[]{"male", "female", "other"};
        Map<String, Integer> genmap = new HashMap<>();
        i = 0;
        for (String s : gender) {
            genmap.put(s, i);
            i++;
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender);
        genspinner.setAdapter(adapter2);
        i = genmap.get(gen);

        genspinner.setSelection(i);


    }
//
//    @Override
//    public void onClick(View view) {
//        chooseImageupdate(view);
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 89 && resultCode == Activity.RESULT_OK
//                && data != null && data.getData() != null )
//        {
//            filePath = data.getData();
//            try {
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                InputStream inputStream =EditProfile.this.getActivity().getContentResolver().openInputStream(data.getData());
//                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//
//                propic_up.setImageBitmap(bmp);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void uploadImage(View view) {
//        EditText name,address,phone;
//
//        Spinner spinnerbtype,spinnergender;
//        name = view.findViewById(R.id.name_update);
//        address = view.findViewById(R.id.address_update);
//
//        phone=view.findViewById(R.id.phone_update);
//        spinnerbtype=view.findViewById(R.id.spinnerbtype_update);
//        spinnergender=view.findViewById(R.id.spinner_gender_update);
//        propic_up=view.findViewById(R.id.profile_pic_update);
//        if( name.getText().toString()==null|| !spinnerbtype.isSelected()||!spinnergender.isSelected()||address.getText().toString()==null||phone.getText().toString()==null){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this.getActivity());
//
//            builder.setMessage("Please enter required details")
//                    .setTitle("Error");
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//            return;
//
//        }
//        progressDialog = new ProgressDialog(EditProfile.this.getActivity());
//        progressDialog.setTitle("Updating...");
//        progressDialog.show();
//        if(filePath != null)
//        {
//
//
//            StorageReference ref = storageReference.child("users/"+ currentUser.getUid());
//            ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    ref.putFile(filePath)
//                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                    ref.getDownloadUrl()
//                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                @Override
//                                                public void onSuccess(Uri uri) {
//                                                    Log.d("storage","stroed");
//                                                    urlStorage=uri.toString();
//                                                    progressDialog.dismiss();
//
//                                                }
//                                            })
//                                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Uri> task) {
//                                                    updateUserInfo(view);
//                                                }
//                                            });
//
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(EditProfile.this.getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            });
//
//
//        }
//    }
//    public void updateUserInfo(View view) {
//
//        progressDialog.dismiss();
//
////        try{Log.d("userid  ",currentUser.getUid());}catch(Exception e){Log.d("userid  ","isnull");}
//        EditText name,address,phone;
//        CheckBox donorcheck;
//        Spinner spinnerbtype,spinnergender;
//        name = view.findViewById(R.id.name_update);
//        address = view.findViewById(R.id.address_update);
//        donorcheck= view.findViewById(R.id.donorcheckupdate);
//        phone=view.findViewById(R.id.phone_update);
//        spinnerbtype=view.findViewById(R.id.spinnerbtype_update);
//        spinnergender=view.findViewById(R.id.spinner_gender_update);
//
//        String Name = name.getText().toString(),
//                Address = address.getText().toString(),
//                bg = spinnerbtype.getSelectedItem().toString(),
//                gen = spinnergender.getSelectedItem().toString(),
//                Phone=phone.getText().toString();
//
//        Map<String, Object> data = new HashMap<>();
////                    data.put("dp", propic);
//        data.put("name", Name);
//        data.put("address", Address);
//        data.put("bgroup", bg);
//        data.put("gender", gen);
//
//        data.put("donor",donorcheck.isChecked());
//        data.put("phone",Phone);
//
//        data.put("imgloc",urlStorage );
//
//
//        db.collection("users").document(currentUser.getUid()).update(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressDialog.dismiss();
//                        Toast.makeText(EditProfile.this.getActivity(),"updated successfully",Toast.LENGTH_LONG).show();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//            }
//        });
//
//    }
//    private void requestMultiplePermissions () {
//        Dexter.withActivity(EditProfile.this.getActivity())
//                .withPermissions(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.INTERNET,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(EditProfile.this.getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
////                            openSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).
//                withErrorListener(new PermissionRequestErrorListener() {
//                    @Override
//                    public void onError(DexterError error) {
//                        Toast.makeText(EditProfile.this.getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .onSameThread()
//                .check();
//    }
//
//    public void chooseImageupdate(View view) {
//        requestMultiplePermissions();
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(i, "update picture"), 89);
//    }
//

}


