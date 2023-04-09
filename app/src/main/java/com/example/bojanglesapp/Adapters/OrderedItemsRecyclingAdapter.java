// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.R;

import java.util.ArrayList;

public class OrderedItemsRecyclingAdapter extends RecyclerView.Adapter<OrderedItemsRecyclingAdapter.OrderedItemsItemHolder> {
    ArrayList<MenuItem> mList;
    LayoutInflater lInflater;

    public OrderedItemsRecyclingAdapter(Context layout, ArrayList<MenuItem> data) {
        this.mList = data;
        this.lInflater = LayoutInflater.from(layout);
    }

    @NonNull
    @Override
    public OrderedItemsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_items_row_item, parent, false);
        OrderedItemsItemHolder orderedItemItemHolder = new OrderedItemsItemHolder(view);
        return orderedItemItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemsItemHolder holder, int position) {
        MenuItem item = mList.get(position);
        holder.textViewOrderedItemName.setText(item.getItemName());
        holder.textviewOrderedItemPrice.setText(String.valueOf(item.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    public static class OrderedItemsItemHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderedItemName;
        TextView textviewOrderedItemPrice;
        View rootView;

        public OrderedItemsItemHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewOrderedItemName = itemView.findViewById(R.id.textViewOrderedItemName);
            textviewOrderedItemPrice = itemView.findViewById(R.id.textViewOrderedItemPrice);
        }
    }
}
