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
            case 2:
                FreeTab freeTab = new FreeTab();
                return freeTab;
            case 3:
                My_GridImage my_gridImage = new My_GridImage();
                return my_gridImage;
            case 4:
                My_Telephone my_telephone = new My_Telephone();
                return my_telephone;
            default:
                return null;
        }
    }

        // counts total number of tabs
    @Override
    public int getCount() {
        return 5;
    }
}
