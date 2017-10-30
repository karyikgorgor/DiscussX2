package com.fyp.discussx.ui.activities.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fyp.discussx.ui.activities.fragments.GroupListFragment;
import com.fyp.discussx.ui.activities.fragments.HomeFragment;
import com.fyp.discussx.ui.activities.fragments.ProfilePageFragment;




public class TabAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                GroupListFragment groupListFragment = new GroupListFragment();
                return groupListFragment;
            case 2:
                ProfilePageFragment profilePageFragment = new ProfilePageFragment();
                return profilePageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
