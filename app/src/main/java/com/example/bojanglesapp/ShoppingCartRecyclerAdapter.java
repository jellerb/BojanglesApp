package com.example.bojanglesapp;

import android.content.Context;
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
    LayoutInflater lInflater;
    iShoppingCart iListener;

    public ShoppingCartRecyclerAdapter(Context layout, ArrayList<MenuItem> data, iShoppingCart iListener) {
        this.mList = data;
        this.lInflater = LayoutInflater.from(layout);
        this.iListener = iListener;
    }

    @NonNull
    @Override
    public ShoppingCartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_row_item, parent, false);
        ShoppingCartItemHolder cartItemHolder = new ShoppingCartItemHolder(view, iListener);
        return cartItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartItemHolder holder, int position) {
        MenuItem item = mList.get(position);
        holder.textViewMenuItemName.setText(item.getItemName());
        holder.textviewMenuItemPrice.setText(String.valueOf(item.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public static class ShoppingCartItemHolder extends RecyclerView.ViewHolder {
        TextView textViewMenuItemName;
        TextView textviewMenuItemPrice;
        View rootView;
        iShoppingCart iListener;

        public ShoppingCartItemHolder(@NonNull View itemView, iShoppingCart iListener) {
            super(itemView);
            rootView = itemView;
            this.iListener = iListener;
            textViewMenuItemName = itemView.findViewById(R.id.textViewMenuItemName);
            textviewMenuItemPrice = itemView.findViewById(R.id.textViewMenuItemPrice);
            // when user selects the (-) button, call method from ShoppingCartFragment
            itemView.findViewById(R.id.buttonRemoveItem).setOnClickListener(v -> {
                int chosenMenuItem = getLayoutPosition();
                iListener.deleteButtonClicked(chosenMenuItem);
            });
        }
    }

    public interface iShoppingCart {
        void deleteButtonClicked(int chosenMenuItem);
    }
}
