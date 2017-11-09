package com.fyp.discussx.ui.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.model.User;
import com.fyp.discussx.ui.activities.adapters.CustomAdapter;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JoinGroupActivity extends AppCompatActivity {

    private Button btnJoinGroup;
    private Group mGroup;
    private ListView groupListView;
    private CustomAdapter customAdapter;
    private List<String> groupNameArray = new ArrayList<>();
    private List <String> groupIdArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupListView = findViewById(R.id.all_group_list_view);

        showGroupList();
    }

    private void showGroupList () {

        FirebaseUtils.getGroupIdAndName().orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String groupName = dataSnapshot.getValue(String.class);
                String groupId = dataSnapshot.getKey();

                groupNameArray.add(groupName);
                groupIdArray.add(groupId);

                customAdapter = new CustomAdapter(JoinGroupActivity.this, groupNameArray, groupIdArray);

                groupListView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupNameClick = groupNameArray.get(position);
                String groupIdClick = groupIdArray.get(position);
                joinGroup(groupIdClick, groupNameClick);
            }
        });

    }

    private void joinGroup (final String groupId, final String groupName) {

        AlertDialog.Builder confirmJoinGroup = new AlertDialog.Builder(this);
        confirmJoinGroup.setTitle("Are you sure to join " + groupName + "?");
        confirmJoinGroup.setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progressDialog = new ProgressDialog(JoinGroupActivity.this);
                        progressDialog.setMessage("Joining group..");
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();

                        final String memberId = FirebaseUtils.getCurrentUser().getUid();
                        Long timeJoined = System.currentTimeMillis();
                        String email = FirebaseUtils.getCurrentUser().getEmail();
                        String userName = FirebaseUtils.getCurrentUser().getDisplayName();

                        final JoinGroup joinGroupInfo = new JoinGroup();
                        joinGroupInfo.setMembersId(memberId);
                        joinGroupInfo.setTimeJoined(timeJoined);
                        joinGroupInfo.setEmail(email);
                        joinGroupInfo.setUserName(userName);
                        joinGroupInfo.setGroupName(groupName);


                                        FirebaseUtils.getGroupCreatedRef(groupId).child(Constant.GROUP_MEMBER).child(memberId).setValue(joinGroupInfo);

                                        FirebaseUtils.getGroupCreatedRef().child(groupId)
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
                                                        FirebaseUtils.addRecord(Constant.GROUP_JOINED_KEY, groupId, groupName);
                                                        progressDialog.dismiss();
                                                        finish();
                                                    }
                                                });


                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = confirmJoinGroup.create();
        alert.show();
    }
}


