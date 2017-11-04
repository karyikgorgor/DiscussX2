package com.fyp.discussx.ui.activities.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.fyp.discussx.BuildConfig;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Comment;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.model.User;
import com.fyp.discussx.ui.activities.PostActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsidePostFragment extends Fragment implements View.OnClickListener{

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".GROUP_LIST_FRAGMENT_TAG";
    private View mRootView;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private ValueEventListener mUserValueEventListener;
    private DatabaseReference mUserRef;
    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private User mUser;
    private EditText mCommentEditTextView;
    private Comment mComment;
    private Button btnCommentSort;
    private static final String TAG = "PostActivity";
    public InsidePostFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView  =  inflater.inflate(R.layout.fragment_inside_post, container, false);

        btnCommentSort = mRootView.findViewById(R.id.comment_sort);

        if (savedInstanceState != null) {
            mComment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }

        Intent intent = getActivity().getIntent();
        mPost = (Post) intent.getSerializableExtra(Constant.EXTRA_POST);

        init();
        initPost();
        initCommentSection();

        return mRootView;
    }




    //display comments list
    private void initCommentSection() {
        RecyclerView commentRecyclerView = mRootView.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                FirebaseUtils.getCommentRef(mPost.getPostId())
        ) {
            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(getActivity())
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

                viewHolder.commentLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentLikeClick(mPost.getPostId(), model.getCommentId());
                    }
                });

                viewHolder.commentDownvoteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentDownvotedClick(mPost.getPostId(), model.getCommentId());
                    }
                });

                btnCommentSort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentSortDialog();
                    }
                });

            }
        };

        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void commentSortDialog () {
        CharSequence [] options = {"Most Upvoted", "Most Downvoted", "Most Argumentative","Oldest", "Newest"};

        AlertDialog.Builder commentSort = new AlertDialog.Builder(getActivity());
        commentSort.setTitle("Comments Sort Options");
        commentSort.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        commentSortByMostUpvotes(mPost.getPostId());
                        break;
                    case 1:
                        commentSortByMostDownvotes(mPost.getPostId());
                        break;
                    case 2:

                        break;
                    case 3:
                        commentSortByOldest(mPost.getPostId());
                        break;
                    case 4:
                        commentSortByNewest(mPost.getPostId());
                        break;
                }

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = commentSort.create();
        alert.show();
    }


    private void commentSortByMostUpvotes (String postId) {
        RecyclerView commentRecyclerView = mRootView.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseUtils.getCommentRef(postId).orderByChild(Constant.NUM_COMMENT_LIKES_KEY);

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                query
        ) {

            @Override
            public Comment getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(getActivity())
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

                viewHolder.commentLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentLikeClick(mPost.getPostId(), model.getCommentId());
                    }
                });
                viewHolder.commentDownvoteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentDownvotedClick(mPost.getPostId(), model.getCommentId());
                    }
                });

            }
        };
        commentRecyclerView.setAdapter(commentAdapter);
    }
    private void commentSortByMostDownvotes (String postId) {
        RecyclerView commentRecyclerView = mRootView.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseUtils.getCommentRef(postId).orderByChild(Constant.NUM_COMMENT_DOWNVOTES_KEY);

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                query
        ) {

            @Override
            public Comment getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(getActivity())
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

                viewHolder.commentLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentLikeClick(mPost.getPostId(), model.getCommentId());
                    }
                });
                viewHolder.commentDownvoteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentDownvotedClick(mPost.getPostId(), model.getCommentId());
                    }
                });

            }
        };
        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void commentSortByNewest (String postId) {
        RecyclerView commentRecyclerView = mRootView.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseUtils.getCommentRef(postId).orderByChild(Constant.COMMENT_TIME_CREATED);

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                query
        ) {

            @Override
            public Comment getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(getActivity())
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

                viewHolder.commentLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentLikeClick(mPost.getPostId(), model.getCommentId());
                    }
                });
                viewHolder.commentDownvoteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentDownvotedClick(mPost.getPostId(), model.getCommentId());
                    }
                });

            }
        };
        commentRecyclerView.setAdapter(commentAdapter);
    }


    private void commentSortByOldest (String postId) {
        RecyclerView commentRecyclerView = mRootView.findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseUtils.getCommentRef(postId).orderByChild(Constant.COMMENT_TIME_CREATED);

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                query
        ) {

            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(getActivity())
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

                viewHolder.commentLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentLikeClick(mPost.getPostId(), model.getCommentId());
                    }
                });
                viewHolder.commentDownvoteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentDownvotedClick(mPost.getPostId(), model.getCommentId());
                    }
                });

            }
        };
        commentRecyclerView.setAdapter(commentAdapter);
    }
    //display posts list
    private void initPost() {
        ImageView postOwnerDisplayImageView = mRootView.findViewById(R.id.iv_post_owner_display);
        TextView postOwnerUsernameTextView = mRootView.findViewById(R.id.tv_post_username);
        TextView postTimeCreatedTextView = mRootView.findViewById(R.id.tv_time);
        ImageView postDisplayImageView = mRootView.findViewById(R.id.iv_post_display);

        TextView postNumLikesTextView = mRootView.findViewById(R.id.tv_upvotes);
        TextView postNumCommentsTextView = mRootView.findViewById(R.id.tv_comments);
        TextView postTitleTextView = mRootView.findViewById(R.id.tv_post_title);
        TextView postDescTextView = mRootView.findViewById(R.id.tv_post_desc);


        postOwnerUsernameTextView.setText(mPost.getUser().getUser());
        postTimeCreatedTextView.setText(DateUtils.getRelativeTimeSpanString(mPost.getTimeCreated()));
        postTitleTextView.setText(mPost.getPostTitle());
        postDescTextView.setText(mPost.getPostDesc());
        postNumLikesTextView.setText(String.valueOf(mPost.getNumLikes()));
        postNumCommentsTextView.setText(String.valueOf(mPost.getNumComments()));


        Glide.with(getActivity())
                .load(mPost.getUser().getPhotoUrl())
                .into(postOwnerDisplayImageView);

        if (mPost.getPostImageUrl() != null) {
            postDisplayImageView.setVisibility(View.VISIBLE);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(mPost.getPostImageUrl());

            Glide.with(getActivity())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(postDisplayImageView);
        } else {
            postDisplayImageView.setImageBitmap(null);
            postDisplayImageView.setVisibility(View.GONE);
        }
    }

    private void init() {
        mCommentEditTextView = mRootView.findViewById(R.id.et_comment);
        mCommentEditTextView.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send:
                sendComment();
                mCommentEditTextView.setText("");
        }
    }

    private void sendComment() {

        if (!TextUtils.isEmpty(mCommentEditTextView.getText().toString())) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Sending comment..");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            hideKeyboardFrom(getActivity());
            mComment = new Comment();
            final String uid = FirebaseUtils.getUid();

            mComment.setCommentId(uid);
            mComment.setComment(mCommentEditTextView.getText().toString());
            mComment.setTimeCreated(System.currentTimeMillis());
            mComment.setNumCommentLikes(0);
            mComment.setNumCommentDownvotes(0);
            mComment.setUserName(FirebaseUtils.getCurrentUser().getDisplayName());

            FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".", ","))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            FirebaseUtils.getCommentRef(mPost.getPostId())
                                    .child(uid)
                                    .setValue(mComment);

                            FirebaseUtils.getGroupCreatedRef(getActivity().getIntent().getExtras().getString("groupId"))
                                    .child(Constant.POST_KEY)
                                    .child(mPost.getPostId())
                                    .child(Constant.NUM_COMMENTS_KEY)
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
                                            FirebaseUtils.addToMyRecord(Constant.COMMENTS_KEY, uid);
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Type something you idiot", Toast.LENGTH_SHORT).show();
        }

    }


    public static class CommentHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView commentOwnerDisplay;
        TextView usernameTextView;
        TextView timeTextView;
        TextView commentTextView;
        TextView numLikesTextView;
        TextView numDownvotesTextView;
        LinearLayout commentLikeLayout;
        LinearLayout commentDownvoteLayout;

        public CommentHolder(View itemView) {
            super(itemView);
            mView = itemView;
            commentOwnerDisplay =  mView.findViewById(R.id.iv_comment_owner_display);
            usernameTextView =  mView.findViewById(R.id.tv_username);
            timeTextView = mView.findViewById(R.id.tv_time);
            commentTextView =  mView.findViewById(R.id.tv_comment);
            numLikesTextView = mView.findViewById(R.id.comment_tv_upvotes);
            numDownvotesTextView = mView.findViewById(R.id.comment_tv_downvote);
            commentLikeLayout = mView.findViewById(R.id.comment_like_layout);
            commentDownvoteLayout = mView.findViewById(R.id.comment_downvote_layout);
        }

        public void setUsername(String username) {
            usernameTextView.setText(username);
        }

        public void setTime(CharSequence time) {
            timeTextView.setText(time);
        }

        public void setComment(String comment) {
            commentTextView.setText(comment);
        }

        public void setCommentNumLikes (String numLikes) {
            numLikesTextView.setText(numLikes);
        }

        public void setCommentNumDownvotes (String numDownvotes) {
            numDownvotesTextView.setText(numDownvotes);
        }
    }

    private void onCommentLikeClick(final String postId, final String commentId) {
        FirebaseUtils.getCommentLikedRef(postId, commentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //User liked
                            FirebaseUtils.getCommentRef(postId)
                                    .child(commentId)
                                    .child(Constant.NUM_COMMENT_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getCommentLikedRef(postId, commentId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getCommentRef(postId)
                                    .child(commentId)
                                    .child(Constant.NUM_COMMENT_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getCommentLikedRef(postId, commentId)
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

    private void onCommentDownvotedClick(final String postId, final String commentId) {
        FirebaseUtils.getCommentDownvotedRef(postId, commentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //User liked
                            FirebaseUtils.getCommentRef(postId)
                                    .child(commentId)
                                    .child(Constant.NUM_COMMENT_DOWNVOTES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getCommentDownvotedRef(postId, commentId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getCommentRef(postId)
                                    .child(commentId)
                                    .child(Constant.NUM_COMMENT_DOWNVOTES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getCommentDownvotedRef(postId, commentId)
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }


    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
