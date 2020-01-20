package com.digipodium.bakerylogin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digipodium.bakerylogin.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.RowHolder> {
    private Context context;
    private List<DocumentSnapshot> data;
    private int layout;
    private LayoutInflater inflater;

    public CakeAdapter(Context context, List<DocumentSnapshot> data, int layout) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.layout = layout;
    }


    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(layout, parent, false);
        RowHolder h = new RowHolder(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        DocumentSnapshot snapshot = data.get(position);
        String name = snapshot.getString("name");
        String price = snapshot.getString("price");
        String imageUrl = snapshot.getString("url");
        holder.textCake.setText(name);
        holder.textPrice.setText(price);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RowHolder extends RecyclerView.ViewHolder {
        TextView textCake, textPrice;

        public RowHolder(@NonNull View itemView) {
            super(itemView);
            textCake = itemView.findViewById(R.id.textCake);
            textPrice = itemView.findViewById(R.id.textPrice);
        }
    }
}