package com.example.bloodbank;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;

public class ViewProfile extends Activity {
    //    String id="sds";
    TextView name, email, address, phone, bgroup, gender;
    ImageView profile_pic;
    DatabaseReference bloodreqRef;
    DatabaseReference notificatioRef;
    Bundle i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        i= getIntent().getExtras();
        bloodreqRef= FirebaseDatabase.getInstance().getReference().child("Blood Requests");
        notificatioRef=FirebaseDatabase.getInstance().getReference().child("Notifications");
ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        name = findViewById(R.id.name_user);
        email = findViewById(R.id.email_user);
        address = findViewById(R.id.address_user);
        phone = findViewById(R.id.phone_user);
        bgroup = findViewById(R.id.blood_type_user);
        gender = findViewById(R.id.gender_user);
        profile_pic = findViewById(R.id.profile_pic_user);

       

        name.setText(i.getString("name"));
        email.setText(i.getString("email"));
        address.setText(i.getString("address"));
        phone.setText(i.getString("phone"));

        bgroup.setText(i.getString("bgroup"));
        gender.setText(i.getString("gender"));
//                           profile_pic.set
        if(i.getString("imgloc")!=null)
        Picasso.get().load(i.getString("imgloc")).into(profile_pic);
        else{
           profile_pic.setImageResource(R.mipmap.profile);
        }


    }

    public void calluser(View view) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + phone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }
    public void viewOnMap(View view){

        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override

                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Creates an Intent that will load a map of San Francisco
                        GeoPoint g = documentSnapshot.getGeoPoint("location");
                        double myLatitude=g.getLatitude(),myLongitude=g.getLongitude();
                        String labelLocation=documentSnapshot.getString("name");
                        assert g != null;
                        Uri gmmIntentUri = Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }




//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            Log.e("src",src);
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("Exception",e.getMessage());
//            return null;
//        }
//    }
}
