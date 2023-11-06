package com.coffeeprogrammer.yummisto.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coffeeprogrammer.yummisto.CakeDetailActivity;
import com.coffeeprogrammer.yummisto.R;
import com.coffeeprogrammer.yummisto.models.Cake;

import java.util.List;

public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.CakeViewHolder> {
    private List<Cake> cakes;

    public void setCakes(List<Cake> cakes) {
        this.cakes = cakes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cake, parent, false);
        return new CakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeViewHolder holder, int position) {
        Cake cake = cakes.get(position);
        // Bind cake data to the views
        holder.cakeNameTextView.setText(cake.getName());

        holder.cakePriceTextView.setText("Price: LKR" + cake.getPrice());
        holder.cakeWeightTextView.setText("Weight: " + cake.getWeight() + " kg");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here
                Intent intent = new Intent(holder.itemView.getContext(), CakeDetailActivity.class);
                intent.putExtra("cake_id", cake.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        // Load cake image using Glide
        Glide.with(holder.cakeImage.getContext()).load(cake.getImageUrl()).into(holder.cakeImage);
    }


    @Override
    public int getItemCount() {
        return cakes != null ? cakes.size() : 0;
    }

    public static class CakeViewHolder extends RecyclerView.ViewHolder {
        ImageView cakeImage;
        TextView cakeNameTextView;
        TextView cakePriceTextView;
        TextView cakeWeightTextView;

        public CakeViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeImage = itemView.findViewById(R.id.cakeImage);
            cakeNameTextView = itemView.findViewById(R.id.cakeNameTextView);
            cakePriceTextView = itemView.findViewById(R.id.cakePriceTextView);
            cakeWeightTextView = itemView.findViewById(R.id.cakeWeightTextView);
        }
    }
}
