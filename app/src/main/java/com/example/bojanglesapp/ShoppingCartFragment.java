package com.example.bojanglesapp;

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

import com.example.bojanglesapp.databinding.FragmentShoppingCartBinding;

import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment {

    FragmentShoppingCartBinding binding;
    private static final String ARG_CART = "cart";

    private ShoppingCart sCart;
    ShoppingCartRecyclerAdapter adapter;
    RecyclerView shoppingCartRecyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<MenuItem> mList;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance(ShoppingCart sCart) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CART, sCart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sCart = (ShoppingCart) getArguments().getSerializable(ARG_CART);
            mList = sCart.getCart();
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

        shoppingCartRecyclerView = view.findViewById(R.id.shoppingCartRecyclerView);
        shoppingCartRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        shoppingCartRecyclerView.setLayoutManager(layoutManager);
        adapter = new ShoppingCartRecyclerAdapter(mList);
        shoppingCartRecyclerView.setAdapter(adapter);
        binding.buttonCheckOut.setOnClickListener(v -> sListener.goToCheckOut());
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