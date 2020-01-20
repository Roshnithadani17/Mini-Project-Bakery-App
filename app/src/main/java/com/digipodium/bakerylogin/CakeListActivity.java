package com.digipodium.bakerylogin;

import android.content.Intent;
import android.os.Bundle;

import com.digipodium.bakerylogin.Adapter.CakeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class CakeListActivity extends AppCompatActivity {

    private TextView textCake;
    private TextView textPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RecyclerView recyclerCake = findViewById(R.id.recyclerCake);
        recyclerCake.setLayoutManager(new LinearLayoutManager(this));
        textCake = findViewById(R.id.textCake);
        textPrice = findViewById(R.id.textPrice);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), AddCakeActivity.class);
                startActivity(myIntent);

            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cakes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> data = task.getResult().getDocuments();
                    // String name = data.get(0).getString("name");
                    CakeAdapter adapter = new CakeAdapter(CakeListActivity.this, data, R.layout.card);
                    recyclerCake.setAdapter(adapter);


                } else {
                    Snackbar.make(fab, "failed to load data", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(CakeListActivity.this, AdminActivity.class));
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

}
