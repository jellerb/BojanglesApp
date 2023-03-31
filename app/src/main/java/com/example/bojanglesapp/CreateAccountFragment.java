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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bojanglesapp.databinding.FragmentCreateAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountFragment extends Fragment {

    FragmentCreateAccountBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(v -> mListener.goToLogin());

        binding.buttonSignup.setOnClickListener(v -> {
            String name = binding.editTextCreateName.getText().toString();
            String email = binding.editTextCreateEmail.getText().toString();
            String password = binding.editTextCreatePassword.getText().toString();
            String payment = binding.editTextCreatePayment.getText().toString();
            int points = 0;

            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            } else if (name.isEmpty()) {
                Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
            } else if (payment.isEmpty()) {
                Toast.makeText(getContext(), "Payment is required", Toast.LENGTH_SHORT).show();
            } else {
                mListener.createAccount(name, email, password, payment, points);
            }
        });

        requireActivity().setTitle(R.string.sign_up_label);
    }

    CreateAccountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateAccountListener) context;
    }

    interface CreateAccountListener {
        void createAccount(String name, String email, String password, String payment, int points);
        void goToLogin();
    }
}