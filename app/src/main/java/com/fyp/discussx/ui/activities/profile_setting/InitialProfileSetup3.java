package com.fyp.discussx.ui.activities.profile_setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fyp.discussx.R;

import java.util.ArrayList;
import java.util.List;

public class InitialProfileSetup3 extends AppCompatActivity {

    private Spinner spinnerCourse;

    //college courses
    private final List<String> preuList = new ArrayList<String>();
    private final List<String> vubbList = new ArrayList<String>();
    private final List<String> vumbaList = new ArrayList<String>();
    private final List<String> proAccList = new ArrayList<String>();
    private final List<String> sunwayDipList = new ArrayList<String>();





    //univeristy courses
    private final List<String> adtpList = new ArrayList<String>();
    private final List<String> celsList = new ArrayList<String>();
    private final List<String> soaList = new ArrayList<String>();
    private final List<String> sohList = new ArrayList<String>();
    private final List<String> somsList = new ArrayList<String>();
    private final List<String> sstList = new ArrayList<String>();
    private final List<String> sihdList = new ArrayList<String>();
    private final List<String> subsList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_profile_setup3);
        setTitle("Set Up Your Profile");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinnerCourse = findViewById(R.id.spinner_course);
        setCourse();

    }

    private void setCourse () {
        String campus = getIntent().getExtras().getString("campus");
        String academicSchool = getIntent().getExtras().getString("academicSchool");

        //college adapters
        ArrayAdapter <String> preuAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, preuList);
        ArrayAdapter <String> vubbAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, vubbList);
        ArrayAdapter <String> vumbaAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, vumbaList);
        ArrayAdapter <String> proAccAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, proAccList);
        ArrayAdapter <String> sunwayDipAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, sunwayDipList);

        //university adapters
        ArrayAdapter <String> adtpAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, adtpList);
        ArrayAdapter <String> celsAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, celsList);
        ArrayAdapter <String> soaAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, soaList);
        ArrayAdapter <String> sohAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, sohList);
        ArrayAdapter <String> somsAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, somsList);
        ArrayAdapter <String> sstAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, sstList);;
        ArrayAdapter <String> sihdAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, sihdList);
        ArrayAdapter <String> subsAdapter = new ArrayAdapter<String>(InitialProfileSetup3.this, R.layout.support_simple_spinner_dropdown_item, subsList);

        //pre-u
        preuList.add("Australian Matriculation Programme (AUSMAT)");
        preuList.add("Cambridge A-Level Programme");
        preuList.add("Canadian International Matriculation Programme");
        preuList.add("Monash University Foundation Year");
        preuList.add("Sunway Foundation in Arts");
        preuList.add("Sunway Foundation in Science & Technology");

        //vubb
        vubbList.add("Victoria University Bachelor of Business");

        //vumba
        vumbaList.add("Master in Business Administration");
        vumbaList.add("Master of Business (Enterprise Resosurce Planning Systems");

        //proAcc
        proAccList.add("Association of Charted Certified Accountants (ACCA)");
        proAccList.add("The Institute of Chartered Accountants in England and Wales (ICAEW)");

        //sunwayDip
        sunwayDipList.add("Diploma in Communication");
        sunwayDipList.add("Diploma in Finance");
        sunwayDipList.add("Diploma in Professional Accouting");


        //adtp
        adtpList.add("Acturial Science");
        adtpList.add("Business");
        adtpList.add("Communication");
        adtpList.add("Computer Science");
        adtpList.add("Engineering");
        adtpList.add("Foensic Science");
        adtpList.add("Psychology");

        //cels
        celsList.add("IEP & IELTS Preparatory Course");

        //soa
        soaList.add("Diploma in Graphic and Multlimedia Design");
        soaList.add("Diploma in Interior Design");
        soaList.add("Diploma in Performing Arts");
        soaList.add("BA (Hons) Communication");
        soaList.add("BA (Hons) Contemporary Music (Audio Technology)");
        soaList.add("BA (Hons) Design Communication");
        soaList.add("BA (Hons) Digital Film Production");
        soaList.add("BA (Hons) in Interior Architecture");

        //soh
        sohList.add("Diploma in Culinary Arts");
        sohList.add("Diploma in Events Management");
        sohList.add("Diploma in Hotel Management");
        sohList.add("BSc (Hons) in Conventions and Events Management");
        sohList.add("BSc (Hons) in Culinary Management");
        sohList.add("BSc (Hons) in International Hospitality Management");

        //som
        somsList.add("BSc (Hons) Actuarial Studies");

        //sst
        sstList.add("Diploma in Information Technology"); //0
        sstList.add("BSc(Hons) Biology with Psychology"); //1
        sstList.add("BSc(Hons) Medical Biotechnology");
        sstList.add("BSc(Hons) in Psychology");
        sstList.add("BSc(Hons) in Computer Science");
        sstList.add("BSc(Hons) Information Systems");
        sstList.add("BSc(Hons) Information Systems (Business Analytics)");
        sstList.add("BSc(Hons) in Information Technology");
        sstList.add("BSc(Hons) in Information Technology (Computer Networking and Security)");
        sstList.add("BSc(Hons) in Mobile Computing with Entrepreneurship");
        sstList.add("Bachelor of Software Engineering (Hons)");
        sstList.add("MSc in Life Sciences");
        sstList.add("MSc in Psychology");
        sstList.add("MSc in Computer Science (By Research)");
        sstList.add("MSc in Information Systems");
        sstList.add("Doctor of Philosophy in Biology");
        sstList.add("Doctor of Philosophy (Computing)");

        //sihd
        sihdList.add("Diploma in Nursing");

        //subs
        subsList.add("Diploma in Business Administration");
        subsList.add("BSc (Hons) Accounting & Finance");
        subsList.add("BSc (Hons) Business Management");
        subsList.add("BSc (Hons) Business Studies");
        subsList.add("BSc (Hons) Financial Analysis");
        subsList.add("BSc (Hons) Financial Economics");
        subsList.add("BSc (Hons) Marketing");
        subsList.add("BSc (Hons) Global Supply Chain Management");
        subsList.add("BSc (Hons) in International Business");
        subsList.add("BA (Hons) Entrepreneurship");
        subsList.add("Master of Business Administration");
        subsList.add("Doctor of Philosophy (Business)");

        switch (academicSchool) {

            //university
            case "American Degree Transfer Program":
                spinnerCourse.setAdapter(adtpAdapter);
                break;

            case "Centre for English Language Studies":
                spinnerCourse.setAdapter(celsAdapter);
                break;

            case "School of Arts":
                spinnerCourse.setAdapter(soaAdapter);
                break;

            case "School of Hospitality":
                spinnerCourse.setAdapter(sohAdapter);
                break;

            case "School of Mathematical Sciences":
                spinnerCourse.setAdapter(somsAdapter);
                break;

            case "School of Science and Technology":
                spinnerCourse.setAdapter(sstAdapter);
                break;

            case "Sunway Institute for Healthcare Development":
                spinnerCourse.setAdapter(sihdAdapter);
                break;

            case "Sunway University Business School":
                spinnerCourse.setAdapter(subsAdapter);
                break;

            //college
            case "Pre-U":
                spinnerCourse.setAdapter(preuAdapter);
                break;

            case "Victoria University Bachelor of Business":
                spinnerCourse.setAdapter(vubbAdapter);
                break;

            case "Victoria University MBA Programme":
                spinnerCourse.setAdapter(vumbaAdapter);
                break;

            case "Professional Accounting Programmes":
                spinnerCourse.setAdapter(proAccAdapter);
                break;

            case "Sunway Diploma Studies":
                spinnerCourse.setAdapter(sunwayDipAdapter);
                break;

        }

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
