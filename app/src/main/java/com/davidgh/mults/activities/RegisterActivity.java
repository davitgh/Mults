package com.davidgh.mults.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // Android Layout
    private Button btnRegister;
    private EditText mName, mEmail, mPassword, mCpassword;

    // Firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initLayout();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
/*                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();*/
                overridePendingTransition(R.anim.from_right, R.anim.from_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    private void initLayout() {
        // Firebase;
        this.mAuth = FirebaseAuth.getInstance();

        this.mName = (EditText) findViewById(R.id.et_name);
        this.mEmail = (EditText) findViewById(R.id.et_email);
        this.mPassword = (EditText) findViewById(R.id.et_password);
        this.mCpassword = (EditText) findViewById(R.id.et_cpassword);
    }

    private void registerUser() {
        final String name = mName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String cpassword = mCpassword.getText().toString().trim();

        // TODO 1: Register in a secure way

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, String> u = new HashMap<>();
                            u.put("username", name);
                            u.put("email", email);
                            u.put("image", "default");

                            mDatabase.child("Users").child(user.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this, "Hello" + name, Toast.LENGTH_SHORT).show();
                                }
                            });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(Object o) {
        // TODO 2: Update ui in a correct way, assume if (o == null)!

        if (null != o){
            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
