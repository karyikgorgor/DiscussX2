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
import com.fyp.discussx.utils.Constant;
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inside_group_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

         if (id == R.id.action_delete_group) {
            deleteGroup();
        } else if (id == R.id.action_quit_group) {
            quitGroup();
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
        final String groupId = getIntent().getExtras().getString("groupId");

        FirebaseUtils.getGroupCreatedRef(groupId)
                .child("creatorId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(String.class).equals(FirebaseUtils.getCurrentUser().getUid())) {

                            AlertDialog.Builder notCreator = new AlertDialog.Builder(InsideGroup.this);
                            notCreator.setCancelable(false);
                            notCreator.setMessage("Are you sure to delete this group? Everything inside will be permanently deleted.");
                            notCreator.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUtils.getGroupCreatedRef(groupId).setValue(null);
                                    FirebaseUtils.getGroupJoinedFromUserRecordRef().child(groupId).setValue(null);
                                    FirebaseUtils.getGroupIdAndName().child(groupId).setValue(null);
                                    Intent intent = new Intent (InsideGroup.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });;
                            AlertDialog alertDialog = notCreator.create();
                            alertDialog.show();

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

    private void quitGroup () {
        final String groupId = getIntent().getExtras().getString("groupId");

        AlertDialog.Builder notCreator = new AlertDialog.Builder(InsideGroup.this);
        notCreator.setCancelable(false);
        notCreator.setMessage("Are you sure to quit this group?");
        notCreator.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUtils.getGroupJoinedFromUserRecordRef().child(groupId).setValue(null);
                FirebaseUtils.getGroupCreatedRef(groupId)
                        .child(Constant.GROUP_MEMBER)
                        .child(FirebaseUtils.getCurrentUser().getUid())
                        .setValue(null);
                dialog.dismiss();
                Intent intent = new Intent (InsideGroup.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = notCreator.create();
        alertDialog.show();

    }

}