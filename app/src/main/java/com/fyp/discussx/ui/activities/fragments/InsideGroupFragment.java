package com.fyp.discussx.ui.activities.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fyp.discussx.BuildConfig;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.ui.activities.CreatePostActivity;
import com.fyp.discussx.ui.activities.PostActivity;
import com.fyp.discussx.ui.activities.dialogs.PostCreateDialog;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fyp.discussx.utils.OnSingleClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class InsideGroupFragment extends Fragment {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".INSIDE_GROUP_FRAGMENT_TAG";
    private View mRootVIew;
    private FirebaseRecyclerAdapter<Post, PostHolder> mPostAdapter;
    private RecyclerView mPostRecyclerView;
    private String groupId;

    public InsideGroupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootVIew = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        return mRootVIew;
    }

    private void init() {
        FloatingActionButton fab =  mRootVIew.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupId = getActivity().getIntent().getExtras().getString("groupId");
                Intent intent = new Intent (getActivity(), CreatePostActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });

        mPostRecyclerView = mRootVIew.findViewById(R.id.recyclerview_post);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter(getActivity().getIntent().getExtras().getString("groupId"));
        mPostRecyclerView.setAdapter(mPostAdapter);
    }

    private void setupAdapter(String groupIdX) {
        Query query = FirebaseUtils.getPostFromGroupRef(groupIdX);


        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(
                Post.class,
                R.layout.row_post,
                PostHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final PostHolder viewHolder, final Post model, int position) {
                viewHolder.setNumCOmments(String.valueOf(model.getNumComments()));
                viewHolder.setNumLikes(String.valueOf(model.getNumUpvotes()));
                viewHolder.setPostNumDownvotesTextView(String.valueOf(model.getNumDownvotes()));
                viewHolder.setTIme(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setUsername(model.getUser().getUser());
                viewHolder.setPostTitle(model.getPostTitle());
                viewHolder.setPostDesc(model.getPostDesc());

                Glide.with(getActivity())
                        .load(model.getUser().getPhotoUrl())
                        .into(viewHolder.postOwnerDisplayImageView);

                if (model.getPostImageUrl() != null) {
                    viewHolder.postDisplayImageVIew.setVisibility(View.VISIBLE);
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference(model.getPostImageUrl());

                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(viewHolder.postDisplayImageVIew);

                } else {
                    viewHolder.postDisplayImageVIew.setImageBitmap(null);
                    viewHolder.postDisplayImageVIew.setVisibility(View.GONE);
                }
                viewHolder.postUpvoteLayout.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onOneClick(View v) {
                        FirebaseUtils.getPostDownvotedFromUserRef(model.getPostId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(getActivity(), "You can only either up-vote or down-vote.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            onPostUpvoteClick(getActivity().getIntent().getExtras().getString("groupId"),model.getPostId(), model.getNumUpvotes());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                });

                viewHolder.postDownvotesLayout.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onOneClick(View v) {

                        FirebaseUtils.getPostUpvotedFromUserRef(model.getPostId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(getActivity(), "You can only either up-vote or down-vote.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            onPostDownvoteClick(getActivity().getIntent().getExtras().getString("groupId"),model.getPostId(), model.getNumDownvotes());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                });

                        viewHolder.postCommentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), PostActivity.class);
                                intent.putExtra(Constant.EXTRA_POST, model);
                                intent.putExtra("groupId", getActivity().getIntent().getExtras().getString("groupId"));
                                intent.putExtra("groupName", getActivity().getIntent().getExtras().getString("groupName"));
                                startActivity(intent);
                            }
                        });
            }
        };
    }


    private void onPostUpvoteClick(final String groupId,
                               final String postId,
                               final long upvotes) {

        FirebaseUtils.getPostUpvotedRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            FirebaseUtils.getGroupCreatedRef(groupId)
                                    .child(Constant.POST_KEY)
                                    .child(postId)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            mutableData.child(Constant.NUM_UPVOTES_KEY)
                                                    .setValue(upvotes - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError,
                                                               boolean b,
                                                               DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostUpvotedRef(postId)
                                                    .setValue(null);
                                            FirebaseUtils.getPostUpvotedFromUserRef(postId).setValue(null);
                                        }
                                    });
                        } else  {
                            FirebaseUtils.getGroupCreatedRef(groupId)
                                    .child(Constant.POST_KEY)
                                    .child(postId)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            mutableData.child(Constant.NUM_UPVOTES_KEY).setValue(upvotes + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostUpvotedRef(postId)
                                                    .setValue(true);
                                            FirebaseUtils.getPostUpvotedFromUserRef(postId).setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void onPostDownvoteClick (final String groupId,
                                 final String postId,
                                 final long downvotes) {


        FirebaseUtils.getPostDownvotedRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            FirebaseUtils.getGroupCreatedRef(groupId)
                                    .child(Constant.POST_KEY)
                                    .child(postId)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            mutableData.child(Constant.NUM_DOWNVOTES_KEY).setValue(downvotes - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostDownvotedRef(postId)
                                                    .setValue(null);
                                            FirebaseUtils.getPostDownvotedFromUserRef(postId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getGroupCreatedRef(groupId)
                                    .child(Constant.POST_KEY)
                                    .child(postId)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            mutableData.child(Constant.NUM_DOWNVOTES_KEY).setValue(downvotes + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostDownvotedRef(postId)
                                                    .setValue(true);
                                            FirebaseUtils.getPostDownvotedFromUserRef(postId)
                                                    .setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        ImageView postOwnerDisplayImageView;
        TextView postOwnerUsernameTextView;
        TextView postTimeCreatedTextView;
        ImageView postDisplayImageVIew;
        LinearLayout postUpvoteLayout;
        LinearLayout postDownvotesLayout;
        LinearLayout postCommentLayout;
        TextView postNumLikesTextView;
        TextView postNumDownvotesTextView;
        TextView postNumCommentsTextView;
        TextView postTitleTextView;
        TextView postDescTextView;

        public PostHolder(View itemView) {
            super(itemView);
            postOwnerDisplayImageView = (ImageView) itemView.findViewById(R.id.iv_post_owner_display);
            postOwnerUsernameTextView = (TextView) itemView.findViewById(R.id.tv_post_username);
            postTimeCreatedTextView = (TextView) itemView.findViewById(R.id.tv_time);
            postDisplayImageVIew = (ImageView) itemView.findViewById(R.id.iv_post_display);
            postUpvoteLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);
            postDownvotesLayout = itemView.findViewById(R.id.downvote_layout);
            postCommentLayout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
            postNumLikesTextView = (TextView) itemView.findViewById(R.id.tv_upvotes);
            postNumDownvotesTextView = itemView.findViewById(R.id.tv_downvote);
            postNumCommentsTextView = (TextView) itemView.findViewById(R.id.tv_comments);
            postTitleTextView = (TextView) itemView.findViewById(R.id.tv_post_title);
            postDescTextView = itemView.findViewById(R.id.tv_post_desc);
        }

        public void setUsername(String username) {
            postOwnerUsernameTextView.setText(username);
        }

        public void setTIme(CharSequence time) {
            postTimeCreatedTextView.setText(time);
        }

        public void setNumLikes(String numLikes) {
            postNumLikesTextView.setText(numLikes);
        }

        public void setPostNumDownvotesTextView (String numDownvotes) {
            postNumDownvotesTextView.setText(numDownvotes);
        }

        public void setNumCOmments(String numComments) {
            postNumCommentsTextView.setText(numComments);
        }

        public void setPostTitle(String text) {
            postTitleTextView.setText(text);
        }

        public void setPostDesc (String text) {
            postDescTextView.setText(text);
        }

    }
}