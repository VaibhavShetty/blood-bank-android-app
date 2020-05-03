package com.example.bloodbank;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.example.bloodbank.MainActivity.db;

public class SignUp extends Activity {
    ImageView propic;
    Button submit;
    Uri filePath;
//    ProgressDialog progressDialog;
//    UploadTask uploadTask;
    String urlStorage;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayAdapter<String> adapter1, adapter2;
    Spinner spinnerbtype, spinnergender;
    String id;
    EditText email, password, name, address ,phone;
//    private int GALLERY = 1, CAMERA = 2;
    private FirebaseAuth mAuth;

    private static final String IMAGE_DIRECTORY = "/demonuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        requestMultiplePermissions();

        spinnerbtype = findViewById(R.id.spinnerbtype);
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, group);
        spinnerbtype.setAdapter(adapter1);

        spinnergender = findViewById(R.id.spinner_gender);
        String[] gender = new String[]{"male", "female", "other"};
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        spinnergender.setAdapter(adapter2);

        Button upload = findViewById(R.id.uploadb);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    email = findViewById(R.id.email);

                    password = findViewById(R.id.password);

                   final String Email = email.getText().toString(),
                            Password = password.getText().toString();

                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in: success
                                        // update UI for current User
                                      FirebaseUser user = mAuth.getCurrentUser();
                                        mAuth.signInWithEmailAndPassword(Email,Password)
                                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Sign in: success
                                                            // update UI for current User
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            updateUI(user,Email);
                                                        } else {
                                                            // Sign in: fail
//                                                            feedback.setText("invalid user");
//                                                            updateUI(null);
                                                        }

                                                        // ...
                                                    }
                                                });
//                                        assert user != null;
//                                        id= user.getUid();
//                                        updateUI(user,Email);
                                    } else {
                                        // Sign in: fail
                                        Log.e("failed", "create Account: Fail!", task.getException());
//                                        updateUI(null,null);
                                    }

                                    // ...
                                }
                            });

        }catch (Exception e){}}});


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                propic=findViewById(R.id.profile_pic);
                propic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
        private void requestMultiplePermissions () {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
//                            openSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }
    private void uploadImage(FirebaseUser user) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("users/"+ user.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
        private void updateUI (FirebaseUser currentUser,String Email)  {
            try{uploadImage(currentUser);}catch(Exception e){Log.d("userid  ","isnull");}
//        try{Log.d("userid  ",currentUser.getUid());}catch(Exception e){Log.d("userid  ","isnull");}

            name = findViewById(R.id.name);
            address = findViewById(R.id.address);
            phone=findViewById(R.id.phone);
            String Name = name.getText().toString(),
                    Address = address.getText().toString(),
                    bg = spinnerbtype.getSelectedItem().toString(),
                    gen = spinnergender.getSelectedItem().toString(),
                    Phone=phone.getText().toString();
            Map<String, Object> data = new HashMap<>();
            data.put("email", Email);
//                    data.put("dp", propic);
            data.put("name", Name);
            data.put("address", Address);
            data.put("bgroup", bg);
            data.put("gender", gen);
            data.put("phone",Phone);
            storageReference.child("users/" + currentUser.getUid()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           urlStorage= uri.toString();

                        }
                    });
            data.put("imgloc",urlStorage );
            db.collection("users").document(currentUser.getUid()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i =new Intent(SignUp.this,HomePageActivity.class);
//                            i.putExtra("userid",currentUser);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            }
}







