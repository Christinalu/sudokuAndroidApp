package com.omicron.android.cmpt276_1191e1_omicron.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.omicron.android.cmpt276_1191e1_omicron.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity
{
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private Calendar calendarEvent;
    private ArrayAdapter<String> adapter;
    private int ifFinished, modeNum;
    private String packageName, modeName;
    private String activityDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendar_view);
        Intent extra = getIntent();

        if(extra.getExtras().getSerializable("ifFinished") != null) {
            ifFinished = (int) extra.getExtras().getSerializable("ifFinished");
        }
        else{
            ifFinished = 0;
        }

        packageName = (String) extra.getSerializableExtra("PackageName");
        activityDate = (String) extra.getSerializableExtra("ActivityDate");
        modeNum = (int) extra.getSerializableExtra("modeSelect");
        switch (modeNum){
            case 0:
                modeName = "Standard Mode";
            case 1:
                modeName = "Listening Comprehension mode";
            case 3:
                modeName = "Mini Game mode";
        }

        Calendar activity_date = Calendar.getInstance();
        try {
            Date temp = dateFormat.parse(activityDate);
            activity_date.setTime(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<EventDay> events = new ArrayList<>();
        calendarEvent = activity_date;
        events.add(new EventDay(calendarEvent, R.drawable.simple_circle));

        calendarView.setEvents(events);

        final ListView listView = findViewById(R.id.list_view);
        final List<String> listItems = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDate = eventDay.getCalendar();
                Date click = clickedDate.getTime();
                String clicked = dateFormat.format(click);

                /**********************More stuff need to show here, need use hashMap**********************/
                /* To do: make click date equal to all dates which have events */
                if(clicked.equals(activityDate)) {
                    if(ifFinished == 2) { //finished the sudoku
                        listItems.add("You have accomplished " + packageName + " words package");
                        listItems.add("You practiced " + modeName + " mode");
                    }
                    else if (ifFinished == 1){ //didn't finish or didn't finish it correctly
                        listItems.add("You didn't finish any words package"+packageName);
                    }
                    else{
                        listItems.add("You never started the game");
                    }
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    listItems.clear();
                }
            }
        });

    }


}
