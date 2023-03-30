// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MenuFragment.MenuListener, EditAccountFragment.EditAccountListener, ViewAccountFragment.ViewAccountListener, LoginFragment.LoginListener, CreateAccountFragment.CreateAccountListener {
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);
        nvDrawer.setHovered(true);
        nvDrawer.setItemIconSize(200);
        nvDrawer.setItemHorizontalPadding(0);
        nvDrawer.setItemIconPadding(0);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_dehaze_24);
        setupDrawerContent(nvDrawer);

        if (mAuth.getCurrentUser() == null) {
            // hide action bar
            getSupportActionBar().hide();
            // go to login page
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new LoginFragment())
                    .commit();
        } else {
            // set user info in nav menu
            View headerView = nvDrawer.getHeaderView(0);
            TextView userName = (TextView) headerView.findViewById(R.id.userName);
            userName.setText(currentUser.getDisplayName());
            TextView userEmail = (TextView) headerView.findViewById(R.id.userEmail);
            userEmail.setText(currentUser.getEmail());
            // show logout button
            Button button = (Button) headerView.findViewById(R.id.logoutButton);
            button.setOnClickListener(v -> logout());
            // go to menu page
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new MenuFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.food_menu:
                fragmentClass = MenuFragment.class;
                break;
            case R.id.my_account:
                fragmentClass = ViewAccountFragment.class;
                break;
            case R.id.edit_account:
                fragmentClass = EditAccountFragment.class;
                break;
            default:
                fragmentClass = MenuFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public void editAccount(String email, String password, String creditCard) {
        //logout user after editing account
        mAuth.signOut();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToEditAccount(String email, String password, String creditCard) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new EditAccountFragment())
                .commit();
    }

    @Override
    public void logout() {
        // sign out of firebase
        mAuth.signOut();
        // remove user information from nav menu
        View headerView = nvDrawer.getHeaderView(0);
        TextView userName = (TextView) headerView.findViewById(R.id.userName);
        userName.setText(null);
        TextView userEmail = (TextView) headerView.findViewById(R.id.userEmail);
        userEmail.setText(null);
        // close drawer menu
        mDrawer.close();
        // hide action bar / menu
        getSupportActionBar().hide();
        // go to login page
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment() )
                .commit();
    }

    @Override
    public void createAccount(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createTask -> {
            if (!createTask.isSuccessful()) {
                Exception exception = createTask.getException();
                assert exception != null;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            AuthResult result = createTask.getResult();
            currentUser = result.getUser();
            assert currentUser != null;

            currentUser.updateProfile(request).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Exception exception = task.getException();
                    assert exception != null;
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("An Error Occurred")
                            .setMessage(exception.getLocalizedMessage())
                            .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                            .show();
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("displayName", currentUser.getDisplayName());
                data.put("Email", email);
                data.put("Password", password);

                firebaseFirestore
                        .collection("Users")
                        .document(currentUser.getUid())
                        .set(data);

                goToMenu(currentUser);
            });
        });
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void authenticate(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception exception = task.getException();
                assert exception != null;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("An Error Occurred")
                        .setMessage(exception.getLocalizedMessage())
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            currentUser = task.getResult().getUser();

            goToMenu(currentUser);
        });
    }

    @Override
    public void goToSignUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateAccountFragment())
                .commit();
    }

    public void goToMenu(FirebaseUser firebaseUser) {
        firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getDisplayName());

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("user", firebaseUser);

        startActivity(intent);
        finish();
    }
}