// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bojanglesapp.databinding.FragmentEditAccountBinding;
import com.example.bojanglesapp.databinding.FragmentViewAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ViewAccountFragment extends Fragment {

    FragmentViewAccountBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userPassword = "";
    String userEmail = "";
    String userPayment = "";
    double userPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle(R.string.my_account_label);

        DocumentReference docRef = firebaseFirestore.collection("Users").document(firebaseUser.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if(task.getResult().exists()){
                String nameResult = task.getResult().getString("displayName");
                String emailResult = task.getResult().getString("Email");
                String passwordResult = task.getResult().getString("Password");
                String paymentResult = task.getResult().getString("Payment");
                double pointsResult = task.getResult().getDouble("Points");
                DecimalFormat df = new DecimalFormat("#");

                binding.accountNameTextViewAccount.setText(nameResult);
                binding.textViewUserEmail.setText(emailResult);
                binding.textViewUserPassword.setText(passwordResult);
                binding.textViewUserPayment.setText(paymentResult);
                binding.textViewUserPoints.setText(df.format(pointsResult));
            } else {
                Log.d("whatever", "get failed with", task.getException());
            }
        });

        binding.buttonGoToEditAccount.setOnClickListener(v -> {
            mListener.goToEditAccount(userEmail, userPassword, userPayment, userPoints);
         } //end of the buttonClickListener
        );

        binding.buttonLogout.setOnClickListener(v -> {
                    mListener.logout();
                } //end of the buttonClickListener
        );
        }
    ViewAccountFragment.ViewAccountListener mListener;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ViewAccountFragment.ViewAccountListener) context;
    }
    interface ViewAccountListener {
        void goToEditAccount(String email, String password, String payment, double userPoints);
        void logout();
    }

}