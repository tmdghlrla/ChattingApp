package com.tmddozla.chattingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tmddozla.chattingapp.config.Config;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment firstFragment;
    Fragment secondFragment;
    Fragment thirdFragment;
    Fragment fourthFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 토큰 확인하고 없으면 회원가입 액티비티 띄우기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        secondFragment = new secondFragment();
        thirdFragment = new thirdFragment();
        fourthFragment = new fourthFragment();

        loadFragment(secondFragment);

        // 바텀 네비게이션 뷰 클릭 했을때
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment fragment = null;
                if(itemId == R.id.secondFragment) {
                    fragment = secondFragment;
                } else if (itemId == R.id.thirdFragment) {
                    fragment = thirdFragment;
                } else if (itemId == R.id.fourthFragment) {
                    showAlertDialog();
                }
                return loadFragment(fragment);
            }
        });

    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        } else {
            return false;
        }
    }

    private void showAlertDialog() {
        // 버튼 3개까지 가능
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // 이 다이얼로그의 외곽부분을 눌렀을때 사라지지 않도록 하는 코드
        builder.setCancelable(false);

        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?" );
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //        현재 이 액티비티를 종료한다. => 액티비티가 1개면 앱이 종료
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", "");
                editor.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }





}
