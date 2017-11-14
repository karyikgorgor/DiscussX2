package com.fyp.discussx.ui.activities.profile_setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fyp.discussx.R;
import com.fyp.discussx.ui.activities.CreateGroupActivity;
import com.fyp.discussx.ui.activities.JoinGroupActivity;

public class ProfileSetupComplete extends AppCompatActivity {
    private Button joinGroup, createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_complete);

        joinGroup = findViewById(R.id.join_group);
        createGroup = findViewById(R.id.create_group);

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ProfileSetupComplete.this, JoinGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ProfileSetupComplete.this, CreateGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
