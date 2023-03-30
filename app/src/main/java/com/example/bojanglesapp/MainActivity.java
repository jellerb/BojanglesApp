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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new MenuFragment())
                    .commit();
        }
    }

    @Override
    public void editAccount(User user) {
        firebaseFirestore.collection("Users")
                        .document(user.getU_id())
                                .set(user)
                                        .addOnCompleteListener(task -> {
                                            if(!task.isSuccessful()){
                                               Exception exception = task.getException();
                                               assert exception != null;
                                               new AlertDialog.Builder(MainActivity.this)
                                                       .setTitle("Error")
                                                       .setMessage(exception.getLocalizedMessage())
                                                       .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                                                       .show();
                                               return;
                                            }
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.rootView, new LoginFragment())
                                                    .addToBackStack(null)
                                                    .commit();
                                        });

    }

    @Override
    public void goToEditAccount(String email, String password, String creditCard) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new EditAccountFragment())
                .commit();
    }


    @Override
    public void logout() {
        mAuth.signOut();

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