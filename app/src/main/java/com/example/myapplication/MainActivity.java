package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Telephone"));
        tabLayout.addTab(tabLayout.newTab().setText("Image"));
        //tabLayout.addTab(tabLayout.newTab().setText("My Telephone"));
        tabLayout.addTab(tabLayout.newTab().setText("Memo"));

        tabLayout.getTabAt(0).setIcon(R.drawable.telephone);
        tabLayout.getTabAt(1).setIcon(R.drawable.image);
        tabLayout.getTabAt(2).setIcon(R.drawable.memo);
        //tabLayout.getTabAt(2).setIcon(R.drawable.telephone);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final Myadapter adapter = new Myadapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}