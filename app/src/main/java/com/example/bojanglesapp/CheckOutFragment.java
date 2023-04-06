package com.example.bojanglesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bojanglesapp.databinding.FragmentCheckOutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;


public class CheckOutFragment extends Fragment {

    private static final String ARG_CART = "cart";
    private ShoppingCart sCart;
    double total;
    double tax;
    double subtotal;
    FragmentCheckOutBinding binding;
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    User user;

    public CheckOutFragment() {
        // Required empty public constructor
    }

    public static CheckOutFragment newInstance(ShoppingCart sCart) {
        CheckOutFragment fragment = new CheckOutFragment();
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
            this.total = sCart.getTotal();
            this.tax = sCart.getTax();
            this.subtotal = sCart.getSubtotal();
//            this.firebaseUser = mAuth.getCurrentUser();
//            this.user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckOutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle(R.string.check_out_label);

//        binding.accountNameTextViewAccount.setText(user.get());
//        DecimalFormat df = new DecimalFormat("#");
//        binding.textViewUserPoints.setText(df.format(user.get));


    }
}