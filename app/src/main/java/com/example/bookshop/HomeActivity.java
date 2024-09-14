package com.example.bookshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener
{

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home Activity");
        bottomNavigationView = findViewById(R.id.homeBottomNagView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeBottomNagMenuHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.homeMenuMyOffers)
        {

        } else if (item.getItemId() == R.id.homeMenuMyCard)
        {

        } else if (item.getItemId() == R.id.homeMenuMyProfile)
        {
            Intent intent =new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.homeFrameLayout)
        {

        }

        return true;
    }

    HomeFragment homeFragment=new HomeFragment();
    CategoryFragment categoryFragment=new CategoryFragment();
    MyOrderFragment myOrderFragment=new MyOrderFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       if (item.getItemId() == R.id.homeBottomNagMenuHome)
       {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,
                   homeFragment).commit();
       } else if (item.getItemId()== R.id.homeBottomNagMenucategory)
       {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,
                   categoryFragment).commit();
       } else if (item.getItemId() == R.id.homeBottomNagMenuOrder)
       {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,
                   myOrderFragment).commit();
       }

        return true;
    }
}