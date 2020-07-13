package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Intent(Context packageContext, Class) : 액티비티 클래스를 구현하는 context, 호출할 액티비티의 클래스
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
