package com.fyp.discussx.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.discussx.R;
import com.fyp.discussx.model.User;
import com.fyp.discussx.ui.activities.profile_setting.InitialProfileSetup1;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "RegisterActivity";
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signInButton = findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(this);
        setGooglePlusButtonText(signInButton,"Sign in with Google");
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                showProgressDialog();
                signIn();

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);

                } else {
                    dismissProgressDialog();
                }
            } else {
                dismissProgressDialog();
            }
        } else {
            dismissProgressDialog();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUtils.getUserRef(account.getEmail().replace(".", ","))
                                    .child("email")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                Intent intent2 = new Intent (RegisterActivity.this, MainActivity.class);
                                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent2);
                                                finish();

                                            } else {
                                                User user = new User();
                                                String photoUrl = null;
                                                if (account.getPhotoUrl() != null) {
                                                    user.setPhotoUrl(account.getPhotoUrl().toString());
                                                }

                                                user.setEmail(account.getEmail());
                                                user.setUser(account.getDisplayName());
                                                user.setUid(mAuth.getCurrentUser().getUid());
                                                user.setDob("null");
                                                user.setGender("null");


                                                FirebaseUtils.getUserRef(account.getEmail().replace(".", ","))
                                                        .setValue(user, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                mFirebaseUser = mAuth.getCurrentUser();

                                                                FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".",","))
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                if (!dataSnapshot.hasChild(Constant.ACADEMIC_PROFILE)) {
                                                                                    Intent intent = new Intent (RegisterActivity.this, InitialProfileSetup1.class);
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                } else {
                                                                                    Intent intent2 = new Intent (RegisterActivity.this, MainActivity.class);
                                                                                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                    startActivity(intent2);
                                                                                    finish();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        } else {
                            dismissProgressDialog();
                        }
                    }
                });
    }
}