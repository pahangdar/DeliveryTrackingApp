package com.example.deliverytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernamtEt);
        passwordEditText = findViewById(R.id.passwordTv);
        loginButton =findViewById(R.id.loginBt);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkUsername() && chkPassword()) {
                    tryLogin();
                }
            }
        });


    }


    private Boolean chkUsername() {
        String username = usernameEditText.getText().toString().trim();
        if(username.isEmpty()){
            usernameEditText.setError("Please enter username");
            return false;
        } else {
            return true;
        }
    }

    private Boolean chkPassword() {
        String password = passwordEditText.getText().toString();
        if(password.isEmpty()){
            passwordEditText.setError("Please enter password");
            return false;
        } else {
            return true;
        }
    }

    private void tryLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(username + "@deliverytracking.com", password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, get the user's ID
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Pass the user ID to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish(); // Close the LoginActivity
                        } else {
                            // Authentication failed
                            usernameEditText.setError("Invalid Credential ");
                            passwordEditText.setError("Invalid Credential ");
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}