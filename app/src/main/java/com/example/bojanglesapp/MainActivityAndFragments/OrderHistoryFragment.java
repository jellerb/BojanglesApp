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
import android.widget.Button;
import android.widget.TextView;

import com.example.bojanglesapp.Objects.MenuItem;
import com.example.bojanglesapp.Objects.Order;
import com.example.bojanglesapp.R;
import com.example.bojanglesapp.databinding.FragmentOrderHistoryBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderHistoryFragment extends Fragment {

    FragmentOrderHistoryBinding binding;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Order, OrderHistoryFragment.OrderHolder> adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.order_history);

        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("Orders")
                .orderBy("orderedAt", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, OrderHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull Order model) {
                holder.setOrderDateTime(model.getOrderedAt().toDate(), model);
                holder.setOrderTotal((model.getCart()).getTotal());
                holder.setOrderFavoriteStatus(model);
            }

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row_item, parent, false);
                return new OrderHolder(view);
            }
        };
        binding.ordersRecyclerView.setAdapter(adapter);
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        private final View view;
        Button buttonFavorite;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.buttonFavorite = view.findViewById(R.id.buttonFavorite);
        }

        void setOrderDateTime(Date date, Order order) {
            TextView textView = view.findViewById(R.id.textViewOrderDateTime);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.getDefault());
            String dateFormatted = sdf.format(date);
            textView.setText(dateFormatted);
            itemView.setOnClickListener(view -> oListener.seeOrderDetails(order));
        }

        void setOrderTotal(double total) {
            TextView textView = view.findViewById(R.id.textViewOrderTotal);
            DecimalFormat dfDecimal = new DecimalFormat("0.00");
            textView.setText(dfDecimal.format(total));
        }

        void setOrderFavoriteStatus(Order order) {
            buttonFavorite.setOnClickListener(view -> {
                // if the order IS NOT a favorite, change the icon to filled when clicked
                if (!order.favoriteStatus()) {
                    buttonFavorite.setBackgroundResource(R.drawable.heart_filled_red_foreground);
                    // set the order as a favorite
                    order.setFavorite(true);
                    // if the order IS a favorite, change the icon to be unfilled when clicked
                } else {
                    buttonFavorite.setBackgroundResource(R.drawable.heart_unfilled_foreground);
                    // set the order as NOT a favorite
                    order.setFavorite(false);
                }
                oListener.updateOrderHistory(order);
                adapter.notifyItemChanged(getLayoutPosition());
            });
        }
    }

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

    OrderHistoryListener oListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        oListener = (OrderHistoryListener) context;
    }

    public interface OrderHistoryListener {
        void seeOrderDetails(Order selectedOrder);
        void updateOrderHistory(Order order);
    }
}