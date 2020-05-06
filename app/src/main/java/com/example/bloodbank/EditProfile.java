package com.example.bloodbank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.example.bloodbank.MainActivity.db;
import static com.example.bloodbank.MainActivity.mAuth;

public class EditProfile extends Fragment {
    View view;
    String id = mAuth.getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        db.collection("users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ZMyDatabaseDataStructure z = documentSnapshot.toObject(ZMyDatabaseDataStructure.class);
                        updateFields(view, z);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return view;
    }

    public void updateFields(View view, ZMyDatabaseDataStructure donor) {
        EditText name, address, phone;
        Spinner bdg, gen;
        Picasso.get().load(donor.getPhoto()).into((ImageView) view.findViewById(R.id.profile_pic_update));
        bdg = view.findViewById(R.id.spinnerbtype_update);
        gen = view.findViewById(R.id.spinner_gender_update);
        name = view.findViewById(R.id.name_update);
        address = view.findViewById(R.id.address_update);
        phone = view.findViewById(R.id.phone_update);
        name.setText(donor.getName());
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
        i = bdgmap.get(donor.bgroup);


        bdg.setSelection(i);

        String[] gender = new String[]{"male", "female", "other"};
        Map<String, Integer> genmap = new HashMap<>();
        i = 0;
        for (String s : group) {
            bdgmap.put(s, i);
            i++;
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender);
        gen.setAdapter(adapter2);
        i = genmap.get(donor.gender);

        gen.setSelection(i);


    }
}


