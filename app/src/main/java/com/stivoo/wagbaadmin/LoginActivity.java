package com.stivoo.wagbaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.stivoo.wagbaadmin.databinding.ActivityLoginBinding;

import java.util.concurrent.atomic.AtomicInteger;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Intent go_to_homepage_intent;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    startActivity(go_to_homepage_intent);
                    finish();
                }
            }
        });

        go_to_homepage_intent = new Intent(this, MainActivity.class);
        binding.btnLoginLogin.setOnClickListener(v -> {
            String mail = binding.etSigninMail.getText().toString();
            String pw = binding.etSigninPw.getText().toString();

            if (mail.length()==0){
                binding.etSigninMail.setError("Please enter an Email first");
                binding.etSigninMail.setBackgroundResource(R.drawable.custom_input_err);
            } else if (pw.length() == 0){
                binding.etSigninPw.setError("Please enter your password first");
                binding.etSigninPw.setBackgroundResource(R.drawable.custom_input_err);
            }
            else{
                loginViewModel.login(mail, pw);
            }
        });

        AtomicInteger pw_visibilityToggle = new AtomicInteger();
        binding.loginViewpwBtn.setOnClickListener(v->{
            if (pw_visibilityToggle.getAndIncrement() % 2 == 0){
                binding.loginViewpwBtn.setImageResource(R.drawable.custom_visibility_off_icon);
                binding.etSigninPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else{
                binding.loginViewpwBtn.setImageResource(R.drawable.custom_visibility_icon);
                binding.etSigninPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });    }
}