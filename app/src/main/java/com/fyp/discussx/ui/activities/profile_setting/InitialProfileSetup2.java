package com.fyp.discussx.ui.activities.profile_setting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.fyp.discussx.R;
import com.fyp.discussx.ui.activities.RegisterActivity;
import com.fyp.discussx.utils.BaseActivity;
import com.fyp.discussx.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InitialProfileSetup2 extends BaseActivity {

    private Button btnNext;
    private Spinner campusSpinner;
    private final List<String> campusList = new ArrayList<String>();
    private Spinner spinnerAcademicSchool;
    private final List<String> uniAcademicSchoolList = new ArrayList<String>();
    private final List<String> collegeAcademicSchoolList = new ArrayList<String>();
    private DatabaseReference mUserRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ValueEventListener mUserValueEventListener;
    
    private String academicSchoolChoice;
    
    private String campusChoice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_profile_setup2);

       // init();

        setTitle("Set Up Your Profile");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        campusSpinner = findViewById(R.id.spinner_uni);
        spinnerAcademicSchool = findViewById(R.id.spinner_academic_school);
        btnNext = findViewById(R.id.btn_next);

        selectCampus();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passData();
            }
        });

    }
    private void init() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(InitialProfileSetup2.this, RegisterActivity.class));
                }
            }
        };

        if (mFirebaseUser != null) {
            mUserRef = FirebaseUtils.getUserRef(mFirebaseUser.getEmail().replace(".", ","));
        }
    }


    private void selectCampus () {
        campusList.add("Sunway College");
        campusList.add("Sunway University");

        uniAcademicSchoolList.add("American Degree Transfer Program"); //0
        uniAcademicSchoolList.add("Centre for English Language Studies"); //1
        uniAcademicSchoolList.add("School of Arts"); //2
        uniAcademicSchoolList.add("School of Hospitality"); //3
        uniAcademicSchoolList.add("School of Mathematical Sciences"); //4
        uniAcademicSchoolList.add("School of Science and Technology"); //5
        uniAcademicSchoolList.add("Sunway Institute for Healthcare Development"); //6
        uniAcademicSchoolList.add("Sunway University Business School"); //7

        collegeAcademicSchoolList.add("Pre-U");
        collegeAcademicSchoolList.add("Victoria University Bachelor of Business");
        collegeAcademicSchoolList.add("Victoria University MBA Programme");
        collegeAcademicSchoolList.add("Professional Accounting Programmes");
        collegeAcademicSchoolList.add("Sunway Diploma Studies");


        final ArrayAdapter <String> universityAdapter = new ArrayAdapter<String>(InitialProfileSetup2.this, R.layout.support_simple_spinner_dropdown_item, uniAcademicSchoolList);
        final ArrayAdapter <String> collegeAdapter = new ArrayAdapter<String>(InitialProfileSetup2.this, R.layout.support_simple_spinner_dropdown_item, collegeAcademicSchoolList);
        ArrayAdapter <String> campusAdapter = new ArrayAdapter<String>(InitialProfileSetup2.this, R.layout.support_simple_spinner_dropdown_item, campusList);

        campusSpinner.setAdapter(campusAdapter);

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                campusChoice = campusList.get(i);
                switch(i) {
                    case 0: 
                        spinnerAcademicSchool.setAdapter(collegeAdapter);
                        setCollegeAcademicSchool();
                        break;
                    case 1:
                        spinnerAcademicSchool.setAdapter(universityAdapter);
                        setUniAcademicSchool();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please choose an university!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUniAcademicSchool () {
        spinnerAcademicSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    academicSchoolChoice = uniAcademicSchoolList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please choose an academic school!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setCollegeAcademicSchool () {
        spinnerAcademicSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                academicSchoolChoice = collegeAcademicSchoolList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void passData () {
        String dob = getIntent().getExtras().getString("dob");
        String gender = getIntent().getExtras().getString("gender");

        Intent intent = new Intent (InitialProfileSetup2.this, InitialProfileSetup3.class);
        intent.putExtra("dob", dob);
        intent.putExtra("gender", gender);
        intent.putExtra("campus", campusChoice);
        intent.putExtra("academicSchool", academicSchoolChoice);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_initial_profile_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 /*  @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        if (mUserRef != null) {
            mUserRef.addValueEventListener(mUserValueEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null)
            mAuth.removeAuthStateListener(mAuthStateListener);
        if (mUserRef != null)
            mUserRef.removeEventListener(mUserValueEventListener);
    }*/

}
