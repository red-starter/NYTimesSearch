package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.codepath.nytimessearch.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    CheckBox cbSports;
    CheckBox cbArt;
    CheckBox cbFashion;
    DatePicker dpBeginDate;
    Spinner spSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupViews();
    }

    public void setupViews(){
        cbSports = (CheckBox) findViewById(R.id.cbSports);
        cbArt = (CheckBox) findViewById(R.id.cbArt);
        cbFashion = (CheckBox) findViewById(R.id.cbFashion);

        dpBeginDate = (DatePicker) findViewById(R.id.dpBeginDate);
        spSortOrder = (Spinner) findViewById(R.id.spSortOrder);

    }


    public void onSaveFilter(View view){
        //        String sortOrder = spSortOrder.getT
        //        cbSports.isChecked()
        Calendar calendar = Calendar.getInstance();

        calendar.set(dpBeginDate.getYear(), dpBeginDate.getMonth(), dpBeginDate.getDayOfMonth());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        Intent data = new Intent();

        ArrayList newsDeskArrayList = new ArrayList<>();
        if (cbFashion.isChecked()){
            newsDeskArrayList.add("\""+cbFashion.getText()+"\"");
        }
        if (cbArt.isChecked()){
            newsDeskArrayList.add("\""+cbArt.getText()+"\"");
        }
        if (cbSports.isChecked()){
            newsDeskArrayList.add("\""+cbSports.getText()+"\"");
        }
        // Pass relevant data back as a result
        data.putExtra("begin_date", format.format(calendar.getTime()));
        data.putExtra("sort", spSortOrder.getSelectedItem().toString());
        data.putExtra("newsDesk", TextUtils.join("%20", newsDeskArrayList));


        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}
