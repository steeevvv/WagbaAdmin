package com.stivoo.wagbaadmin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragmentViewModel extends ViewModel {
    private static final DatabaseReference ORDERS_REF =
            FirebaseDatabase.getInstance().getReference("/ConfirmedOrders");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(ORDERS_REF);

    @NonNull
    public LiveData<DataSnapshot> getOrdersLiveData() {
        return liveData;
    }

}
