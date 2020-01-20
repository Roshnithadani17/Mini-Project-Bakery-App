package com.digipodium.bakerylogin.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digipodium.bakerylogin.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CakeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CakeFragment extends Fragment {
    private static final String CAKE_NAME = "name";
    private static final String CAKE_URL = "url";
    private static final String CAKE_PRICE = "price";
    private String cakeName;
    private String cakeUrl;
    private float cakePrice;
    private TextView textName, textPrice;
    private ImageView img;

    public CakeFragment() {
        // Required empty public constructor
    }

    public static CakeFragment newInstance(String name, float price, String url) {
        CakeFragment fragment = new CakeFragment();
        Bundle args = new Bundle();
        args.putString(CAKE_NAME, name);
        args.putFloat(CAKE_PRICE, price);
        args.putString(CAKE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cakeName = getArguments().getString(CAKE_NAME);
            cakeUrl = getArguments().getString(CAKE_URL);
            cakePrice = getArguments().getFloat(CAKE_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cake, container, false);
        textName = v.findViewById(R.id.textName);
        img = v.findViewById(R.id.img);
        textPrice = v.findViewById(R.id.textPrice);
        updateUI();
        return v;
    }

    private void updateUI() {
        textName.setText(cakeName);
        textPrice.setText(String.valueOf(cakePrice));
        Glide.with(getActivity()).load(cakeUrl).centerInside().into(img);
    }


}
