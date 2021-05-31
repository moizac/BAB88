package com.example.bab8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class SignUp extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private AuthMethodPickerLayout authMethodPickerLayout;
    private List<AuthUI.IdpConfig> providers;

    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firebase_login)
                .setGoogleButtonId(R.id.button1)
                .setPhoneButtonId(R.id.button2)
                .build();

        toMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                cekData(user.getEmail());
            } else {
                Toast.makeText(this, "Terjadi kesalahan pada server, coba lagi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cekData(String email) {
        String userKey = email.replaceAll("[-+.^:,]","");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dataSnapshot = snapshot.child(userKey).getKey();

                assert dataSnapshot != null;
                if (dataSnapshot.equalsIgnoreCase(userKey)) {
                    startActivity(new Intent(SignUp.this, ProfileActivity.class));
                } else {
                    Intent intent = new Intent(SignUp.this, FormProfileActivity.class);
                    intent.putExtra("FIRST", true);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toMainActivity() {
        int loadingTime = 3000;
        new Handler().postDelayed(() -> {
            if (firebaseUser != null) {
                startActivity(new Intent(SignUp.this, FormProfileActivity.class));
                finish();
            } else {
                doSignIn();
            }
        }, loadingTime);
    }

    public void doSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .build(),
                RC_SIGN_IN);
    }
}