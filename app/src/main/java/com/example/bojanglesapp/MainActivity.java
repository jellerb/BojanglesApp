// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MenuFragment.MenuListener, EditAccountFragment.EditAccountListener {

    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = getIntent().getParcelableExtra("user");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new MenuFragment())
                .commit();
    }

    @Override
    public void editAccount(String email, String password, String creditCard) {


        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment() )
                .commit();
    }
}