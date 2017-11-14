package com.fyp.discussx.ui.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.BuildConfig;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.GroupNameAndId;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.ui.activities.CreatePostActivity;
import com.fyp.discussx.ui.activities.InsideGroup;
import com.fyp.discussx.ui.activities.adapters.CustomAdapter;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getAutoLogAppEventsEnabled;

public class GroupListFragment extends Fragment {
    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".GROUP_LIST_FRAGMENT_TAG";
    private View mRootView;
    private ListView groupListView;
    private ImageView logo;
    private TextView caption;
    private CustomAdapter customAdapter;

    private List <String> groupNameArray = new ArrayList<>();
    private List <String> groupIdArray = new ArrayList<>();

    public GroupListFragment () {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_group_list, container, false);

        init();
        return mRootView;
    }


    private void init () {
        groupListView = mRootView.findViewById(R.id.group_list_view);
        logo = mRootView.findViewById(R.id.logo_in_join_group);
        caption = mRootView.findViewById(R.id.ask_join_group);


        FirebaseUtils.getGroupJoinedFromUserRecordRef().orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String groupName = dataSnapshot.getValue(String.class);
                String groupId = dataSnapshot.getKey();

                groupNameArray.add(groupName);
                groupIdArray.add(groupId);

                if (groupIdArray.size() == 0) {
                    groupListView.setVisibility(View.GONE);
                    logo.setVisibility(View.VISIBLE);
                    caption.setVisibility(View.VISIBLE);

                } else if (groupIdArray.size() > 0){
                    groupListView.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.GONE);
                    caption.setVisibility(View.GONE);
                }


                customAdapter = new CustomAdapter(getActivity(), groupNameArray, groupIdArray);

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

                Intent intent = new Intent (getActivity(), InsideGroup.class);
                intent.putExtra("groupName", groupNameClick);
                intent.putExtra("groupId", groupIdClick);

                startActivity(intent);
            }
        });

    }

}