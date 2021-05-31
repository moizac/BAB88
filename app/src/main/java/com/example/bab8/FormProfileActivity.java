package com.example.bab8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormProfileActivity extends AppCompatActivity {
    private EditText nim, nama, kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_profile);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        nim = findViewById(R.id.nim);
        nama = findViewById(R.id.nama);
        kelas = findViewById(R.id.kelas);
        MaterialButton simpan = findViewById(R.id.simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekData()) {
                    String userKey = firebaseUser.getEmail().replaceAll("[-+.^:,]","");

                    MahasiswaModel model = new MahasiswaModel(
                            nim.getText().toString(),
                            nama.getText().toString(),
                            kelas.getText().toString()
                    );

                    databaseReference.child(userKey).setValue(model);
                }
            }
        });
    }

    public boolean cekData() {
        boolean cek1 = true;
        boolean cek2 = true;
        boolean cek3 = true;

        if (nim.getText().toString().isEmpty()) {
            nim.setError("Mohon isi data berikut");
            cek1 = false;
        }

        if (nama.getText().toString().isEmpty()) {
            nama.setError("Mohon isi data berikut");
            cek2 = false;
        }

        if (kelas.getText().toString().isEmpty()) {
            kelas.setError("Mohon isi data berikut");
            cek3 = false;
        }

        return cek1 && cek2 && cek3;
    }
}