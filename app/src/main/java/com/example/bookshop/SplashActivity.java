package com.example.bookshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
ImageView ivlogo;
Animation animtranslate;
Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivlogo=findViewById(R.id.ivsplashlogo);
        animtranslate = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.righttolefttranslation);
       ivlogo.startAnimation(animtranslate);
   handler.postDelayed(new Runnable() {
       @Override
       public void run() {
           Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
           startActivity(intent);
       }
   },4000);
    }
}