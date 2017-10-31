package com.fyp.discussx.ui.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.ui.activities.InsideGroup;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class GroupListFragment extends Fragment {
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
                        String value = dataSnapshot.getValue(String.class);
                        mArrayList.add(value);
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
                Toast.makeText(getActivity(), "Group Name: " + test, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), InsideGroup.class);
                startActivity(intent);

            }
        });

    }

}