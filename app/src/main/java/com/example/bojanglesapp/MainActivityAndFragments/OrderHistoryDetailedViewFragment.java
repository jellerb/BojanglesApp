// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp.MainActivityAndFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bojanglesapp.Adapters.OrderedItemsRecyclingAdapter;
import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.Objects.Order;
import com.example.bojanglesapp.Objects.ShoppingCart;
import com.example.bojanglesapp.R;
import com.example.bojanglesapp.databinding.FragmentOrderConfirmationBinding;
import com.example.bojanglesapp.databinding.FragmentOrderHistoryDetailedViewBinding;
import com.google.firebase.Timestamp;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderHistoryDetailedViewFragment extends Fragment {

    FragmentOrderHistoryDetailedViewBinding binding;
    private static final String ARG_ORDER = "order";
    private Order order;
    String customerName, customerEmail, customerPayment;
    double total, tax, subtotal, points;
    boolean favorite;
    Date orderedAt;
    ShoppingCart shoppingCart;
    OrderedItemsRecyclingAdapter adapter;
    RecyclerView orderedItemsRecyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<MenuItem> mList;

    public OrderHistoryDetailedViewFragment() {}

    public static OrderHistoryDetailedViewFragment newInstance(Order order) {
        OrderHistoryDetailedViewFragment fragment = new OrderHistoryDetailedViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.order = (Order) getArguments().getSerializable(ARG_ORDER);
            // get date and time order was placed
            Timestamp timestamp = order.getOrderedAt();
            this.orderedAt = timestamp.toDate();
            // get user information tied to the order
            this.customerName = order.getCustomerName();
            this.customerEmail = order.getCustomerEmail();
            this.customerPayment = order.getCustomerPayment();
            // get items ordered
            this.shoppingCart = order.getCart();
            // get money and point values from order
            this.total = shoppingCart.getTotal();
            this.tax = shoppingCart.getTax();
            this.subtotal = shoppingCart.getSubtotal();
            this.points = shoppingCart.getPoints();
            // pull the arraylist for the recycler view
            this.mList = shoppingCart.getCart();
            this.favorite = order.favoriteStatus();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderHistoryDetailedViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.order_history);

        // print the date and time of the order
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.getDefault());
        String dateFormatted = sdf.format(orderedAt);
        binding.textViewOrderedAt.setText(dateFormatted);

        // print the user information tied to the order
        binding.accountNameTextViewAccount.setText(customerName);
        binding.textViewUserEmail.setText(customerEmail);
        binding.textViewUserPayment.setText(customerPayment);
        binding.buttonFavoriteOrder.setOnClickListener(v -> {
            // if the order IS NOT a favorite, change the icon to filled when clicked
            if (!favorite) {
                binding.buttonFavoriteOrder.setBackgroundResource(R.drawable.heart_filled_red_foreground);
                // set the order as a favorite
                order.setFavorite(true);
                // if the order IS a favorite, change the icon to be unfilled when clicked
            } else {
                binding.buttonReturnToMenu.setBackgroundResource(R.drawable.heart_unfilled_foreground);
                // set the order as NOT a favorite
                order.setFavorite(false);
            }
            oListener.updateOrder(order);
        });

        // set money and point values from order
        DecimalFormat dfDecimal = new DecimalFormat("0.00");
        DecimalFormat dfNoDecimal = new DecimalFormat("#");
        dfNoDecimal.setRoundingMode(RoundingMode.DOWN);
        binding.textViewSubtotal.setText(dfDecimal.format(subtotal));
        binding.textViewTax.setText(dfDecimal.format(tax));
        binding.textViewTotal.setText(dfDecimal.format(total));
        binding.textViewUserPoints.setText(dfNoDecimal.format(points));

        // set up recycler view
        orderedItemsRecyclerView = view.findViewById(R.id.orderedItemsRecyclerView);
        orderedItemsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        orderedItemsRecyclerView.setLayoutManager(layoutManager);
        adapter = new OrderedItemsRecyclingAdapter(getActivity(), mList);
        orderedItemsRecyclerView.setAdapter(adapter);

        // if user clicks button, return to menu
        binding.buttonReturnToMenu.setOnClickListener(v -> oListener.goToOrderHistory());
    }

    OrderConfirmationListener oListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        oListener = (OrderConfirmationListener) context;
    }

    interface OrderConfirmationListener {
        void goToOrderHistory();
        void updateOrder(Order order);
    }
}