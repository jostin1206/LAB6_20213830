package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email, passwd;
    private Button btnLogin, btnRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        passwd = findViewById(R.id.passwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegistro.setOnClickListener(v -> registerUser());

    }
    private void loginUser() {
        String correo = email.getText().toString().trim();
        String contrasenia = passwd.getText().toString().trim();

        auth.signInWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        // toast
                        Toast.makeText(this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        //toast
                        Toast.makeText(this, "Error de inicio: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String correo = email.getText().toString().trim();
        String contrasenia = passwd.getText().toString().trim();

        auth.createUserWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //toast
                        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        //toast
                        Toast.makeText(this, "Error de registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}