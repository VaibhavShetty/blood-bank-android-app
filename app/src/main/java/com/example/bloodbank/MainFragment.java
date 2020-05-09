package com.example.bloodbank;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;


public class MainFragment extends Fragment implements DonorAdapter.onSingleDonorClickListener {
    private View view;
    private RecyclerView recyclerView;
    DonorAdapter donorAdapter;

    Spinner spinnerbtype;
    FusedLocationProviderClient mFusedLocationClient;
    double latitude,longitude;
    ArrayAdapter<String> adapter1;
    String id= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();;
    ArrayList<ZMyDatabaseDataStructure> donorlist= new ArrayList<>();
    ArrayList<ZMyDatabaseDataStructure> donorlist2= new ArrayList<>();

    int PERMISSION_ID = 45;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        spinnerbtype = view.findViewById(R.id.spinnerbtypefrag);

        String[] group = new String[]{"all" ,"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        adapter1 = new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), android.R.layout.simple_spinner_dropdown_item, group);
        spinnerbtype.setAdapter(adapter1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        getLastLocation();




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
       CollectionReference colref= db.collection("users");

        colref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


          Log.d("size", String.valueOf(queryDocumentSnapshots.size()));
          if (queryDocumentSnapshots.size() == 0) {

              AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

              builder.setMessage("no donors found")
                      .setTitle("sorry");

              AlertDialog dialog = builder.create();
              dialog.show();
          } else {
              for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                  Log.d("id", documentSnapshot.getId());
                  Log.d("iduser", id);

                  double dist= 4d;
                  if (!(id.equals(documentSnapshot.getId()))) {
                    try{ dist = distance(latitude,
                            longitude,
                            documentSnapshot.getGeoPoint("location"));}catch (NullPointerException e){ Log.d("iduser", id);}
                      if (dist < 50000) {


                          ZMyDatabaseDataStructure z = documentSnapshot.toObject(ZMyDatabaseDataStructure.class);
                          if(dist>1000){
                              dist/=1000;
                              dist= (int)(dist*100)/(double)100;
                              z.setDistance(dist);
                              z.unit="km";
                          }else {

                              z.setDistance((int)dist);
                              z.unit="m";

                          }
                          z.setId(documentSnapshot.getId());

                          donorlist.add(z);

                      }
                  }


              }
              Collections.sort(donorlist, new Comparator<ZMyDatabaseDataStructure>() {
                  @Override
                  public int compare(ZMyDatabaseDataStructure o1, ZMyDatabaseDataStructure o2) {
                      if(o1.unit.equals("m")&&o2.unit.equals("km"))
                          return -1;
                      else if(o2.unit.equals("m")&&o1.unit.equals("km"))
                          return  1;
                      else {
                          if (o1.distance == o2.distance)
                              return 0;
                          else if (o1.distance < o2.distance)
                              return -1;
                          else
                              return 1;
                      }
                  }
              });
              donorAdapter = new DonorAdapter(donorlist, MainFragment.this);
              recyclerView.setAdapter(donorAdapter);
          }

      }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



        spinnerbtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    donorAdapter = new DonorAdapter(donorlist, MainFragment.this);
                }else {
                    donorlist2.clear();
                        donorAdapter=null;
                    Iterator<ZMyDatabaseDataStructure> iter
                            = donorlist.iterator();
                    while(iter.hasNext()) {
                        ZMyDatabaseDataStructure z=iter.next();
                        Log.d("position", String.valueOf(position));
                        if (z.getBgroup().equals(group[position])) {

                            donorlist2.add(z);
                        }
                    }
                    if(donorlist2.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setMessage("no donors found")
                                .setTitle("sorry");

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    donorAdapter = new DonorAdapter(donorlist2, MainFragment.this);

                }
                recyclerView.setAdapter(donorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    @Override
    public void onDonorClick(int position,View view) {

            Intent i=        new Intent(getActivity(),ViewProfile.class);

            i.putExtra("name",donorAdapter.donorlist.get(position).name);
            i.putExtra("bgroup",donorAdapter.donorlist.get(position).bgroup);

            i.putExtra("email",donorAdapter.donorlist.get(position).email);
            i.putExtra("gender",donorAdapter.donorlist.get(position).gender);
            i.putExtra("imgloc",donorAdapter.donorlist.get(position).imgloc);
            i.putExtra("address",donorAdapter.donorlist.get(position).address);
            i.putExtra("phone",donorAdapter.donorlist.get(position).phone);
            i.putExtra("id",donorAdapter.donorlist.get(position).id);
            startActivity(i);


    }


     boolean checkPermissions() {
         return ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                 ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
     }

     void requestPermissions() {
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(this.getActivity()),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

     boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
         assert locationManager != null;
         return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
     void getLastLocation(){
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
                                    latitude = location.getLatitude();
                                    longitude= location.getLongitude();

                                    Log.d("lat",latitude+"");
                                    Log.d("long",longitude+"");

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(MainFragment.this.getActivity(),"enable location",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
     void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getActivity()));
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,

                Looper.myLooper()
        );

    }
     LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
//

        }
    };
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }
    public static double distance(double lat1, double lng1, GeoPoint point){
        try {

            Location location1 = new Location("locationA");
            location1.setLatitude(lat1);
            location1.setLongitude(lng1);
            Location location2 = new Location("locationB");
            location2.setLatitude(point.getLatitude());
            location2.setLongitude(point.getLongitude());

            double dista= location1.distanceTo(location2);

            return dista;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }
    }
