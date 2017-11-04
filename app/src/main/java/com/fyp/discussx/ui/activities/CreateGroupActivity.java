package com.fyp.discussx.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fyp.discussx.R;
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
    private Group mGroup;
    private JoinGroup mJoinGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = findViewById(R.id.group_name_et);
        btnCreateGroup = findViewById(R.id.btn_create_group);

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
                Intent intent = new Intent (CreateGroupActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void createGroup () {
        if (!TextUtils.isEmpty(groupNameEditText.getText().toString())) {
            final ProgressDialog progressDialog = new ProgressDialog(CreateGroupActivity.this);
            progressDialog.setMessage("Creating group..");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            mGroup = new Group();
            mJoinGroup = new JoinGroup();
            final String uid = FirebaseUtils.getUid();

            mGroup.setGroupId(uid);
            mGroup.setGroupName(groupNameEditText.getText().toString());
            mGroup.setTimeCreated(System.currentTimeMillis());
            mGroup.setCreator(FirebaseUtils.getCurrentUser().getUid());
            mGroup.setModerator(FirebaseUtils.getCurrentUser().getUid());
            mGroup.setNumMembers(0);
            mGroup.setNumPosts(0);
            mJoinGroup.setMembersId(FirebaseUtils.getCurrentUser().getUid());
            mJoinGroup.setTimeJoined(System.currentTimeMillis());
            mJoinGroup.setEmail(FirebaseUtils.getCurrentUser().getEmail());
            mJoinGroup.setUserName(FirebaseUtils.getCurrentUser().getDisplayName());
            mJoinGroup.setGroupName(groupNameEditText.getText().toString());


            FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".",","))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            FirebaseUtils.getGroupCreatedRef(uid).setValue(mGroup);
                            FirebaseUtils.getGroupCreatedRef(uid).child(Constant.GROUP_MEMBER).child(FirebaseUtils.getCurrentUser().getUid()).setValue(mJoinGroup);
                            FirebaseUtils.addGroupIdAndName(uid, groupNameEditText.getText().toString());


                            FirebaseUtils.getGroupCreatedRef().child(uid)
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
                                            FirebaseUtils.addRecord(Constant.GROUP_CREATED_KEY,uid,groupNameEditText.getText().toString());
                                            FirebaseUtils.addRecord(Constant.GROUP_JOINED_KEY,uid,groupNameEditText.getText().toString());

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
        }
    }

}