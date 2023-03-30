// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bojanglesapp.databinding.FragmentEditAccountBinding;
import com.example.bojanglesapp.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAccountFragment extends Fragment {

    String currentPassword = "";
    String currentPayment = "";
    String currentEmail = "";
    FragmentEditAccountBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(R.string.edit_account_label);

        //GETTING THE USER DOCUMENT DATA!!!!!!!!!!!!!!!
        User user = new User();

        DocumentReference docRef = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   Log.d("whatever", "Document Data: "+ document.getData());

                   currentEmail = document.get("Email").toString();
                   currentPassword = document.get("Password").toString();
                   currentPayment = document.get("Payment").toString();


                   //setting the user to the CURRENT data from firestore

                   user.setEmail(document.get("Email").toString());
                   user.setName(firebaseUser.getDisplayName());
                   user.setPayment(document.get("Credit Card").toString());
                   user.setPassword(document.get("Password").toString());


               } else {
                   Log.d("whatever", "No such document");
               }
           } else {
               Log.d("whatever", "get failed with", task.getException());
           }
        });


        binding.accountNameTextView.setText("Edit Account " + user.getName());
        binding.buttonSubmitEdit.setOnClickListener(v -> {
            String email = binding.editTextAccountEmail.getText().toString();
            String password = binding.editTextAccountPassword.getText().toString();
            String creditCard = binding.editTextAccountPayment.getText().toString();

            if(email.isEmpty() || password.isEmpty() || creditCard.isEmpty()){
                Toast.makeText(getContext(), R.string.empty_prompt, Toast.LENGTH_SHORT).show();

            } else {
                user.setEmail(email);
                user.setPassword(password);
                user.setPayment(creditCard);

                mListener.editAccount(user);
            }
        });
        binding.buttonLogout.setOnClickListener(v -> mListener.logout());

        requireActivity().setTitle(R.string.login_label);
    }

    EditAccountFragment.EditAccountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (EditAccountListener) context;
    }
    interface EditAccountListener {
        void editAccount(User user);
        void logout();
    }
}