package com.example.roomassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

import com.example.roomassignment.Model.Session.Sessionmanager;
import com.example.roomassignment.View.HomeActivity;
import com.example.roomassignment.View.login.UserLogin;
import com.example.roomassignment.View.signUp.UserSignup;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashMethod();
    }
    public void SplashMethod(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckedAlreadyLogin();
            }
        },3000);
    }

    private void CheckedAlreadyLogin(){
        Sessionmanager sessionmanager=new Sessionmanager(this);
        if(sessionmanager.getLogin()){
            Intent nextactivity = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(nextactivity);
            finish();
        }else{
            Intent nextactivity = new Intent(MainActivity.this, UserLogin.class);
            startActivity(nextactivity);
            finish();
        }
    }
}