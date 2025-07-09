package com.java.wangyiding.ui.search;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.java.wangyiding.R;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity  {

    private TextView startTimeText;
    private TextView endTimeText;
    private Button confirmButton;
    private Button backButton;
    private Button startTimeButton;
    private Button endTimeButton;
    private EditText keyWordText;
    private Spinner categorySpinner;
    private String startTime;
    private String endTime;
    private String category="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backButton=findViewById(R.id.SearchBackButton);
        confirmButton=findViewById(R.id.confirmButton);
        startTimeButton=findViewById(R.id.StartTimeButton);
        endTimeButton=findViewById(R.id.endTimeButton);
        keyWordText=findViewById(R.id.keyWord);
        categorySpinner=findViewById(R.id.Category);
        startTimeText=findViewById(R.id.StartTimeText);
        endTimeText=findViewById(R.id.endTimeText);

        SimpleDateFormat sdf=new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        endTime=sdf.format(date);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        startTime=sdf.format(calendar.getTime());
        endTimeText.setText("终止时间：\n"+endTime);
        startTimeText.setText("起始时间：\n"+startTime);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected=adapterView.getItemAtPosition(i).toString();
                //Log.d("spinner",selected);
                if(selected.compareTo("选择类别")==0 || selected.compareTo("所有类别")==0)
                    category="";
                else
                    category=selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent();
                intent.putExtra("startTime",startTime);
                intent.putExtra("endTime",endTime);
                intent.putExtra("keyWord",keyWordText.getText().toString());
                intent.putExtra("category",category);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent();
                intent.putExtra("startTime",startTime);
                intent.putExtra("endTime",endTime);
                intent.putExtra("keyWord",keyWordText.getText().toString());
                intent.putExtra("category",category);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("st","button");
                int year=Integer.parseInt(startTime.substring(0,4));
                int month=Integer.parseInt(startTime.substring(5,7));
                int day=Integer.parseInt(startTime.substring(8,10));
                int hour=Integer.parseInt(startTime.substring(11,13));
                int minute=Integer.parseInt(startTime.substring(14,16));
                DatePickerDialog datePickerDialog=new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String formattedDate=String.format("%04d-%02d-%02d ",datePicker.getYear(),1+datePicker.getMonth(),datePicker.getDayOfMonth());
                        startTime=formattedDate+startTime.substring(11);
                        startTimeText.setText("起始时间：\n"+startTime);
                    }
                }, year, month-1, day);
                datePickerDialog.show();
                TimePickerDialog timePickerDialog=new TimePickerDialog(SearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String formattedTime=String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                        startTime=startTime.substring(0,11)+formattedTime+startTime.substring(16);
                        startTimeText.setText("起始时间：\n"+startTime);
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });


        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("st","button");
                int year=Integer.parseInt(endTime.substring(0,4));
                int month=Integer.parseInt(endTime.substring(5,7));
                int day=Integer.parseInt(endTime.substring(8,10));
                int hour=Integer.parseInt(endTime.substring(11,13));
                int minute=Integer.parseInt(endTime.substring(14,16));
                DatePickerDialog datePickerDialog=new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String formattedDate=String.format("%04d-%02d-%02d ",datePicker.getYear(),1+datePicker.getMonth(),datePicker.getDayOfMonth());
                        endTime=formattedDate+endTime.substring(11);
                        endTimeText.setText("终止时间：\n"+endTime);
                    }
                }, year, month-1, day);
                datePickerDialog.show();
                TimePickerDialog timePickerDialog=new TimePickerDialog(SearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String formattedTime=String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                        endTime=endTime.substring(0,11)+formattedTime+endTime.substring(16);
                        endTimeText.setText("终止时间：\n"+endTime);
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });
    }




}