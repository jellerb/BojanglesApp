// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp.MainActivityAndFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.R;
import com.example.bojanglesapp.Objects.ShoppingCart;
import com.example.bojanglesapp.databinding.FragmentMenuBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    FragmentMenuBinding binding;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<MenuItem, MenuItemHolder> adapter;
    ArrayList<MenuItem> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.menu_label);

        binding.menuItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firebaseFirestore
                .collection("MenuV2")
                .orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>()
                .setQuery(query, MenuItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MenuItem, MenuItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuItemHolder holder, int position, @NonNull MenuItem model) {
                holder.setItemName(model.getItemName(), model.getItemPrice(), model.getIngredients(), model.getCalories());
                holder.setItemPrice((model.getItemPrice()));
            }

            @NonNull
            @Override
            public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_row_item, parent, false);
                return new MenuItemHolder(view);
            }
        };

        binding.menuItemsRecyclerView.setAdapter(adapter);
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private final View view;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        void setItemName(String name, double price, String ingredients, int calories) {
            TextView textView = view.findViewById(R.id.textViewMenuItemName);
            textView.setText(name);
            // allow user to tap an item to view it directly in the Menu Item fragment
            itemView.setOnClickListener(view ->
                    mListener.goToMenuItem(name, price, ingredients, calories));
        }

        void setItemPrice(double price) {
            TextView textView = view.findViewById(R.id.textViewMenuItemPrice);
            textView.setText(String.valueOf(price));
        }
    }
    MenuListener mListener;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void updateShoppingCart(ArrayList<MenuItem> mList) {
        this.mList = mList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MenuListener) context;
    }

    interface MenuListener {
        void logout();
        void goToMenuItem(String name, double price, String ingredients, int calories);
    }
}