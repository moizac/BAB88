package com.example.bab8;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText email = findViewById(R.id.ET_Email);
        EditText password = findViewById(R.id.ET_Password);
        Button login = findViewById(R.id.BT_Login);
        Button register = findViewById(R.id.BT_Register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Memail = email.getText().toString().trim();
                String Mpassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(Memail)){
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(Mpassword)){
                    password.setError("Password is Required");
                    return;
                }

                if (password.length() < 6){
                    password.setError("Password must be at least 6 Character");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(Memail, Mpassword).addOnCanceledListener(new OnCompleteListener<AuthResult>()){
                    @Override
                    public void onSuccess(Void Void) {
                        Toast.makeText(MainActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
}