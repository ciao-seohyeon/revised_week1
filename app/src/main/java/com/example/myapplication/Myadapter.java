package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Myadapter extends FragmentPagerAdapter {
    int totalTabs;

    public Myadapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Telephone telephone = new Telephone();
                return telephone;
            case 1:
                GridImage gridImage = new GridImage();
                return gridImage;
//            case 2:
//                My_Telephone my_telephone = new My_Telephone();
//                return my_telephone;
            case 2:
                Memo memo = new Memo();
                return memo;
            default:
                return null;
        }
    }

        // counts total number of tabs
    @Override
    public int getCount() {
        return 3;
    }
}
