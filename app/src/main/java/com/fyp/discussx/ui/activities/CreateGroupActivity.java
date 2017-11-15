package com.fyp.discussx.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.discussx.R;
import com.fyp.discussx.model.Example;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.model.User;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


public class CreateGroupActivity extends AppCompatActivity {
    private EditText groupNameEditText;
    private Button btnCreateGroup;
    private Button btnCollapse;
    private Group mGroup;
    private JoinGroup joinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        setTitle("Create A Discussion Group");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        groupNameEditText = findViewById(R.id.group_name_et);
        btnCreateGroup = findViewById(R.id.btn_create_group);
        btnCollapse = findViewById(R.id.btn_collapse);

        btnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupNameEditText.setVisibility(View.VISIBLE);
                btnCreateGroup.setVisibility(View.VISIBLE);
                btnCollapse.setVisibility(View.GONE);
            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();

            }
        });


    }

   private void createGroup () {
        final String groupNameString = groupNameEditText.getText().toString();

        if (!TextUtils.isEmpty(groupNameString)) {
            Group mGroup = new Group();
            JoinGroup joinGroup = new JoinGroup();

            final String uid = FirebaseUtils.getUid();

            mGroup.setGroupName(groupNameString);
            mGroup.setTimeCreated(System.currentTimeMillis());
            mGroup.setCreatorId(FirebaseUtils.getCurrentUser().getUid());
            mGroup.setCreatorName(FirebaseUtils.getCurrentUser().getDisplayName());

            joinGroup.setMembersId(FirebaseUtils.getCurrentUser().getUid());
            joinGroup.setEmail(FirebaseUtils.getCurrentUser().getEmail());
            joinGroup.setUserName(FirebaseUtils.getCurrentUser().getDisplayName());
            joinGroup.setTimeJoined(System.currentTimeMillis());

            FirebaseUtils.getGroupCreatedRef().child(uid).setValue(mGroup);

            FirebaseUtils.getGroupCreatedRef().child(uid)
                    .child(Constant.GROUP_MEMBER)
                    .child(FirebaseUtils.getCurrentUser().getUid())
                    .setValue(joinGroup);

            FirebaseUtils.addGroupIdAndName(uid, groupNameString);

            FirebaseUtils.addRecord(Constant.GROUP_CREATED_KEY, uid, groupNameString);
            FirebaseUtils.addRecord(Constant.GROUP_JOINED_KEY, uid, groupNameString);

            Intent intent = new Intent (CreateGroupActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            
            /*FirebaseUtils.getGroupCreatedRef().child(uid)
                    .child(Constant.NUM_MEMBERS_KEY)
                    .runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            long num = (long) mutableData.getValue();
                            mutableData.setValue(num + 1);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                        }
                    });*/
        } else {
            Toast.makeText(this, "Please make sure you have entered a group name.", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_initial_profile_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}