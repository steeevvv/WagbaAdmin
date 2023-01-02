package com.stivoo.wagbaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.stivoo.wagbaadmin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.navBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Home:
                    FragmentManager fragm = getSupportFragmentManager();
                    fragm.beginTransaction().replace(R.id.frameLayout, new HomeFragment(), "HOMEEE").addToBackStack(null).commit();
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.Delivered:
                    replaceFragment(new FilterFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragm = getSupportFragmentManager();
        fragm.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
    }
}