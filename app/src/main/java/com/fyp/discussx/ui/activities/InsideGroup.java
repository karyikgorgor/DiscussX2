package com.fyp.discussx.ui.activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.User;
import com.fyp.discussx.ui.activities.adapters.TabAdapter;
import com.fyp.discussx.ui.activities.dialogs.PostCreateDialog;
import com.fyp.discussx.ui.activities.fragments.HomeFragment;
import com.fyp.discussx.ui.activities.fragments.InsideGroupFragment;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InsideGroup extends BaseActivity{

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUserRef;
    private String groupNameX;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(R.id.container,
                new InsideGroupFragment(),
                InsideGroupFragment.FRAGMENT_TAG);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(InsideGroup.this, RegisterActivity.class));
                }
            }
        };

        init();

        setContentView(R.layout.activity_inside_group);

        groupNameX = getIntent().getExtras().getString("groupName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(groupNameX);
        }
    }


    private void init() {

        if (mFirebaseUser != null) {
            mUserRef = FirebaseUtils.getUserRef(mFirebaseUser.getEmail().replace(".", ","));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inside_group_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_group_info) {
            Intent intent = new Intent(InsideGroup.this, CreateGroupActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_delete_group) {
            deleteGroup();
        } else if (id == R.id.action_report_group) {

        }

        else if (item.getItemId() == R.id.refresh) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void deleteGroup () {
        String groupId = getIntent().getExtras().getString("groupId");

        FirebaseUtils.getGroupCreatedRef(groupId)
                .child("creatorId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(String.class).equals(FirebaseUtils.getCurrentUser().getUid())) {

                        } else {
                            AlertDialog.Builder notCreator = new AlertDialog.Builder(InsideGroup.this);
                            notCreator.setCancelable(false);
                            notCreator.setMessage("You are not authorised to delete this group.");
                            notCreator.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = notCreator.create();
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}