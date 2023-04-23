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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bojanglesapp.Objects.Order;
import com.example.bojanglesapp.R;
import com.example.bojanglesapp.Objects.ShoppingCart;
import com.example.bojanglesapp.databinding.FragmentCheckOutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class CheckOutFragment extends Fragment {

    private static final String ARG_CART = "cart";
    ShoppingCart sCart;
    double total, tax, subtotal, points;
    FragmentCheckOutBinding binding;
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();
    DocumentReference docRef;
    Order order = new Order();
    double userPoints;

    public CheckOutFragment() {}

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
            this.points = sCart.getPoints();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckOutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.check_out_label);
        // get user info to display on the screen
        docRef = firebaseFirestore.collection("Users").document(firebaseUser.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if(task.getResult().exists()){
                String nameResult = task.getResult().getString("displayName");
                String emailResult = task.getResult().getString("Email");
                String paymentResult = task.getResult().getString("Payment");
                double pointsResult = task.getResult().getDouble("Points");

                binding.accountNameTextViewAccount.setText(nameResult);
                binding.textViewUserEmail.setText(emailResult);
                binding.textViewUserPayment.setText(paymentResult);

                order.setCustomerName(nameResult);
                order.setCustomerEmail(emailResult);
                order.setCustomerPayment(paymentResult);
                order.setCart(sCart);
                order.setPointsGained(points);
                userPoints = pointsResult;
            } else {
                Log.d("whatever", "get failed with", task.getException());
            }
        });

        DecimalFormat dfDecimal = new DecimalFormat("0.00");
        DecimalFormat dfNoDecimal = new DecimalFormat("#");
        dfNoDecimal.setRoundingMode(RoundingMode.DOWN);

        binding.textViewSubtotal.setText(dfDecimal.format(subtotal));
        binding.textViewTax.setText(dfDecimal.format(tax));
        binding.textViewTotal.setText(dfDecimal.format(total));

        binding.textViewUserPoints.setText(dfNoDecimal.format(points));
        // Checkout Button
        binding.buttonCheckOut.setOnClickListener(v -> {
            double newPoints = userPoints + order.getPointsGained();
            order.setOrderedAt();
            cListener.placeOrder(order);
            cListener.updateUserPoints(newPoints);
        });
    }

    CheckOutListener cListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cListener = (CheckOutListener) context;
    }

    interface CheckOutListener {
        void placeOrder(Order order);
        void updateUserPoints(double points);
    }
}