package com.fyp.discussx.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Comment;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.model.User;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;


import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BUNDLE_COMMENT = "comment";
    private Post mPost;
    private User mUser;
    private EditText mCommentEditTextView;
    private Comment mComment;
    private static final String TAG = "PostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (savedInstanceState != null) {
            mComment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }

        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra(Constant.EXTRA_POST);

        init();
        initPost();
        initCommentSection();
    }

    //display comments list
    private void initCommentSection() {
        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                FirebaseUtils.getCommentRef(mPost.getPostId())
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));

                Glide.with(PostActivity.this)
                        .load(mPost.getUser().getPhotoUrl())
                        .into(viewHolder.commentOwnerDisplay);

            }
        };

        commentRecyclerView.setAdapter(commentAdapter);
    }

    //display posts list
    private void initPost() {
        ImageView postOwnerDisplayImageView = findViewById(R.id.iv_post_owner_display);
        TextView postOwnerUsernameTextView = findViewById(R.id.tv_post_username);
        TextView postTimeCreatedTextView = findViewById(R.id.tv_time);
        ImageView postDisplayImageView = findViewById(R.id.iv_post_display);
        LinearLayout postUpvoteLayout = findViewById(R.id.like_layout);
        LinearLayout postDownvoteLayout = findViewById(R.id.downvote_layout);
        LinearLayout postCommentLayout = findViewById(R.id.comment_layout);
        TextView postNumLikesTextView = findViewById(R.id.tv_upvotes);
        TextView postNumCommentsTextView = findViewById(R.id.tv_comments);
        TextView postTextTextView = findViewById(R.id.tv_post_text);

        postOwnerUsernameTextView.setText(mPost.getUser().getUser());
        postTimeCreatedTextView.setText(DateUtils.getRelativeTimeSpanString(mPost.getTimeCreated()));
        postTextTextView.setText(mPost.getPostText());
        postNumLikesTextView.setText(String.valueOf(mPost.getNumLikes()));
        postNumCommentsTextView.setText(String.valueOf(mPost.getNumComments()));

        Glide.with(PostActivity.this)
                .load(mPost.getUser().getPhotoUrl())
                .into(postOwnerDisplayImageView);

        if (mPost.getPostImageUrl() != null) {
            postDisplayImageView.setVisibility(View.VISIBLE);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(mPost.getPostImageUrl());

            Glide.with(PostActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(postDisplayImageView);
        } else {
            postDisplayImageView.setImageBitmap(null);
            postDisplayImageView.setVisibility(View.GONE);
        }
    }

    private void init() {
        mCommentEditTextView = findViewById(R.id.et_comment);
        mCommentEditTextView.setOnClickListener(this);
        findViewById(R.id.iv_send).setOnClickListener(this);
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
        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Sending comment..");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        hideKeyboardFrom(PostActivity.this);
        mComment = new Comment();
        final String uid = FirebaseUtils.getUid();
        String strComment = mCommentEditTextView.getText().toString();

        mComment.setCommentId(uid);
        mComment.setComment(strComment);
        mComment.setTimeCreated(System.currentTimeMillis());
        mComment.setUserName(FirebaseUtils.getCurrentUser().getDisplayName());
        FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        FirebaseUtils.getCommentRef(mPost.getPostId())
                                .child(uid)
                                .setValue(mComment);

                        FirebaseUtils.getPostRef().child(mPost.getPostId())
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
    }


    public static class CommentHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView commentOwnerDisplay;
        public CommentHolder(View itemView) {
            super(itemView);
            mView = itemView;
            commentOwnerDisplay =  mView.findViewById(R.id.iv_comment_owner_display);
        }

        public void setUsername(String username) {
            TextView usernameTextView =  mView.findViewById(R.id.tv_username);
            usernameTextView.setText(username);
        }

        public void setTime(CharSequence time) {
            TextView timeTextView = mView.findViewById(R.id.tv_time);
            timeTextView.setText(time);
        }

        public void setComment(String comment) {
            TextView commentTextView =  mView.findViewById(R.id.tv_comment);
            commentTextView.setText(comment);
        }
    }
    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }
}