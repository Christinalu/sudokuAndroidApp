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
import com.omicron.android.cmpt276_1191e1_omicron.Model.EventMap;
import com.omicron.android.cmpt276_1191e1_omicron.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class EventActivity extends AppCompatActivity
{
    private CalendarView calendarView;
    private Calendar calendarEvent;
    private ListView listView;

    private ArrayAdapter<String> adapter, empty;
    private int ifFinished;
    private int progress;
    private String activityDates, packageName, modeName;
    private List<String> listItems = new ArrayList<String>(), emptyList = new ArrayList<String>();
    public String event1,event2;

    private String[] dateContent, stateContent;
    private EventMap eventMap = new EventMap();
    private EventMap eventMap_read = new EventMap();
//    private HashMap<String, Integer> prgToPkg = new HashMap<>();
//    private HashMap<String, Integer> modeToState = new HashMap<>();
//    private HashMap<HashMap<String, Integer>,HashMap<String, Integer>> prgPkgTomodeState = new HashMap<>();
//    private HashMap<String, HashMap<HashMap<String, Integer>,HashMap<String, Integer>>> dateTopkg = new HashMap<>();
//    private HashMap<String, HashMap<HashMap<String, Integer>,HashMap<String, Integer>>> readDatePkg = null;
    private List<Integer> state_buffer = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendar_view);
        listView = findViewById(R.id.list_view);
        String date_output = "date_out.txt";
        String all_output = "dateAndPkg.bin";

        /********************** Getting values from mainActivity ******************/
        Intent extra = getIntent();

        activityDates = (String) extra.getSerializableExtra("ActivityDate");
        writeFile(date_output, activityDates);

        //package Name
        packageName = (String) extra.getSerializableExtra("PackageName");

        //progress percentage of corresponding pkg
        double percentage = (Double) extra.getSerializableExtra("Progress");
        progress =(int) percentage;

        eventMap.setPrgToPkg(packageName, progress);

        //chosen mode name
        modeName = (String) extra.getSerializableExtra("modeSelect");

        //if finished state
        if(extra.getSerializableExtra("ifFinished") != null) {
            ifFinished = (int) extra.getSerializableExtra("ifFinished");
            state_buffer.add(ifFinished);
        }
        else{
            ifFinished = 0;
            state_buffer.add(ifFinished);
        }

        dateContent = readFile(date_output);

        eventMap.setModeToState(modeName, ifFinished);

        eventMap.setPrgPkgTomodeState();
        for(int i =0; i < dateContent.length; i++) {
            eventMap.setDateTopkg(dateContent[i]);
        }

        //connect pkgName with its progress
//        prgToPkg.put(packageName, progress);
        System.out.println("prgtopjkg:"+ eventMap.getPrgToPkg() );
//        //connect modeName with its state
//        modeToState.put(modeName, ifFinished);
        System.out.println("modetostate"+ eventMap.getModeToState());
//        //connect the pkgAndProgress with its
//        prgPkgTomodeState.put(prgToPkg, modeToState);
        System.out.println("prgPkgTomodeState"+ eventMap.getPrgPkgTomodeState());
//        //connect date to the whole content
//        dateTopkg.put(activityDates, prgPkgTomodeState);
        System.out.println("dateTopkg"+ eventMap.getDateTopkg());


        /************** Write all data into file *************/
        try {
            Log.d("If write in?", "0");
            FileOutputStream fos = getApplicationContext().openFileOutput(all_output, Context.MODE_PRIVATE);
            Log.d("If write in?", "1");
            ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(fos));
            Log.d("If write in?", "2");
            oos.writeObject(eventMap);
            Log.d("If write in?", "3");
            oos.flush();
            Log.d("If write in?", "4");
            oos.close();
            Log.d("If write in?", "5");
            fos.close();
            Log.d("If write in?", "6");
        } catch (IOException e) {
            Log.d("IF write in?", "NO, ERROR INITIALIZING STREAM");
            e.printStackTrace();
        }


        /*************** read from files ***************/


        FileInputStream inputStream = null;
        try {
            Log.d("If read in?", "0");
            inputStream = openFileInput(all_output);
            Log.d("If read in?", "1");
            ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(inputStream));
            Log.d("If read in?", "2");
            Object readObject = ois.readObject();
            Log.d("If read in?", "3");
            ois.close();
            Log.d("If read in?", "4");
            eventMap_read = (EventMap) readObject;
            Log.d("If read in?", "5");
            inputStream.close();
            Log.d("If read in?", "6");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("If read in?", "NONO");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("If read in?", "CLASS NOT FOUND");
        }

        markEventsOn();

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

                if(eventMap_read!=null) {
                    Log.d("eventMap_read", "not null");
                    for (String dates : eventMap_read.getDateTopkg().keySet()) {
                        Log.d("eventMap_read", "0");
                        if (clicked.equals(dates)) {
                            Log.d("eventMap_read", "1");
                            HashMap<HashMap<String, Integer>, HashMap<String, Integer>> temp = eventMap_read.getDateTopkg().get(dates);
                            for (HashMap<String, Integer> proPkg : temp.keySet()) {
                                Log.d("eventMap_read", "2");
                                HashMap<String, Integer> modeState = temp.get(proPkg);
                                for (String pkg : proPkg.keySet()) {
                                    Log.d("eventMap_read", "3");
                                    String pkg_temp = pkg;
                                    int prog_temp = proPkg.get(pkg);
                                    for (String mode : modeState.keySet()) {
                                        Log.d("eventMap_read", mode);
                                        if(mode.equals("Mini Game")){
                                            Log.d("eventMap_read", "5");
                                            event1 = "You tried our Mini Game mode with the " + pkg_temp + " words package!";
                                            if(listItems.stream().noneMatch(event1::contains)){
                                                listItems.add(event1);
                                            }
                                            listView.setAdapter(adapter);
                                        } else {
                                            Log.d("modeState", modeState.get(mode).toString());
                                            if (state_buffer.contains(2)) {
                                                Log.d("eventMap_read", "7");
                                                event1 = "You have accomplished " + prog_temp + "% " + pkg_temp + " words package with the " + mode + " mode";
                                                if(listItems.stream().noneMatch(event1::contains)){
                                                    listItems.add(event1);
                                                }
                                                System.out.println("contains true");
                                                listView.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                            listView.setAdapter(empty);
                        }
                    }
                }
            }
        });

    }

    public String[] readFile(String fileName){
        FileInputStream inputStream = null;
        String content;
        String[] readString = new String[0];
        try{
            inputStream = openFileInput(fileName);
            int len = inputStream.available();
            readString = new String[len];
            Log.d("length of inputstream", Integer.toString(len));
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
            FileOutputStream fos = openFileOutput(fName, Context.MODE_PRIVATE);
            fos.write(eventString.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void markEventsOn(){
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

    private void saveStringToPreferences(String event){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("event label", event);
        editor.apply();
    }

    //This is for save the Events done in "today"
    private void saveEvent(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String previousText = preferences.getString("event label","");
        if(!TextUtils.isEmpty(previousText)){
            listItems.add(event1);
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
