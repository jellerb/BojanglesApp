// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = getIntent().getParcelableExtra("user");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

//    @Override
//    public void logout() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("online", false);
//
//        // To keep things responsive, we intentionally ignore the response.
//        firebaseFirestore
//                .collection("Users")
//                .document(currentUser.getUserId())
//                .update(data);
//
//        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
//        startActivity(intent);
//        finish();
//    }


}