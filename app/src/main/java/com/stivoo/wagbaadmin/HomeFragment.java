package com.stivoo.wagbaadmin;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    OrdersAdapter adapter = new OrdersAdapter(this);
    ArrayList<OrderModel> orders;


    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeFragmentViewModel homeViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        LiveData<DataSnapshot> liveData = homeViewModel.getOrdersLiveData();

        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                orders = new ArrayList<>();
                for (DataSnapshot dataSnapshott: dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshottt : dataSnapshott.getChildren()) {
                        if (dataSnapshottt.child("statusDelivery").getValue().equals("--:--")) {

                            OrderModel x = new OrderModel();
                            x.setUid(dataSnapshott.getKey().toString());
                            Order item = new Order();
                            item.setId((String) dataSnapshottt.child("id").getValue());
                            item.setOrderDate((String) dataSnapshottt.child("orderDate").getValue());
                            item.setStatusProcess((String) dataSnapshottt.child("statusProcess").getValue());
                            item.setStatusConfirm((String) dataSnapshottt.child("statusConfirm").getValue());
                            item.setStatusCooking((String) dataSnapshottt.child("statusCooking").getValue());
                            item.setStatusDelivery((String) dataSnapshottt.child("statusDelivery").getValue());
                            item.setGate((String) dataSnapshottt.child("gate").getValue());
                            ArrayList<CartItem> ci = new ArrayList<>();
                            for (DataSnapshot meal : dataSnapshottt.child("meals").getChildren()) {
                                CartItem ciMeal = new CartItem();
                                ciMeal.setAdditional_info((String) meal.child("additional_info").getValue());
                                ciMeal.setDelivery_fee(Float.parseFloat((String.valueOf(meal.child("delivery_fee").getValue()))));
                                ciMeal.setImg((String) meal.child("img").getValue());
                                ciMeal.setMeal_name((String) meal.child("meal_name").getValue());
                                ciMeal.setPrice((String) meal.child("price").getValue());
                                ciMeal.setQty(Integer.parseInt(((String.valueOf(meal.child("qty").getValue())))));
                                ciMeal.setRestaurant_name((String) meal.child("restaurant_name").getValue());
                                ci.add(ciMeal);
                            }
                            item.setMeals(ci);
                            item.setOrderInfo((String) dataSnapshottt.child("orderInfo").getValue());
                            item.setPeriod((String) dataSnapshottt.child("period").getValue());
                            item.setOrderTime((String) dataSnapshottt.child("orderTime").getValue());
                            x.setTimestamp(dataSnapshottt.getKey().toString());
                            x.setOrderData(item);
                            orders.add(x);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            orders.sort(new Comparator<OrderModel>() {
                                public int compare(OrderModel u1, OrderModel u2) {
                                    if (u1.getOrderData().getId() == u2.getOrderData().getId())
                                        return 0;
                                    return Long.parseLong(u1.getOrderData().getId().substring(1)) < Long.parseLong(u2.getOrderData().getId().substring(1)) ? -1 : 1;
                                }
                            });
                        }
                        Collections.reverse(orders);
                        adapter.setList(orders);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.po_recyclerview);
        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onViewBtnClick(OrderModel pos) {
        FragmentManager fragm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new OrderState(pos));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}