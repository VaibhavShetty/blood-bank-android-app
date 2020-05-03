package com.example.bloodbank;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorHolder> {

    ArrayList<ZMyDatabaseDataStructure> donorlist;
    private onSingleDonorClickListener listener;




    public DonorAdapter(ArrayList<ZMyDatabaseDataStructure> donorlist, onSingleDonorClickListener listener) {
        this.donorlist = donorlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardfor_recyclerview, parent, false);
        DonorHolder holder = new DonorHolder(view, listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull DonorHolder holder, int position) {
        ZMyDatabaseDataStructure donor = donorlist.get(position);
        holder.name.setText(donor.getName());
        holder.bgroup.setText(donor.getBgroup());
        Picasso.get().load(donor.getPhoto()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return donorlist.size();
    }

    public static class DonorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView bgroup;
        onSingleDonorClickListener listener;
        CircleImageView img;
        public DonorHolder(@NonNull View itemView, onSingleDonorClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            bgroup = itemView.findViewById(R.id.bgroup);
            img=itemView.findViewById(R.id.profile_img_recycler);
            this.listener = listener;
//            itemView.findViewById(R.id.call).setOnClickListener(this);
            itemView.findViewById(R.id.viewprfile).setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
//            listener.onCallClick(getAdapterPosition());
            listener.onDonorClick(getAdapterPosition(),view);
        }
    }

    public interface onSingleDonorClickListener {
        void onDonorClick(int position,View view);
//        void onCallClick(int position);


    }
}

//public class DonorAdapter extends FirestoreRecyclerAdapter<ZMyDatabaseDataStructure, DonorAdapter.DonorHolder> {
//    private OnItemClickListener listener;
//
//    public DonorAdapter(@NonNull FirestoreRecyclerOptions<ZMyDatabaseDataStructure> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull DonorHolder holder, int position, @NonNull ZMyDatabaseDataStructure model) {
//        holder.name.setText(model.getName());
//        holder.bgroup.setText(model.getBgroup());
//    }
//
//    @NonNull
//    @Override
//    public DonorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardfor_recyclerview,
//                parent, false);
//        return new DonorHolder(v);
//    }
//
//    public void deleteItem(int position) {
//        getSnapshots().getSnapshot(position).getReference().delete();
//    }
//
//    class DonorHolder extends RecyclerView.ViewHolder {
//        TextView name;
//        TextView bgroup;
//        Button viewDetails,callperson;
//
//        public DonorHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.name);
//            bgroup = itemView.findViewById(R.id.bgroup);
//            viewDetails = itemView.findViewById(R.id.viewprfile);
//            callperson=itemView.findViewById(R.id.call);
//            callperson.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                    }
//
//                }
//            });
//
//            viewDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                    }
//                }
//            });
//
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(DocumentSnapshot documentSnapshot, int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//}