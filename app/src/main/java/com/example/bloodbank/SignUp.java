package com.example.bloodbank;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bloodbank.MainActivity.db;

public class SignUp extends AppCompatActivity {
    private static final int PERMISSION_ID = 15;
    ImageView propic;
    Button submit;
    Uri filePath;
    GeoPoint g;
//    ProgressDialog progressDialog;
//    UploadTask uploadTask;
    String urlStorage;
    CheckBox donorcheck;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayAdapter<String> adapter1, adapter2;
    Spinner spinnerbtype, spinnergender;
    String id;
    EditText email, password, name, address ,phone;
    ProgressDialog progressDialog;
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
        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        requestMultiplePermissions();

        donorcheck=findViewById(R.id.donorcheck);

        spinnerbtype = findViewById(R.id.spinnerbtype);
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, group);
        spinnerbtype.setAdapter(adapter1);

        spinnergender = findViewById(R.id.spinner_gender);
        String[] gender = new String[]{"male", "female", "other"};
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        spinnergender.setAdapter(adapter2);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        ImageButton upload = findViewById(R.id.uploadb);



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
                   if((Email == null) || (Password == null) ||( name.getText().toString()==null)||(address.getText().toString()==null)||(phone.getText().toString()==null)){

                           AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

                           builder.setMessage("Please enter required details")
                                   .setTitle("Error");

                           AlertDialog dialog = builder.create();
                           dialog.show();
                            return;

                   }
                   progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setTitle("Signing in...");
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                      FirebaseUser user = mAuth.getCurrentUser();
                                        mAuth.signInWithEmailAndPassword(Email,Password)
                                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {

                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            uploadImage(user,Email);
                                                        }


                                                    }
                                                });
//
                                    } else {
                                     
                                        Log.e("failed", "create Account: Fail!", task.getException());
//
                                    }

                                    // ...
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(),"Something is wrong",-1).show();
                        }
                    });

        }catch (Exception e){}}});


    }

    private void chooseImage() {
//        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//
//        startActivityForResult(intent,100);
      Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100
                )
        {
            try {
//                propic=findViewById(R.id.profile_pic);
//                filePath = data.getData();
//                propic.setImageURI(filePath);
            filePath = data.getData();

//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                InputStream inputStream =this.getContentResolver().openInputStream(data.getData());
               Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                propic=findViewById(R.id.profile_pic);
                propic.setImageBitmap(bmp);
            }
            catch (Exception e)
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
//                                Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
    private void uploadImage(FirebaseUser user,String Email) {

        if(filePath != null)
        {


            StorageReference ref = storageReference.child("users/"+ user.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                  try {
                                      urlStorage=uri.toString();
                                  }catch (NullPointerException e){
                                      e.printStackTrace();
                                  }

                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    updateUI(user,Email);
                                }
                            });
//                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                            while (!uriTask.isComplete());
//                            Uri uriphoto = uriTask.getResult();
//                            urlStorage = uriphoto.toString();


//                            progressDialog.dismiss();
//                            Toast.makeText(SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                            progressDialog.setMessage("Signing in..."+(int)progress+"%");
                        }
                    });

        }
    }
        private void updateUI (FirebaseUser currentUser,String Email)  {
//            try{uploadImage(currentUser);}catch(Exception e){Log.d("userid  ","isnull");}
//        try{Log.d("userid  ",currentUser.getUid());}catch(Exception e){Log.d("userid  ","isnull");}


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
//            storageReference.child("users/" + currentUser.getUid()).getDownloadUrl()
//                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                           urlStorage= uri.toString();
//
//                        }
//                    });
            data.put("donor", donorcheck.isChecked());
            data.put("location",g);
            data.put("imgloc",urlStorage );
            db.collection("users").document(currentUser.getUid()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Intent i =new Intent(SignUp.this,HomePageActivity.class);
//                            i.putExtra("userid",currentUser);

                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    g =new GeoPoint(location.getLatitude(),location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mlocation = locationResult.getLastLocation();
            g =new GeoPoint(mlocation.getLatitude(),mlocation.getLongitude());

        }
    };
}








