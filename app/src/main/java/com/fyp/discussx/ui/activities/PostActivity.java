package com.fyp.discussx.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.fyp.discussx.ui.activities.dialogs.CommentSortDialog;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;


import com.firebase.ui.storage.images.FirebaseImageLoader;
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

public class PostActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        initNavHeader(navHeaderView);

        btnCommentSort = findViewById(R.id.comment_sort);

        if (savedInstanceState != null) {
            mComment = (Comment) savedInstanceState.getSerializable(BUNDLE_COMMENT);
        }

        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra(Constant.EXTRA_POST);

        init();
        initPost();
        initCommentSection();


    }

    public PostActivity () {

    }

    private void initNavHeader(View view) {
        mDisplayImageView = (ImageView) view.findViewById(R.id.imageView_display);
        mNameTextView = (TextView) view.findViewById(R.id.textview_name);
        mEmailTextView = (TextView) view.findViewById(R.id.textView_email);

        mUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User users = dataSnapshot.getValue(User.class);
                    Glide.with(PostActivity.this)
                            .load(users.getPhotoUrl())
                            .into(mDisplayImageView);

                    mNameTextView.setText(users.getUser());
                    mEmailTextView.setText(users.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
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
            protected void populateViewHolder(final CommentHolder viewHolder, final Comment model, int position) {
                viewHolder.setUsername(model.getUserName());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setCommentNumLikes(String.valueOf(model.getNumCommentLikes()));
                viewHolder.setCommentNumDownvotes(String.valueOf(model.getNumCommentDownvotes()));

                Glide.with(PostActivity.this)
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

        AlertDialog.Builder commentSort = new AlertDialog.Builder(this);
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
        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

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

                Glide.with(PostActivity.this)
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
        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

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

                Glide.with(PostActivity.this)
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
        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

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

                Glide.with(PostActivity.this)
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
        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));

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

                Glide.with(PostActivity.this)
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
        ImageView postOwnerDisplayImageView = findViewById(R.id.iv_post_owner_display);
        TextView postOwnerUsernameTextView = findViewById(R.id.tv_post_username);
        TextView postTimeCreatedTextView = findViewById(R.id.tv_time);
        ImageView postDisplayImageView = findViewById(R.id.iv_post_display);

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

        if (!TextUtils.isEmpty(mCommentEditTextView.getText().toString())) {
            final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
            progressDialog.setMessage("Sending comment..");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            hideKeyboardFrom(PostActivity.this);
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
        } else {
            Toast.makeText(this, "Type something you idiot", Toast.LENGTH_SHORT).show();
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_COMMENT, mComment);
        super.onSaveInstanceState(outState);
    }


    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_group) {
            Intent intent = new Intent (PostActivity.this, CreateGroupActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_join_group) {
            Intent intent = new Intent (PostActivity.this, JoinGroupActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.refresh) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.moderator_page) {
            // Handle the camera action
        }  else if (id == R.id.settings) {

        } else if (id == R.id.moderator_page) {

        }  else if (id == R.id.button_sign_out) {
            Toast.makeText(this, "wtf", Toast.LENGTH_SHORT).show();
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}