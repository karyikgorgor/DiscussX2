package com.fyp.discussx.ui.activities.profile_setting;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class InitialProfileSetup2 extends AppCompatActivity {

    private Button btnNext;
    private Spinner campusSpinner;
    private final List<String> campusList = new ArrayList<String>();
    private Spinner spinnerAcademicSchool;
    private final List<String> uniAcademicSchoolList = new ArrayList<String>();
    private final List<String> collegeAcademicSchoolList = new ArrayList<String>();
    
    private String academicSchoolChoice;
    
    private String campusChoice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_profile_setup2);
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
                switch (i) {
                    case 0:
                        academicSchoolChoice = uniAcademicSchoolList.get(0);
                        break;
                    case 1:
                        academicSchoolChoice = uniAcademicSchoolList.get(1);
                        break;
                    case 2:
                        academicSchoolChoice = uniAcademicSchoolList.get(2);
                        break;
                    case 3:
                        academicSchoolChoice = uniAcademicSchoolList.get(3);
                        break;
                    case 4:
                        academicSchoolChoice = uniAcademicSchoolList.get(4);
                        break;
                    case 5:
                        academicSchoolChoice = uniAcademicSchoolList.get(5);
                        break;
                    case 6:
                        academicSchoolChoice = uniAcademicSchoolList.get(6);
                        break;
                    case 7:
                        academicSchoolChoice = uniAcademicSchoolList.get(7);
                        break;

                }
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
                switch (position) {
                    case 0:
                        academicSchoolChoice = collegeAcademicSchoolList.get(0);
                        break;
                    case 1:
                        academicSchoolChoice = collegeAcademicSchoolList.get(1);
                        break;
                    case 2:
                        academicSchoolChoice = collegeAcademicSchoolList.get(2);
                        break;
                    case 3:
                        academicSchoolChoice = collegeAcademicSchoolList.get(3);
                        break;
                    case 4:
                        academicSchoolChoice = collegeAcademicSchoolList.get(4);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void passData () {
        Intent intent = new Intent (InitialProfileSetup2.this, InitialProfileSetup3.class);
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

}
