package com.davidgh.mults.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidgh.mults.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Firebase
    private FirebaseAuth mAuth;

    // Layouts
    private RelativeLayout topContent;
    private LinearLayout bottomContent;

    // Android Layout
    private EditText mEmail, mPassword;
    private Button mBtnLogin, mBtnCreate;
    private Button mBtnGoogle, mBtnFb, mBtnTw;

    // Animations
    private Animation fromTop;
    private Animation fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
        loadAnimation();
        loadClickListeners();
        // attamptLogin();
    }

    private void initLayout(){

        mAuth = FirebaseAuth.getInstance();

        // Layouts
        topContent = (RelativeLayout) findViewById(R.id.rv_top_content);
        bottomContent = (LinearLayout) findViewById(R.id.ll_bottom_content);

        // Buttons
        mBtnCreate = (Button) findViewById(R.id.btn_create);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

        mBtnGoogle = (Button) findViewById(R.id.btn_google);
        mBtnFb = (Button) findViewById(R.id.btn_fb);
        mBtnTw = (Button) findViewById(R.id.btn_tw);

        // Edit Text
        mEmail = (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
    }

    private void loadAnimation(){
        fromTop = AnimationUtils.loadAnimation(this, R.anim.from_top);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        topContent.setAnimation(fromTop);
        bottomContent.setAnimation(fromBottom);
    }

    private void loadClickListeners(){
        // TODO 2: Initialize Click Listeners
        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.from_right, R.anim.from_left);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            // TODO 3: Create Login Function with details and progress layout
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        mBtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                Log.e(TAG, "onClick: " + getString(R.string.default_web_client_id));

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                Intent googleIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(googleIntent, 10); // TODO; replace 10 to RC_SIGN_IN constant
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (null != user){
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();


                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, String> u = new HashMap<>();
                            u.put("username", acct.getDisplayName());
                            u.put("email", acct.getEmail());
                            u.put("image", "default");

                            mDatabase.child("Users").child(user.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(LoginActivity.this, "Hello" + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) { // TODO; previous
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }

        } else {
            // TODO ;
        }
    }
}
