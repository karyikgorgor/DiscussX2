package com.fyp.discussx.ui.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class GroupListFragment extends Fragment {
    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".GROUP_LIST_FRAGMENT_TAG";
    private View mRootView;
    private ListView groupListView;
    private ArrayList <String> mArrayList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    public GroupListFragment () {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_group_list, container, false);
       init();
        return mRootView;
    }


    private void init () {
        groupListView = mRootView.findViewById(R.id.group_list_view);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mArrayList);
       groupListView.setAdapter(mAdapter);

        FirebaseUtils.getGroupJoinedFromUserRecordRef()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String groupName = dataSnapshot.getValue(String.class);
                        String groupId = dataSnapshot.getKey();
                        //retreiveGroupInfo(groupName,groupId);
                        mArrayList.add(groupName);
                        mAdapter.notifyDataSetChanged();
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
                String test = groupListView.getItemAtPosition(position).toString();

                Intent intent = new Intent(getActivity(), InsideGroup.class);
                intent.putExtra("groupName", test);
                startActivity(intent);

            }
        });

    }

    /*private void retreiveGroupInfo (String groupName, String groupId) {
        ArrayList details = getListData(groupName, groupId);
        final ListView groupView = mRootView.findViewById(R.id.group_list_view);
        groupView.setAdapter(new CustomAdapter(getActivity(), details));

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object x = groupView.getItemAtPosition(position);
                GroupNameAndId groupNameAndId = (GroupNameAndId)x;
                Toast.makeText(getActivity(), "Selected: "+ groupNameAndId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList getListData (String groupName, String groupId) {
        ArrayList <GroupNameAndId> results = new ArrayList<GroupNameAndId>();
        GroupNameAndId groupNameAndId = new GroupNameAndId();

        groupNameAndId.setGroupName(groupName);
        groupNameAndId.setGroupId(groupId);
        results.add(groupNameAndId);

        return results;
    }*/

}