package com.stivoo.wagbaadmin;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class AuthRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> logOutMutableLiveData;
    private FirebaseAuth firebaseAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public AuthRepository(Application application) {
        this.application = application;

        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        logOutMutableLiveData = new MutableLiveData<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReferenceFromUrl("https://wagba-c9f53-default-rtdb.firebaseio.com/");

        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            logOutMutableLiveData.postValue(false);
        }
    }

    public void login(String mail, String password) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            } else {
                                Toast.makeText(application, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void logOut() {
        firebaseAuth.signOut();
        logOutMutableLiveData.postValue(true);
    }

    public MutableLiveData<Boolean> getLogOutMutableLiveData() {
        return logOutMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}