package com.stivoo.wagbaadmin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderStateAdapter extends RecyclerView.Adapter<OrderStateAdapter.MyViewHolder> {

    private ArrayList<CartItem> orders = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.name.setText(orders.get(position).getMeal_name());
        holder.rest.setText(orders.get(position).getRestaurant_name());
        holder.qty.setText("X"+orders.get(position).getQty());
        holder.info.setText(orders.get(position).getAdditional_info());
        Glide.with(holder.getImg()).load(orders.get(position).getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setList(ArrayList<CartItem> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        TextView qty;
        TextView rest;
        TextView info;

        public ImageView getImg() {
            return img;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.cimg_meal);
            name = itemView.findViewById(R.id.tv_meal_namee);
            qty = itemView.findViewById(R.id.tv_meal_quantity);
            rest = itemView.findViewById(R.id.tv_meal_rest);
            info = itemView.findViewById(R.id.tv_meal_info);


        }
    }
}
