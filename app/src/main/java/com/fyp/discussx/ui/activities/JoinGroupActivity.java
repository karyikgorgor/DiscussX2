package com.fyp.discussx.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.model.User;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


public class JoinGroupActivity extends AppCompatActivity {
    private Button btnJoinGroup;
    private Group mGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        showGroupName();
    }


    private void showGroupName () {
        RecyclerView groupRecyclerView = findViewById(R.id.grouplist_recyclerview);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(JoinGroupActivity.this));

        Query query = FirebaseUtils.getGroupCreatedRef().orderByChild(Constant.GROUP_NAME);

        FirebaseRecyclerAdapter<Group, GroupHolder> groupAdapter = new FirebaseRecyclerAdapter<Group, GroupHolder>(
                Group.class,
                R.layout.row_groups,
                GroupHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(GroupHolder viewHolder, Group model, int position) {
                viewHolder.setGroupName(model.getGroupName());
                viewHolder.setGroupId(model.getGroupId());
            }

            @Override
            public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final GroupHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnClickListener(new GroupHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView groupIdTv = view.findViewById(R.id.tv_group_id);
                        String groupId = groupIdTv.getText().toString();
                        joinGroup(groupId);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder;
            }
        };
        groupRecyclerView.setAdapter(groupAdapter);
    }


    public static class GroupHolder extends RecyclerView.ViewHolder {
        TextView groupNameTV;
        TextView groupIdTV;

        public GroupHolder(View itemView) {
            super(itemView);
            groupNameTV = itemView.findViewById(R.id.tv_group_name);
            groupIdTV = itemView.findViewById(R.id.tv_group_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }
        public void setGroupId (String groupId) {
            groupIdTV.setText(groupId);
        }

        public void setGroupName (String groupName) {
            groupNameTV.setText(groupName);
        }

        private GroupHolder.ClickListener mClickListener;

        public interface ClickListener {
             void onItemClick (View view, int position);
             void onItemLongClick (View view, int position);
        }

        public void setOnClickListener (GroupHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

    }

    private void joinGroup (final String groupId) {
        AlertDialog.Builder confirmJoinGroup = new AlertDialog.Builder(this);
        confirmJoinGroup.setTitle("Are you sure to join ");
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

                        final JoinGroup joinGroupInfo = new JoinGroup(memberId, timeJoined);
                        joinGroupInfo.setMembersId(memberId);
                        joinGroupInfo.setTimeJoined(timeJoined);

                        FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".",","))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                FirebaseUtils.getGroupMembersRef(groupId).child(memberId).setValue(joinGroupInfo);

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
                                                progressDialog.dismiss();
                                                FirebaseUtils.addToMyRecord(Constant.GROUP_JOINED_KEY, groupId);
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

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

