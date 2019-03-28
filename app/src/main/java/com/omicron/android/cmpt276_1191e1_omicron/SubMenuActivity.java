package com.omicron.android.cmpt276_1191e1_omicron;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SubMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        //Level spinner
        Spinner spinnerLevel = findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,
                R.array.arrLevel, android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);
        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });
        //Language spinner
        Spinner spinnerLanguage = findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(this,
                R.array.arrLanguage, android.R.layout.simple_spinner_item);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapterLanguage);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });

        //Mode spinner
        Spinner spinnerMode = findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> adapterMode= ArrayAdapter.createFromResource(this,
                R.array.arrMode, android.R.layout.simple_spinner_item);
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(adapterMode);
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ew
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });

        //Size spinner
        Spinner spinnerSize = findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> adapterSize= ArrayAdapter.createFromResource(this,
                R.array.arrPuzzleSize, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSize);
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });


        }




}
