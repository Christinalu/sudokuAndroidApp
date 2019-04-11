package com.omicron.android.cmpt276_1191e1_omicron.Controller;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.omicron.android.cmpt276_1191e1_omicron.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity
{
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private Calendar calendar, calendar2, calendar3;
    private ArrayAdapter<String> adapter;
    private int ifFinished;
    private String packageName;
    private String activityDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent extra = getIntent();

        /*****************Issues here, cannot read the state from gameActivity*******************/
        if(extra.getSerializableExtra("ifFinished") != null) {
            ifFinished = (int) extra.getSerializableExtra("ifFinished");
        }
        else{
            ifFinished = 0;
        }

        packageName = (String) extra.getSerializableExtra("packageName");
        activityDate = (String) extra.getSerializableExtra("ActivityDate");

        /***********************For testing************************/
        List<EventDay> events = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,2);
        events.add(new EventDay(calendar, R.drawable.simple_circle));

        calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 5);
        events.add(new EventDay(calendar2, R.drawable.simple_circle));

        calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_MONTH, 7);
        events.add(new EventDay(calendar3, R.drawable.simple_circle));

        calendarView = findViewById(R.id.calendar_view);
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

                /**********************Still need to fix logic problems here**********************/
                /* To do: make click date equal to all dates which have events */
                if(clicked.equals(activityDate)) {
                    if(ifFinished == 2){ //finished the sudoku
                        listItems.add("You have accomplished " + packageName + " words package");
                    }
                    else{ //didn't finish or didn't finish it correctly
                        listItems.add("You didn't finish any words package");
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
