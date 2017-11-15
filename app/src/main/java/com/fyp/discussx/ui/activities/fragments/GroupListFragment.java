package com.fyp.discussx.ui.activities.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.BuildConfig;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.GroupNameAndId;
import com.fyp.discussx.model.JoinGroup;
import com.fyp.discussx.ui.activities.CreateGroupActivity;
import com.fyp.discussx.ui.activities.CreatePostActivity;
import com.fyp.discussx.ui.activities.InsideGroup;
import com.fyp.discussx.ui.activities.JoinGroupActivity;
import com.fyp.discussx.ui.activities.MainActivity;
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

public class GroupListFragment extends Fragment{
    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".GROUP_LIST_FRAGMENT_TAG";
    private View mRootView;
    private ListView groupListView;
    private ImageView logo;
    private TextView caption;
    private CustomAdapter customAdapter;
    private List<String> groupNameArray = new ArrayList<>();
    private List<String> groupIdArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public GroupListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_group_list, container, false);

        init();
        return mRootView;
    }


    private void init() {


    }




}