package com.dreamtek.systemservice.test.demo;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;


import com.dreamtek.systemservice.test.demo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        BottomNavigationView navigationView = findViewById(R.id.nav_menu);

        // 配置navigation与底部菜单之间的联系
        // 底部菜单的样式里面的item里面的ID与navigation布局里面指定的ID必须相同，否则会出现绑定失败的情况
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_system,R.id.navigation_settings,R.id.navigation_network,R.id.navigation_deviceinfo)
                .build();

        // 建立fragment容器的控制器，这个容器就是页面的上的fragment容器
        NavController navController = Navigation.findNavController(this, R.id.nav_stu_fragment);

        // 启动
        NavigationUI.setupActionBarWithNavController(BottomNavActivity.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}