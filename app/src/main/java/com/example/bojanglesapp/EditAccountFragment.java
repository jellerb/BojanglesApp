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


    FragmentEditAccountBinding binding;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public EditAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.navigation_menu, menu);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle("Edit Account");

        //GETTING THE USER DOCUMENT DATA!!!!!!!!!!!!!!!
        DocumentReference docRef = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   Log.d("whatever", "Document Data: "+ document.getData());
                   currentPassword = document.get("Password").toString();
                   currentPayment = document.get("Credit Card").toString();

               } else {
                   Log.d("whatever", "No such document");
               }
           } else {
               Log.d("whatever", "get failed with", task.getException());
           }
        });

        binding.accountNameTextView.setText(R.string.edit_account_label + " " + firebaseUser.getDisplayName());
        binding.buttonSubmitEdit.setOnClickListener(v -> {
            String email = binding.editTextAccountEmail.getText().toString();
            String password = binding.editTextAccountPassword.getText().toString();
            String creditCard = binding.editTextAccountPayment.getText().toString();

            if(email.isEmpty()){
                Toast.makeText(getContext(), R.string.email_empty_prompt, Toast.LENGTH_SHORT).show();
                //get current email from firestore
                email = firebaseUser.getEmail();

            } else if(password.isEmpty()){
                Toast.makeText(getContext(), R.string.password_empty_prompt, Toast.LENGTH_SHORT).show();
                //get current password from firestore
                password = currentPassword;

            } else if(creditCard.isEmpty()){
                Toast.makeText(getContext(), R.string.payment_empty_prompt, Toast.LENGTH_SHORT).show();
                //get current payment from firestore
                creditCard = currentPayment;


            }else {
                mListener.editAccount(email, password, creditCard);
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
        void editAccount(String email, String password, String creditCard);
        void logout();
    }
}