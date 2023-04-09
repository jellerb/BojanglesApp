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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.databinding.FragmentMenuItemBinding;

public class MenuItemFragment extends Fragment {

    FragmentMenuItemBinding binding;
    private static final String ARG_ITEM = "item";

    private MenuItem item;

    public MenuItemFragment() {
        // Required empty public constructor
    }

    public static MenuItemFragment newInstance(MenuItem item) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);

        MenuItemFragment fragment = new MenuItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.item = (MenuItem) getArguments().getSerializable(ARG_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(item.getItemName());

        binding.textViewItemName.setText(item.getItemName());
        binding.textViewPrice.setText(String.valueOf(item.getItemPrice()));
        binding.textViewIngredients.setText(item.getIngredients());
        binding.textViewCalories.setText(String.valueOf(item.getCalories()));
        binding.buttonAddToCart.setOnClickListener(v -> {

            mListener.addToCart(item);
            mListener.goToMenu();
        });
    }

    MenuItemListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (MenuItemListener) context;
    }

    interface MenuItemListener {
        void addToCart(MenuItem item);
        void goToMenu();
    }
}