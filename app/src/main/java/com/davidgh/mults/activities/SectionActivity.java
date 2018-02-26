package com.davidgh.mults.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.davidgh.mults.R;

public class SectionActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mToolbar = (Toolbar) findViewById(R.id.section_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sectionTitle = getIntent().getStringExtra("section_name");
        mToolbar.setTitle(sectionTitle);
    }

}
