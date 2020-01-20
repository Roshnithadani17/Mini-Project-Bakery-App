package com.digipodium.bakerylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PurchaseActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText editAddress;
    private EditText editInfo;
    private Button btnConfirm;
    private TextView textName;
    private TextView textPrice;
    private TextView textView;
    private ImageView imgCake;
    String[] qty= {"1", "2", "3", "4", "5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        spinner = findViewById(R.id.spinner);
        editAddress = findViewById(R.id.editAddress);
        editInfo = findViewById(R.id.editInfo);
        btnConfirm = findViewById(R.id.btnConfirm);
        textName = findViewById(R.id.textName);
        textPrice = findViewById(R.id.textPrice);
        imgCake = findViewById(R.id.imgCake);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, qty);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Intent i = getIntent();
        if (i.hasExtra("name")) {
            textName.setText(i.getStringExtra("name"));
        }
        if (i.hasExtra("price")) {
            textPrice.setText((i.getStringExtra("price")));
        }
        if (i.hasExtra("url")) {
            Glide.with(this).load(i.getStringExtra("url")).into(imgCake);
        }

    }

    public void onButtonClick2(View v) {
        String address=editAddress.getText().toString();
        if(address.length()>0) {
            Intent myIntent = new Intent(getBaseContext(), LastActivity.class);
            startActivity(myIntent);
        }
        else{
            editAddress.setError("please enter the delivery address");
        }
    }
}
