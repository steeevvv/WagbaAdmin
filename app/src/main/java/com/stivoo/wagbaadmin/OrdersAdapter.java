package com.stivoo.wagbaadmin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private static ArrayList<OrderModel> orders_list = new ArrayList<>();


    public OrdersAdapter(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        holder.gate.setText(orders_list.get(position).getOrderData().getGate());
        DecimalFormat df = new DecimalFormat("#.00");
        holder.delivery.setText("EGP "+ df.format(orders_list.get(position).getOrderData().getTotal()));
        holder.time.setText(orders_list.get(position).getOrderData().getPeriod());
        holder.date.setText(orders_list.get(position).getOrderData().getOrderDate() + " (" + orders_list.get(position).getOrderData().getOrderTime()+")");
        holder.id.setText(orders_list.get(position).getOrderData().getId());
        if(Objects.equals(orders_list.get(position).getOrderData().getOrderInfo(), "")){
            holder.info.setText("--- None ---");
        }else {
            holder.info.setText(orders_list.get(position).getOrderData().getOrderInfo());
        }


     DatabaseReference ref =
        FirebaseDatabase.getInstance().getReference("/Users" + "/" + orders_list.get(position).getUid());

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                holder.name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                holder.number.setText(String.valueOf(dataSnapshot.child("phone").getValue()));
                Glide.with(holder.getImg()).load(String.valueOf(dataSnapshot.child("profile_img").getValue())).into(holder.img);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    ref.addListenerForSingleValueEvent(eventListener);



    }

    @Override
    public int getItemCount() {
        return orders_list.size();
    }

    public void setList(ArrayList<OrderModel> orders_list) {
        this.orders_list = orders_list;
        notifyDataSetChanged();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        TextView delivery;
        TextView gate;
        TextView time;
        TextView date;
        TextView id;
        TextView info;
        ImageView img;
        ConstraintLayout l;

        public ImageView getImg() {
            return img;
        }

        public OrdersViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_person_name);
            number = itemView.findViewById(R.id.tv_person_phone);
            delivery = itemView.findViewById(R.id.tv_delivery_fees);
            gate = itemView.findViewById(R.id.tv_gate);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            id = itemView.findViewById(R.id.tv_id);
            info = itemView.findViewById(R.id.tv_info);
            img = itemView.findViewById(R.id.person_photo);
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onViewBtnClick(orders_list.get(pos));
                    }
                }
            });
        }
    }
}
