package com.stivoo.wagbaadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OrderState extends Fragment {

    OrderModel order;
    TextView subtotal;
    TextView delivery;
    TextView total;
    DecimalFormat df = new DecimalFormat("#.00");
    RadioGroup radioGroup;
    RadioButton placed,confirmed,cooking,ddelivery;


    public OrderState() {

    }

    public OrderState(OrderModel order) {
        this.order = order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_order_state, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.o_summary_recyclerView);
        OrderStateAdapter adapter = new OrderStateAdapter();
        adapter.setList(order.getOrderData().getMeals());
        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        subtotal = view.findViewById(R.id.o_subtotal_val);
        delivery = view.findViewById(R.id.o_delivery_val);
        total = view.findViewById(R.id.o_total_val);
        radioGroup = view.findViewById(R.id.order_state_radio);

        if(!order.getOrderData().getStatusProcess().equals("--:--") && order
                .getOrderData().getStatusConfirm().equals("--:--") & order.getOrderData().getStatusCooking().equals("--:--")&& order.getOrderData().getStatusDelivery().equals("--:--")){
            radioGroup.check(R.id.state_placed);
        }
        else if(order.getOrderData().getStatusCooking().equals("--:--")){
            view.findViewById(R.id.state_placed).setEnabled(false);
            radioGroup.check(R.id.state_confirmed);
        }else if(order.getOrderData().getStatusDelivery().equals("--:--")){
            view.findViewById(R.id.state_placed).setEnabled(false);
            view.findViewById(R.id.state_confirmed).setEnabled(false);
            radioGroup.check(R.id.state_cooking);
        }else{
            view.findViewById(R.id.state_placed).setEnabled(false);
            view.findViewById(R.id.state_confirmed).setEnabled(false);
            view.findViewById(R.id.state_cooking).setEnabled(false);
            radioGroup.check(R.id.state_delivery);
        }

        float deliveryy = 0.00f;
        float subtotal_value = 0.00f;

            for (CartItem ci : order.getOrderData().getMeals()) {
                subtotal_value += ci.getQty() * Float.parseFloat(ci.getPrice().substring(4));
                if (ci.getDelivery_fee() > deliveryy) {
                    deliveryy = ci.getDelivery_fee();
                }
            }
        total.setText("EGP "+ df.format(subtotal_value + deliveryy));
        subtotal.setText("EGP "+ df.format(subtotal_value));
        delivery.setText("EGP "+ df.format(deliveryy));

        placed = view.findViewById(R.id.state_placed);
        cooking = view.findViewById(R.id.state_cooking);
        confirmed = view.findViewById(R.id.state_confirmed);
        ddelivery = view.findViewById(R.id.state_delivery);

        final DatabaseReference ORDER_REF = FirebaseDatabase.getInstance().getReference("/ConfirmedOrders/" + order.getUid() +"/"+order.getTimestamp());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.state_confirmed) {
                        long currentTime = Calendar.getInstance().getTime().getHours();
                        if(order.getOrderData().getPeriod().equals("12:00 (Noon Period)")) {
                            if (10 - currentTime > 0) {
                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Date idd = Calendar.getInstance().getTime();
                                            ORDER_REF.child("statusCooking").setValue("--:--");
                                            ORDER_REF.child("statusConfirm").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                            ORDER_REF.child("statusDelivery").setValue("--:--");
                                            view.findViewById(R.id.state_placed).setEnabled(false);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                };
                                ORDER_REF.addListenerForSingleValueEvent(eventListener);
                                Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                            } else if (10 - currentTime == 0) {
                                if (Calendar.getInstance().getTime().getMinutes() - 30 < 0) {
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Date idd = Calendar.getInstance().getTime();
                                                ORDER_REF.child("statusCooking").setValue("--:--");
                                                ORDER_REF.child("statusConfirm").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                                ORDER_REF.child("statusDelivery").setValue("--:--");
                                                view.findViewById(R.id.state_placed).setEnabled(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };
                                    ORDER_REF.addListenerForSingleValueEvent(eventListener);
                                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Invalid Time to Confirm", Toast.LENGTH_SHORT).show();
                                    radioGroup.check(R.id.state_placed);
                                }
                            } else {
                                Toast.makeText(getContext(), "Invalid Time to Confirm", Toast.LENGTH_SHORT).show();
                                radioGroup.check(R.id.state_placed);
                            }
                    } else{
                            if (15 - currentTime > 0) {
                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Date idd = Calendar.getInstance().getTime();
                                            ORDER_REF.child("statusCooking").setValue("--:--");
                                            ORDER_REF.child("statusConfirm").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                            ORDER_REF.child("statusDelivery").setValue("--:--");
                                            view.findViewById(R.id.state_placed).setEnabled(false);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                };
                                ORDER_REF.addListenerForSingleValueEvent(eventListener);
                                Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                            } else if (15 - currentTime == 0) {
                                if (Calendar.getInstance().getTime().getMinutes() - 30 < 0) {
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Date idd = Calendar.getInstance().getTime();
                                                ORDER_REF.child("statusCooking").setValue("--:--");
                                                ORDER_REF.child("statusConfirm").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                                ORDER_REF.child("statusDelivery").setValue("--:--");
                                                view.findViewById(R.id.state_placed).setEnabled(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };
                                    ORDER_REF.addListenerForSingleValueEvent(eventListener);
                                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Invalid Time to Confirm", Toast.LENGTH_SHORT).show();
                                    radioGroup.check(R.id.state_placed);
                                }
                            } else {
                                Toast.makeText(getContext(), "Invalid Time to Confirm", Toast.LENGTH_SHORT).show();
                                radioGroup.check(R.id.state_placed);
                            }
                        }
                    }
                    else if (checkedId == R.id.state_cooking) {
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Date idd = Calendar.getInstance().getTime();
                                        ORDER_REF.child("statusCooking").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                        ORDER_REF.child("statusDelivery").setValue("--:--");
                                        view.findViewById(R.id.state_placed).setEnabled(false);
                                        view.findViewById(R.id.state_confirmed).setEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            };
                            ORDER_REF.addListenerForSingleValueEvent(eventListener);
                            Toast.makeText(getContext(), "Cooking", Toast.LENGTH_SHORT).show();
                    }else if(checkedId == R.id.state_delivery) {
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Date idd = Calendar.getInstance().getTime();
                                        ORDER_REF.child("statusDelivery").setValue(String.format("%02d", idd.getHours()) + ":"+ String.format("%02d", idd.getMinutes()));
                                        view.findViewById(R.id.state_placed).setEnabled(false);
                                        view.findViewById(R.id.state_confirmed).setEnabled(false);
                                        view.findViewById(R.id.state_cooking).setEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            };
                            ORDER_REF.addListenerForSingleValueEvent(eventListener);
                            Toast.makeText(getContext(), "Delivery", Toast.LENGTH_SHORT).show();
                        }
            }
        });


    }
    }