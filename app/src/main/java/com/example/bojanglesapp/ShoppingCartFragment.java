package com.example.bojanglesapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bojanglesapp.databinding.FragmentShoppingCartBinding;

public class ShoppingCartFragment extends Fragment {

    FragmentShoppingCartBinding binding;
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance(String param1, String param2) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle(R.string.shopping_cart_label);

    }

    ShoppingCartListener sListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sListener = (ShoppingCartListener) context;
    }

    interface ShoppingCartListener {
        void goToCheckOut();
    }
}