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

public class ViewAccountFragment extends Fragment {

    com.example.bojanglesapp.databinding.FragmentViewAccountBinding binding;
    String userPassword = "";
    String userEmail = "";

    String userPoints ="";
    int userPointsInt = 0;

    String userPayment = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();

                if (document.exists()){
                    Log.d("whatever", "Document Data: "+ document.getData());
                    userEmail = document.get("Email", String.class);
                    userPassword = document.get("Password", String.class);
                    userPayment = document.get("Credit Card", String.class);
                    userPoints = document.get("Rewards Points", String.class);

                    //userPointsInt =Integer.parseInt(userPoints);

                } else {
                    Log.d("whatever", "No such document");
                }
            } else {
                Log.d("whatever", "get failed with", task.getException());
            }
        });

        binding.accountNameTextViewAccount.setText(firebaseUser.getDisplayName());
        binding.textViewUserEmail.setText(userEmail);
        binding.textViewUserPassword.setText(userPassword);
        binding.textViewUserPayment.setText(userPayment);
        binding.textViewRewards.setText(userPoints + " Points");

        binding.buttonGoToEditAccount.setOnClickListener(v -> {
            mListener.goToEditAccount(userEmail, userPassword, userPayment);
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
        void goToEditAccount(String email, String password, String creditCard);
        void logout();
    }

}