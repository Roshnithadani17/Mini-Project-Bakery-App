package com.digipodium.bakerylogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.digipodium.bakerylogin.Adapter.CakeSliderAdapter;
import com.digipodium.bakerylogin.Adapter.ZoomOutPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Bakery items");
        dialog.show();
        db.collection("cakes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> data = task.getResult().getDocuments();
                    displayPager(data);
                } else {
                    Snackbar.make(toolbar, "failed to load data", Snackbar.LENGTH_LONG).show();
                }
                if (dialog.isShowing()) {
                    dialog.hide();
                }
            }
        });
    }

    private void displayPager(List<DocumentSnapshot> data) {
        ViewPager pager=findViewById(R.id.pager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        pager.setPageTransformer(true,new ZoomOutPageTransformer());
        pager.setAdapter(new CakeSliderAdapter(getSupportFragmentManager(),data));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            startActivity(new Intent(this, AdminActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
