package com.example.bookMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    // declare some variables
    private EditText finputName, sinputName, inputEmail, inputPassword, inputConfirmPassword;
    private Button registerBtn, backToLoginBTN;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialise them
        finputName = findViewById(R.id.fnameText);
        sinputName = findViewById(R.id.snameText);
        inputEmail = findViewById(R.id.emailText);
        inputPassword = findViewById(R.id.enterPasswordText);
        inputConfirmPassword = findViewById(R.id.repeatPasswordText);
        registerBtn = findViewById(R.id.bookBtn);
        backToLoginBTN = findViewById(R.id.loginBTN);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //register button action listener
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
            //authentication method here.

            private void PerformAuth() {
                String firstName = finputName.getText().toString();
                String secondName = sinputName.getText().toString();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();
                // some validation of user details
                if (firstName.isEmpty()) {
                    finputName.setError("Enter a name !");

                }
                if (secondName.isEmpty()) {
                    sinputName.setError("Enter a second name !");

                }
                if (!email.matches(emailPattern)) {
                    inputEmail.setError("Enter a proper email!");
                } else if (password.isEmpty()) {
                    inputPassword.setError("Password cannot be empty!");
                } else if (password.length() < 6) {
                    inputPassword.setError("Password cannot be less than 6 characters ");
                } else if (!password.equals(confirmPassword)) {
                    inputConfirmPassword.setError("Passwords must match!");
                } else {
                    progressDialog.setMessage("Please wait while it's registering....");
                    progressDialog.setTitle("Registering");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(firstName, secondName, email);
                                // add the User object just created into our Realtime database under " Users "
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //if task is okay then go to next activity which is login page
                                            progressDialog.dismiss();
                                            nextActivity();
                                            Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            } // END OF REGISTER METHOD
        });

        backToLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });

    }

    // go to the login page once registered successfully
    private void nextActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}