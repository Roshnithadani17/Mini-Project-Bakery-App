package com.digipodium.bakerylogin.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.digipodium.bakerylogin.fragment.CakeFragment;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class CakeSliderAdapter extends FragmentStatePagerAdapter {

    private final List<DocumentSnapshot> data;

    public CakeSliderAdapter(FragmentManager fm, List<DocumentSnapshot> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        DocumentSnapshot item = data.get(position);
        float price = Float.parseFloat(item.getString("price"));
        String name = item.getString("name");
        String url = item.getString("url");
        return CakeFragment.newInstance(name, price, url);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        DocumentSnapshot item = data.get(position);
        String name = item.getString("name");
        return name;
    }
}
