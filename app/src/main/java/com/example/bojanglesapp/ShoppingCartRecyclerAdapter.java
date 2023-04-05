package com.example.bojanglesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShoppingCartRecyclerAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerAdapter.ShoppingCartItemHolder> {
    ArrayList<MenuItem> mList;

    public ShoppingCartRecyclerAdapter(ArrayList<MenuItem> data) {
        this.mList = data;
    }

    @NonNull
    @Override
    public ShoppingCartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_row_item, parent, false);
        ShoppingCartItemHolder cartItemHolder = new ShoppingCartItemHolder(view);
        return cartItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartItemHolder holder, int position) {
        MenuItem item = mList.get(position);
        holder.textViewMenuItemName.setText(item.getItemName());
        holder.textviewMenuItemPrice.setText(String.valueOf(item.getItemPrice()));
        holder.buttonRemoveItem.setOnClickListener(v -> {
            mList.remove(item);
            // decrement total
            // refresh fragment
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public void refreshCart(){

    }

    public static class ShoppingCartItemHolder extends RecyclerView.ViewHolder {
        TextView textViewMenuItemName;
        TextView textviewMenuItemPrice;
        View rootView;
        Button buttonRemoveItem;

        public ShoppingCartItemHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewMenuItemName = itemView.findViewById(R.id.textViewMenuItemName);
            textviewMenuItemPrice = itemView.findViewById(R.id.textViewMenuItemPrice);
            buttonRemoveItem = itemView.findViewById(R.id.buttonRemoveItem);
        }
    }
}
