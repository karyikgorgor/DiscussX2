package com.fyp.discussx.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.discussx.R;
import com.fyp.discussx.model.Group;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.model.User;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends BaseActivity implements View.OnClickListener {
    private static final int RC_PHOTO_PICKER = 1;
    private Post mPost;
    private Group mGroup;
    private ProgressDialog mProgressDialog;
    private Uri mSelectedUri;
    private ImageView mPostDisplay;
    private EditText titleET;
    private EditText descET;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mPost = new Post();
        mGroup = new Group();
        mProgressDialog = new ProgressDialog(this);

        titleET = findViewById(R.id.title_edittext);
        descET = findViewById(R.id.desc_edittext);
        btnPost = findViewById(R.id.btn_post);

        findViewById(R.id.post_dialog_send_imageview).setOnClickListener(this);
        findViewById(R.id.post_dialog_select_imageview).setOnClickListener(this);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_dialog_send_imageview:
                sendPost();
                break;
            case R.id.post_dialog_select_imageview:
                selectImage();
                break;
        }
    }

    private void sendPost() {
        final String checkEmptyTitle = titleET.getText().toString();
        final String checkEmptyDesc = descET.getText().toString();


        if (!TextUtils.isEmpty(checkEmptyTitle) && !TextUtils.isEmpty(checkEmptyDesc)) {
            mProgressDialog.setMessage("Sending post...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();

            FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getEmail()
                    .replace(".", ","))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            Post mPost = new Post();
                            Group mGroup = new Group();

                            final String postId = FirebaseUtils.getUid();

                            mPost.setUser(user);
                            mPost.setNumComments(0);
                            mPost.setNumUpvotes(0);
                            mPost.setNumDownvotes(0);
                            mPost.setTimeCreated(System.currentTimeMillis());
                            mPost.setPostId(postId);
                            mPost.setPostTitle(checkEmptyTitle);
                            mPost.setPostDesc(checkEmptyDesc);

                            mGroup.setPost(mPost);

                            String groupId = getIntent()
                                    .getExtras().getString("groupId");

                            FirebaseUtils.getGroupCreatedRef(groupId)
                                    .child(Constant.POST_KEY)
                                    .child(postId).setValue(mPost);

                            mProgressDialog.dismiss();
                            finish();

                         /*  if (mSelectedUri != null) {
                                FirebaseUtils.getImageSRef()
                                        .child(mSelectedUri.getLastPathSegment())
                                        .putFile(mSelectedUri)
                                        .addOnSuccessListener(CreatePostActivity.this,
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        String url = Constant.POST_IMAGES + "/" + mSelectedUri.getLastPathSegment();
                                                        mPost.setPostImageUrl(url);
                                                        createPost(postId);
                                                    }
                                                });
                            } else {

                            }*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            Toast.makeText(this, "You left either the title box or description box blank. Please fill everything up.", Toast.LENGTH_SHORT).show();
        }


    }

    private void createPost(String postId) {





        FirebaseUtils.getPostRef().child(postId)
                .setValue(mPost);


        FirebaseUtils.getMyPostRef().child(postId).setValue(true)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                    }
                });

        FirebaseUtils.addToMyRecord(Constant.POST_KEY, postId);
        finish();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                mSelectedUri = data.getData();
                mPostDisplay.setImageURI(mSelectedUri);
            }
        }
    }
}
