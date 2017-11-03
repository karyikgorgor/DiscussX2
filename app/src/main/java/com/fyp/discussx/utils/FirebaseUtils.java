package com.fyp.discussx.utils;

import android.hardware.ConsumerIrManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by brad on 2017/02/05.
 */

public class FirebaseUtils {
    //I'm creating this class for similar reasons as the Constants class, and to make my code a bit
    //cleaner and more well managed.

    public static DatabaseReference getUserRef(String email){
        return FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_KEY)
                .child(email);
    }

    public static DatabaseReference getPostRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constant.POST_KEY);
    }

    public static DatabaseReference getPostLikedRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constant.POST_LIKED_KEY);
    }

    public static DatabaseReference getPostLikedRef(String postId){
        return getPostLikedRef().child(getCurrentUser().getEmail()
                .replace(".",","))
                .child(postId);
    }

    public static DatabaseReference getPostDownvotedRef() {
        return FirebaseDatabase.getInstance()
                .getReference(Constant.POST_DOWNVOTED_KEY);
    }

    public static DatabaseReference getPostDownvotedRef(String postId) {
        return getPostDownvotedRef().child(getCurrentUser().getEmail()
                .replace(".",","))
                .child(postId);
    }

    public static DatabaseReference getCommentLikedRef(){
        return FirebaseDatabase.getInstance()
                .getReference(Constant.COMMENT_LIKED_KEY);
    }

    public static DatabaseReference getCommentLikedRef(String postId, String commentId){
        return getCommentLikedRef().child(getCurrentUser().getEmail()
                .replace(".",","))
                .child(postId).child(commentId);
    }

    public static DatabaseReference getCommentDownvotedRef() {
        return FirebaseDatabase.getInstance()
                .getReference(Constant.COMMENT_DOWNVOTED_KEY);
    }

    public static DatabaseReference getCommentDownvotedRef(String postId, String commentId) {
        return getCommentDownvotedRef().child(getCurrentUser().getEmail()
                .replace(".",","))
                .child(postId).child(commentId);
    }

    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static StorageReference getImageSRef(){
        return FirebaseStorage.getInstance().getReference(Constant.POST_IMAGES);
    }

    public static DatabaseReference getMyPostRef(){
        return FirebaseDatabase.getInstance().getReference(Constant.MY_POSTS)
                .child(getCurrentUser().getEmail().replace(".",","));
    }

    public static DatabaseReference getCommentRef(String postId){
        return FirebaseDatabase.getInstance().getReference(Constant.COMMENTS_KEY)
                .child(postId);
    }

    public static DatabaseReference getGroupCreatedRef () {
        return  FirebaseDatabase.getInstance().getReference(Constant.GROUP_CREATED_KEY);
    }

    public static DatabaseReference getGroupCreatedRef (String groupId) {
        return getGroupCreatedRef().child(groupId);
    }

    public static DatabaseReference getGroupJoinedRef () {
        return  FirebaseDatabase.getInstance().getReference(Constant.GROUP_JOINED_KEY);
    }

    public static DatabaseReference getGroupJoinedRef (String groupId) {
        return  getGroupJoinedRef().child(groupId);
    }

    public static DatabaseReference getGroupJoinedFromUserRecordRef () {
        DatabaseReference x = FirebaseDatabase.getInstance().getReference(Constant.USER_RECORD);
        if (FirebaseUtils.getCurrentUser() != null) {
            x = FirebaseDatabase.getInstance().getReference(Constant.USER_RECORD)
                    .child(getCurrentUser().getEmail().replace(".",",")).child(Constant.GROUP_JOINED_KEY);
        }
        return x;
    }


    public static DatabaseReference getGroupMembersRef () {
        return FirebaseDatabase.getInstance().getReference(Constant.GROUP_MEMBER);
    }

    public static DatabaseReference getGroupMembersRef (String groupId) {
        return getGroupMembersRef().child(groupId);
    }

    public static DatabaseReference getMyRecordRef(){
        return FirebaseDatabase.getInstance().getReference(Constant.USER_RECORD)
                .child(getCurrentUser().getEmail().replace(".",","));
    }

    public static void addToMyRecord(String node, final String id){
        getMyRecordRef().child(node).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ArrayList<String> myRecordCollection;
                if(mutableData.getValue() == null){
                    myRecordCollection = new ArrayList<String>(1);
                    myRecordCollection.add(id);
                }else{
                    myRecordCollection = (ArrayList<String>) mutableData.getValue();
                    myRecordCollection.add(id);
                }

                mutableData.setValue(myRecordCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void addRecord (String constant, final String id, final String name) {
        getMyRecordRef().child(constant).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child(id).setValue(name);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static DatabaseReference getPostFromGroupRef (String groupId) {
        return FirebaseDatabase.getInstance().getReference(Constant.GROUP_CREATED_KEY).child(groupId)
                .child(Constant.POST_KEY);
    }

}