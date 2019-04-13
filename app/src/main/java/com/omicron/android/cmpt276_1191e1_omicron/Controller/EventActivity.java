package com.omicron.android.cmpt276_1191e1_omicron.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.CalendarView;
import com.omicron.android.cmpt276_1191e1_omicron.R;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity
{
    private CalendarView calendarView;
    private Calendar calendarEvent;
    private ListView listView;

    private ArrayAdapter<String> adapter, empty;
    private int ifFinished;
    private double progress;
    private String activityDates, packageName, modeName;
    private List<String> listItems = new ArrayList<String>(), emptyList = new ArrayList<String>();
    public String event1, event2, event3, event4, event5;

    private String date_output = "date_out.txt";
    private String pkg_output = "pkg_out.txt";
    private String mode_output = "mode_out.txt";
    private String prg_output = "prg_out.txt";
    private String state_output = "state_out.txt" ;
    private String[] dateContent, pkgContent, modeContent, prgContent, stateContent;

    //NOT necessary
    private ArrayList<String> dates_buffer = new ArrayList<>();
    //............
    private ArrayList<String> pack_buffer = new ArrayList<>();
    private ArrayList<String> mode_buffer = new ArrayList<>();
    private ArrayList<String> progress_percent = new ArrayList<>();
    private ArrayList<Integer> state_Buffer = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendar_view);
        listView = findViewById(R.id.list_view);

        // data from Past days
        dateContent = readFile(date_output);
        pkgContent = readFile(pkg_output);
        modeContent = readFile(mode_output);
        prgContent = readFile(prg_output);
        stateContent = readFile(state_output);

        /********************** Getting values from mainActivity ******************/
        Intent extra = getIntent();
        if(extra.getSerializableExtra("ifFinished") != null) {
            ifFinished = (int) extra.getSerializableExtra("ifFinished");
            writeFile(state_output, Integer.toString(ifFinished));
            state_Buffer.add(ifFinished);
        }
        else{
            ifFinished = 0;
            state_Buffer.add(ifFinished);
        }

        progress = (Double) extra.getSerializableExtra("Progress");
        progress_percent.add(Double.toString(progress));
        writeFile(prg_output, Double.toString(progress));

        packageName = (String) extra.getSerializableExtra("PackageName");
        pack_buffer.add(packageName);
        Log.d("package name: ", pack_buffer.toString());
        writeFile(pkg_output, packageName);

        //NOT necessary
        activityDates = (String) extra.getSerializableExtra("ActivityDate");
        dates_buffer.add(activityDates);
        Log.d("dates: ", dates_buffer.toString());
        writeFile(date_output, activityDates);
        //............

        modeName = (String) extra.getSerializableExtra("modeSelect");
        mode_buffer.add(modeName);
        Log.d("mode select: ", mode_buffer.toString());
        writeFile(mode_output, modeName);


//        List<EventDay> events = new ArrayList<>();
//        for(int i = 0; i < dates_buffer.size(); i++) {
//            //getting the date to calendar
//            Calendar activity_date = Calendar.getInstance();
//            try {
//                Date temp = dateFormat.parse(dates_buffer.get(i));
//                activity_date.setTime(temp);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            calendarEvent = activity_date; //mark this day
//            events.add(new EventDay(calendarEvent, R.drawable.simple_circle));
//            calendarView.setEvents(events);
//        }
        /************** I'm still working on this func**************/
        addEventsOn();

        //Two listView adapters
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems);
        empty = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptyList);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDate = eventDay.getCalendar();
                Date click = clickedDate.getTime();
                String clicked = dateFormat.format(click);
                Log.d("click date", clicked);
                /* To do: make click date equal to all dates which have events */
                if(dates_buffer.contains(clicked)) {
                    System.out.println("contains true");
                    listView.setAdapter(adapter);
                }
                else {
                    listView.setAdapter(empty);
                }
            }
        });

    }

    private void saveStringToPreferences(String event){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("event label", event);
        editor.apply();
    }

    public int getMaxSize(){
        if(pack_buffer.size()>mode_buffer.size() && pack_buffer.size()>progress_percent.size()){
            return pack_buffer.size();
        }
        else if(mode_buffer.size()>pack_buffer.size() && mode_buffer.size()>progress_percent.size()){
            return mode_buffer.size();
        }
        else
            return progress_percent.size();
    }

    public String[] readFile(String fileName){
        FileInputStream inputStream = null;
        String content;
        String[] readString = new String[0];
        try{
            inputStream = openFileInput(fileName);
            int len = inputStream.available();
            readString = new String[len];
            /************** file read **************/
            InputStreamReader inStreamRead = new InputStreamReader(inputStream);
            BufferedReader buffRead = new BufferedReader(inStreamRead);
            while((content = buffRead.readLine())!=null){
                readString = content.split(",");
            }
            Log.d("file content read", content);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readString;
    }

    public void writeFile(String fName, String eventString){
        try {
            FileOutputStream fos = openFileOutput(fName, Context.MODE_APPEND);
            fos.write(eventString.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: need to add events on corresponding date
    public void addEventsOn(){

        List<EventDay> events = new ArrayList<>();
        for(int i = 0; i < dateContent.length; i++) {
            //getting the date to calendar
            Calendar activity_date = Calendar.getInstance();
            try {
                Date temp = dateFormat.parse(dateContent[i]);
                activity_date.setTime(temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendarEvent = activity_date; //mark this day
            events.add(new EventDay(calendarEvent, R.drawable.simple_circle));
            calendarView.setEvents(events);
        }

    }

    //This is for save the Events done in "today"
    private void saveEvent(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String previousText = preferences.getString("event label","");
        int x = 0, y = 0, z = 0;

        if(!TextUtils.isEmpty(previousText)){

            if( state_Buffer.contains(2)) { //finished at least one time of the sudoku
                for(int i = 0; i < getMaxSize(); i++) {
                    if(i < progress_percent.size()) x = i;
                    if(i < pack_buffer.size()) y = i;
                    if(i < mode_buffer.size()) z = i;
                    event1 = "You have accomplished "+ progress_percent.get(i) + "% " + pack_buffer.get(i) + " words package with the " + mode_buffer.get(i) + " mode";
                    listItems.add(event1);
                }
            }
            saveStringToPreferences(event1);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveEvent();
    }

    public void onResume(){
        super.onResume();
        saveEvent();
    }

}
