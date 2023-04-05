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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MenuFragment.MenuListener, EditAccountFragment.EditAccountListener, ViewAccountFragment.ViewAccountListener, LoginFragment.LoginListener, CreateAccountFragment.CreateAccountListener, MenuItemFragment.MenuItemListener, ShoppingCartFragment.ShoppingCartListener {
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;
    ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button toolBarButton = findViewById(R.id.buttonToolbarShoppingCart);
        toolBarButton.setOnClickListener(v -> goToShoppingCart(shoppingCart));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = findViewById(R.id.drawer_layout);

        nvDrawer = findViewById(R.id.nvView);
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
            TextView userName = headerView.findViewById(R.id.userName);
            userName.setText(currentUser.getDisplayName());
            TextView userEmail = headerView.findViewById(R.id.userEmail);
            userEmail.setText(currentUser.getEmail());
            // show logout button
            Button button = headerView.findViewById(R.id.logoutButton);
            button.setOnClickListener(v -> logout());

            //Make new shopping cart - function
            shoppingCart = new ShoppingCart();

            // go to menu page
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent, MenuFragment.newInstance(shoppingCart))
                    .addToBackStack(null)
                    .commit();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.flContent, new MenuFragment())
//                    .commit();
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
            case R.id.shopping_cart:
                fragmentClass = ShoppingCartFragment.class;
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
    public void goToViewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new ViewAccountFragment())
                .commit();
    }

    @Override
    public void editAccount() {
//        firebaseFirestore.collection("Users")
//                        .document(user.getU_id())
//                                .set(user)
//                                        .addOnCompleteListener(task -> {
//                                            if(!task.isSuccessful()){
//                                               Exception exception = task.getException();
//                                               assert exception != null;
//                                               new AlertDialog.Builder(MainActivity.this)
//                                                       .setTitle("Error")
//                                                       .setMessage(exception.getLocalizedMessage())
//                                                       .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
//                                                       .show();
//                                               return;
//                                            }
//                                            getSupportFragmentManager().beginTransaction()
//                                                    .replace(R.id.flContent, new LoginFragment())
//                                                    .addToBackStack(null)
//                                                    .commit();
//                                        });

    }

    @Override
    public void goToEditAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new EditAccountFragment())
                .commit();
    }

    @Override
    public void logout() {
        // sign out of firebase
        mAuth.signOut();
        // remove user information from nav menu
        View headerView = nvDrawer.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.userName);
        userName.setText(null);
        TextView userEmail = headerView.findViewById(R.id.userEmail);
        userEmail.setText(null);
        // close drawer menu
        mDrawer.close();
        // hide action bar / menu
        getSupportActionBar().hide();
        // go to login page
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment() )
                .commit();
    }

    @Override
    public void goToMenuItem(String name, double price, String ingredients, int calories) {
        com.example.bojanglesapp.MenuItem menuItem = new com.example.bojanglesapp.MenuItem(
                name, price, ingredients, calories
        );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, MenuItemFragment.newInstance(menuItem))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createAccount(String name, String email, String password, String payment, int points) {
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
                data.put("Payment", payment);
                data.put("Points", points);

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
                .replace(R.id.flContent, new LoginFragment())
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
                .replace(R.id.flContent, new CreateAccountFragment())
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

    public void goToShoppingCart(ShoppingCart shoppingCart){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, ShoppingCartFragment.newInstance(shoppingCart))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addToCart(com.example.bojanglesapp.MenuItem item) {
        shoppingCart.addItem(item);
        System.out.println(shoppingCart.getCartSize());
        System.out.println(shoppingCart.getCart());
    }

    @Override
    public void goToMenu() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flContent, new MenuFragment())
                .commit();
    }

    @Override
    public void goToCheckOut() {

    }
}

