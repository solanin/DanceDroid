package com.ash.ervin.dancedroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;

public class MyActivity extends AppCompatActivity {

    // Time vars
    Timer timer;

    // Buttons
    ArrayList<Button> buttonList = new ArrayList<>();
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;
    Button six;
    Button seven;
    Button eight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        addButtons();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addClickListeners();
        four.setBackgroundColor(Color.BLUE);

    }
    //creates buttons and puts them in the button list
    //in a slow way that should probably be optimized somehow
    private void addButtons(){
        one = (Button)findViewById(R.id.button_one);
        two = (Button) findViewById(R.id.button_two);
        three = (Button) findViewById(R.id.button_three);
        four = (Button) findViewById(R.id.button_four);
        five = (Button) findViewById(R.id.button_five);
        six = (Button) findViewById(R.id.button_six);
        seven = (Button) findViewById(R.id.button_seven);
        eight = (Button) findViewById(R.id.button_eight);

        buttonList.add(one);
        buttonList.add(two);
        buttonList.add(three);
        buttonList.add(four);
        buttonList.add(five);
        buttonList.add(six);
        buttonList.add(seven);
        buttonList.add(eight);
    }

    private void turnOffBtn(Button button){
        button.setBackgroundColor(Color.GRAY);
    }

    private void turnOnBtn(Button button){
        button.setBackgroundColor(Color.BLUE);
    }

    private void addClickListeners() {
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "One pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Two pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Three pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Four pressed",
                        Toast.LENGTH_SHORT).show();
                four.setBackgroundColor(Color.GRAY);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Five pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Six pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Seven pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,
                        "Eight pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
