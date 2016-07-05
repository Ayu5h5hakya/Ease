package com.example.ayush.slider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Ayush on 7/2/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragmentslist;
    ArrayList<String> titles;

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
        fragmentslist = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void addFragments(Fragment newFragment,String title){
        this.fragmentslist.add(newFragment);
        this.titles.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentslist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentslist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
