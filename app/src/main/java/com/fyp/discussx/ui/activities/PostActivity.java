package com.fyp.discussx.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Comment;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.model.User;
import com.fyp.discussx.ui.activities.dialogs.CommentSortDialog;
import com.fyp.discussx.ui.activities.fragments.InsideGroupFragment;
import com.fyp.discussx.ui.activities.fragments.InsidePostFragment;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;


import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private ValueEventListener mUserValueEventListener;
    private DatabaseReference mUserRef;
    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private User mUser;
    private EditText mCommentEditTextView;
    private Comment mComment;
    private Button btnCommentSort;
    private static final String TAG = "PostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(R.id.container,
                new InsidePostFragment(),
                InsidePostFragment.FRAGMENT_TAG);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(PostActivity.this, RegisterActivity.class));
                }
            }
        };

        init();
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        initNavHeader(navHeaderView);




    }

    public PostActivity () {

    }

    private void init () {
        if (mFirebaseUser != null) {
            mUserRef = FirebaseUtils.getUserRef(mFirebaseUser.getEmail().replace(".", ","));
        }
    }

    private void initNavHeader(View view) {
        mDisplayImageView = (ImageView) view.findViewById(R.id.imageView_display);
        mNameTextView = (TextView) view.findViewById(R.id.textview_name);
        mEmailTextView = (TextView) view.findViewById(R.id.textView_email);

        mUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User users = dataSnapshot.getValue(User.class);
                    Glide.with(PostActivity.this)
                            .load(users.getPhotoUrl())
                            .into(mDisplayImageView);

                    mNameTextView.setText(users.getUser());
                    mEmailTextView.setText(users.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_group) {
            Intent intent = new Intent (PostActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_join_group) {
            Intent intent = new Intent (PostActivity.this, JoinGroupActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.refresh) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.moderator_page) {
            // Handle the camera action
        }  else if (id == R.id.settings) {

        } else if (id == R.id.moderator_page) {

        }  else if (id == R.id.button_sign_out) {
            Toast.makeText(this, "wtf", Toast.LENGTH_SHORT).show();
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        if (mUserRef != null) {
            mUserRef.addValueEventListener(mUserValueEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
        if (mUserRef != null)
            mUserRef.removeEventListener(mUserValueEventListener);
    }
}