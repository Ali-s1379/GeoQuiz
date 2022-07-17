package com.example.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private TextView question;
    private Button buttonReveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        question = findViewById(R.id.textView_question2);
        Intent intent = getIntent();
        question.setText(intent.getIntExtra(MainActivity.QUESTION_RES_ID,0));
        buttonReveal = findViewById(R.id.button_reveal);
    }

    public void revealAnswer(View v){
        Intent intent = getIntent();
        String str = intent.getBooleanExtra(MainActivity.QUESTION_ANSWER,false) + "";
        buttonReveal.setText(str);
        MainActivity.setCheated(true);
        buttonReveal.setEnabled(false);
    }
}
