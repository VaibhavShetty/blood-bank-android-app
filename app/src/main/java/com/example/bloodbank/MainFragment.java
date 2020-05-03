package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;


public class MainFragment extends Fragment implements DonorAdapter.onSingleDonorClickListener {
    private View view;
//    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    private RecyclerView recyclerView;
    DonorAdapter donorAdapter;
    Spinner spinnerbtype;
    ArrayAdapter<String> adapter1;
    String id;
    final ArrayList<ZMyDatabaseDataStructure> donorlist= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        spinnerbtype = view.findViewById(R.id.spinnerbtypefrag);
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        adapter1 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, group);
        spinnerbtype.setAdapter(adapter1);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       Log.d("size", String.valueOf(queryDocumentSnapshots.size()));
//                        String name2,bgroup2;
                          for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
//                            name2 =documentSnapshot.getString("name");
//                                    bgroup2 =documentSnapshot.getString("bgroup");

//                              Picasso.with(activity).load(documentSnapshot.getString("imgloc")).transform(new CircleTransform()).into(pro);
//                              id=documentSnapshot.getId();
                              Log.d("id", documentSnapshot.getId());
                              Log.d("iduser", mAuth.getCurrentUser().getUid());

                              if(!documentSnapshot.getId().equals(mAuth.getCurrentUser().getUid())) {
                                  ZMyDatabaseDataStructure z = documentSnapshot.toObject(ZMyDatabaseDataStructure.class);


                            donorlist.add(z);}

                          }
                        donorAdapter = new DonorAdapter(donorlist,MainFragment.this);
                        recyclerView.setAdapter(donorAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

//      displayDonors(view);
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
        startActivity(i);
    }



//    public void displayDonors(View view) {
//
////        ButterKnife.bind(this);
//        String id = "9DBet2djXGWdkTuvEKy14JOG2Y63";
////        Query lessQuery = db.collection("users").whereLessThan("uid", id);
////        Query greaterQuery = db.collection("users").whereGreaterThan("uid", id);
////        Task lessQuerytask  = lessQuery.get();
//        Query query = FirebaseFirestore.getInstance()
//                .collection("users");
////        Task<com.google.firebase.firestore.QuerySnapshot> greaterQuerytask = greaterQuery.get();
//        FirestoreRecyclerOptions<ZMyDatabaseDataStructure> options = new FirestoreRecyclerOptions.Builder<ZMyDatabaseDataStructure>()
//                .setQuery(query,ZMyDatabaseDataStructure.class)
//                .build();
////        donorAdapter = new DonorAdapter(options);
//
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity();
//        recyclerView.setAdapter(donorAdapter);
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                donorAdapter.deleteItem(viewHolder.getAdapterPosition());
//            }
//        }).attachToRecyclerView(recyclerView);
//
//        donorAdapter.setOnItemClickListener(new DonorAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                ZMyDatabaseDataStructure note = documentSnapshot.toObject(ZMyDatabaseDataStructure.class);
//                String id = documentSnapshot.getId();
//                String path = documentSnapshot.getReference().getPath();
//                Toast.makeText(getActivity(),
//                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
//class DonorHolder extends RecyclerView.ViewHolder {
//    TextView name;
//    TextView bgroup;
////    Button viewDetails,callperson;
//
//    public DonorHolder(View itemView) {
//        super(itemView);
//        name = itemView.findViewById(R.id.name);
//        bgroup = itemView.findViewById(R.id.bgroup);
//        viewDetails = itemView.findViewById(R.id.viewprfile);
//        callperson=itemView.findViewById(R.id.call);
//        callperson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION && listener != null) {
//                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                }
//
//            }
//        });
//
//        viewDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION && listener != null) {
//                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                }
//            }
//        });

//    public class CircleTransform implements Transformation {
//        @Override
//        public Bitmap transform(Bitmap source) {
//            int size = Math.min(source.getWidth(), source.getHeight());
//
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//
//            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//            if (squaredBitmap != source) {
//                source.recycle();
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
//
//            Canvas canvas = new Canvas(bitmap);
//            Paint paint = new Paint();
//            BitmapShader shader = new BitmapShader(squaredBitmap,
//                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            paint.setShader(shader);
//            paint.setAntiAlias(true);
//
//            float r = size / 2f;
//            canvas.drawCircle(r, r, r, paint);
//
//            squaredBitmap.recycle();
//            return bitmap;
//        }
//
//        @Override
//        public String key() {
//            return "circle";
//        }
//    }
    }
//