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

import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.R;
import com.example.bojanglesapp.Objects.ShoppingCart;
import com.example.bojanglesapp.Adapters.ShoppingCartRecyclerAdapter;
import com.example.bojanglesapp.databinding.FragmentShoppingCartBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment implements ShoppingCartRecyclerAdapter.iShoppingCart {

    FragmentShoppingCartBinding binding;
    private static final String ARG_CART = "cart";
    ShoppingCart sCart;
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

            System.out.println("Entering Shopping Cart.");
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
        adapter = new ShoppingCartRecyclerAdapter(getActivity(), mList, this);
        shoppingCartRecyclerView.setAdapter(adapter);

        sCart.setCart(mList);
        sCart.getSubtotal();
        sCart.getTax();
        sCart.getTotal();
        sCart.getPoints();
        updateView();
        //build sCart using mList?
        System.out.println("Contents of sCart (shoppingCart): " + sCart);
        System.out.println("SCF - Price Total: " + sCart.getTotal());

        binding.buttonCheckOut.setOnClickListener(v -> sListener.goToCheckOut(sCart));
    }

    ShoppingCartListener sListener;

    private void updateView() {
        DecimalFormat df = new DecimalFormat("0.00");

        binding.textViewSubtotal.setText(df.format(sCart.getSubtotal()));
        binding.textViewTax.setText(df.format(sCart.getTax()));
        binding.textViewTotal.setText(df.format(sCart.getTotal()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sListener = (ShoppingCartListener) context;
    }

    @Override
    public void deleteButtonClicked(int chosenMenuItem) {
        mList.remove(chosenMenuItem);

        if ((mList.isEmpty())) {
            sListener.emptyCartReturnToMenu(mList);
            //set text on shopping cart page to 0
        } else {
            adapter.notifyItemRemoved(chosenMenuItem);
            updateView();
        }
    }

    interface ShoppingCartListener {
        void goToCheckOut(ShoppingCart sCart);
        void emptyCartReturnToMenu(ArrayList<MenuItem> mList);
    }
}