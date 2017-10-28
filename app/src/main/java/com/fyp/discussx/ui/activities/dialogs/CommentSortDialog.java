package com.fyp.discussx.ui.activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fyp.discussx.R;
import com.fyp.discussx.model.Comment;
import com.fyp.discussx.model.Post;
import com.fyp.discussx.ui.activities.PostActivity;
import com.fyp.discussx.utils.Constant;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.database.Query;


public class CommentSortDialog extends DialogFragment {
    private Post mPost;
    private PostActivity mPostActivity;
    private CharSequence [] options = {"Most Upvoted", "Most Downvoted", "Most Argumentative","Oldest", "Newest"};
    @Override
    @NonNull
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Comment Sort Options").setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Toast.makeText(getActivity(), "Sorted", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                    Toast.makeText(getActivity(), "Choose something", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return builder.create();

    }



}
