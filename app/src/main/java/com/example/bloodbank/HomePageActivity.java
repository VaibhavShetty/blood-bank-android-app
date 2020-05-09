package com.example.bloodbank;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
DrawerLayout drawerLayout;
    ProgressDialog progressDialog;
    ImageView propic_up;
    Uri filePath;


    String urlStorage;

    StorageReference storageReference;
    FirebaseUser currentUser=mAuth.getCurrentUser();

NavigationView navigationView;
ActionBarDrawerToggle actionBarDrawerToggle;

Toolbar toolbar;

FragmentManager fragmentManager;
FragmentTransaction fragmentTransaction;
boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        navigationView =findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout=findViewById(R.id.drawer);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if(item.getItemId()==R.id.nearbydon){

            fragmentTransaction.replace(R.id.container_fragment,new MainFragment());
            fragmentTransaction.commit();
//        }else if(item.getItemId()==R.id.requestdon) {
//            fragmentTransaction.replace(R.id.container_fragment, new Request_fragment());
//            fragmentTransaction.commit();
        }else if(item.getItemId()==R.id.edit_profile){
            fragmentTransaction.replace(R.id.container_fragment, new EditProfile());
            fragmentTransaction.commit();
        }else if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(this,LoginActivity.class));

        }
        drawerLayout.closeDrawer(GravityCompat.START,true);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();

            finish();

        }
if(!this.doubleBackToExitPressedOnce)
    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        this.doubleBackToExitPressedOnce = true;


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 89 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                InputStream inputStream =this.getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                propic_up=findViewById(R.id.profile_pic_update);
                propic_up.setImageBitmap(bmp);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(View view) {
        progressDialog = new ProgressDialog(HomePageActivity.this);
        progressDialog.setTitle("Updating...");
        progressDialog.show();
        if(filePath != null)
        {


            StorageReference ref = storageReference.child("users/"+ currentUser.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("storage","stroed");
                                            urlStorage=uri.toString();
                                            progressDialog.dismiss();

                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            updateUserInfo(view);
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(HomePageActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    public void updateUserInfo(View view) {

        progressDialog.dismiss();

//        try{Log.d("userid  ",currentUser.getUid());}catch(Exception e){Log.d("userid  ","isnull");}
        EditText name,address,phone;
        Spinner spinnerbtype,spinnergender;
        name = view.findViewById(R.id.name_update);
        address = view.findViewById(R.id.address_update);
        phone=view.findViewById(R.id.phone_update);
        spinnerbtype=view.findViewById(R.id.spinnerbtype_update);
        spinnergender=view.findViewById(R.id.spinner_gender_update);
        String Name = name.getText().toString(),
                Address = address.getText().toString(),
                bg = spinnerbtype.getSelectedItem().toString(),
                gen = spinnergender.getSelectedItem().toString(),
                Phone=phone.getText().toString();

        Map<String, Object> data = new HashMap<>();
//                    data.put("dp", propic);
        data.put("name", Name);
        data.put("address", Address);
        data.put("bgroup", bg);
        data.put("gender", gen);


        data.put("phone",Phone);

    data.put("imgloc",urlStorage );


        db.collection("users").document(currentUser.getUid()).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"updated successfully",Toast.LENGTH_LONG).show();
                        fragmentTransaction.replace(R.id.container_fragment, new EditProfile());
                        fragmentTransaction.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
            }
        });

    }
    private void requestMultiplePermissions () {
        Dexter.withActivity(HomePageActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
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

    public void chooseImageupdate(View view) {
        requestMultiplePermissions();
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "update picture"), 89);
    }





}
