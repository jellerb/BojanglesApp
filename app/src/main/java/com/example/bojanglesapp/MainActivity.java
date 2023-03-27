// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bojangles_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewAccountFragment:
                startActivity(new Intent(this, ViewAccountFragment.class));
                return true;
            case R.id.menuFragment:
                startActivity(new Intent(this, MenuFragment.class));
                return true;
//            case R.id.:
//                startActivity(new Intent(this, EditAccountFragment.class));
//                return true;
            //case logout
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void editAccount(String email, String password, String creditCard) {
        //logout user after editing account
        firebaseAuth.signOut();


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