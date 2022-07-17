package com.example.geoquiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.model.Question;

public class MainActivity extends AppCompatActivity {

    public static final String QUESTION_INDEX = "Question_Index";
    public static final String SCORE = "Score";
    public static final String ANSWER_STATUS = "Answer Status";
    public static final String QUESTION_RES_ID = "com.example.geoquiz.Question_Res_Id";
    public static final String QUESTION_ANSWER = "com.example.geoquiz.Question_Answer";
    private static boolean cheated = false;
    private Button mButtonTrue;
    private Button mButtonFalse;
    private Button mButtonNext;
    private Button mButtonPrevious;
    private Button buttonFirst;
    private Button buttonLast;
    private TextView mTextView;
    private TextView textViewScore;
    private TextView textViewEnd;
    private int score = 0;
    private int counter = 0;

    private int mQuestionIndex = 0;
    private Question[] mQuestionBank = {
            new Question(R.string.question_australia, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, false),
            new Question(R.string.question_asia, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
//        if (fragment == null)
//            fragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container,new GeoQuizFragment())
//                    .commit();
        if (savedInstanceState != null) {
            boolean[] answers = savedInstanceState.getBooleanArray(ANSWER_STATUS);
            for (int i = 0;i < 6;i++){
                mQuestionBank[i].setmAnswered(answers[i]);
            }
            mQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX);
            score = savedInstanceState.getInt(SCORE);
        }
        textViewEnd = findViewById(R.id.textView_end);
        textViewEnd.setVisibility(View.GONE);
        textViewScore = findViewById(R.id.textView_score);
        textViewScore.setText("Score: "+ score);
        mTextView = findViewById(R.id.textView_question);
        updateQuestion();

        mButtonTrue = findViewById(R.id.button_true);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mQuestionIndex].setmAnswered(true);
                checkAnswer(true);
                mButtonFalse.setEnabled(false);
                mButtonTrue.setEnabled(false);
            }
        });

        mButtonFalse = findViewById(R.id.button_false);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mQuestionIndex].setmAnswered(true);
                checkAnswer(false);
                mButtonFalse.setEnabled(false);
                mButtonTrue.setEnabled(false);
            }
        });

        mButtonNext = findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = (++mQuestionIndex) % mQuestionBank.length;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex = (++mQuestionIndex) % mQuestionBank.length;
                    counter++;
                    if (counter == 6){
                        end();
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });

        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = (--mQuestionIndex + 6) % mQuestionBank.length;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex = (--mQuestionIndex + 6) % mQuestionBank.length;
                    counter++;
                    if (counter == 6) {
                        end();
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });
        buttonFirst = findViewById(R.id.button_first);
        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = 0;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex++;
                    counter++;
                    if (counter == 6) {
                        end();
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);

            }
        });
        buttonLast = findViewById(R.id.button_last);
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = 5;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex--;
                    counter++;
                    if (counter == 6){
                        end();
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(QUESTION_INDEX,mQuestionIndex);
        boolean[] answers = new boolean[6];
        for (int i = 0;i < 6;i++){
            answers[i] = mQuestionBank[mQuestionIndex].ismAnswered();
        }
        outState.putBooleanArray(ANSWER_STATUS,answers);
        outState.putInt(SCORE,score);
    }

    public void goToCheat(View v) {
        Intent intent = new Intent(this, CheatActivity.class);
        intent.putExtra(QUESTION_RES_ID,getQuestion());
        intent.putExtra(QUESTION_ANSWER,mQuestionBank[mQuestionIndex].isAnswerTrue());
        startActivity(intent);
    }

    private void checkAnswer(boolean userPressed) {
        if (mQuestionBank[mQuestionIndex].isAnswerTrue() == userPressed) {
            if (cheated){
                Toast.makeText(this, "You Cheated!", Toast.LENGTH_SHORT).show();
                score -= 2;
                cheated = false;
            }else {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);
                Toast toast = new Toast(getApplicationContext());
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0,0);
                toast.show();
                score++;
            }

        }else {
            if (cheated){
                Toast.makeText(this, "You Got It Wrong Despite Cheating!!", Toast.LENGTH_SHORT).show();
                score -= 3;
                cheated = false;
            }else{
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view2, null);
                Toast toast = new Toast(getApplicationContext());
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0,0);
                toast.show();
                score--;
            }
        }
    }

    public void end() {
        findViewById(R.id.Layout1).setVisibility(View.GONE);
        findViewById(R.id.Layout2).setVisibility(View.GONE);
        findViewById(R.id.Layout3).setVisibility(View.GONE);
        findViewById(R.id.textView_question).setVisibility(View.GONE);
        findViewById(R.id.button_cheat).setVisibility(View.GONE);
        textViewEnd.setVisibility(View.VISIBLE);
    }

    private void updateQuestion() {
        if (!mQuestionBank[mQuestionIndex].ismAnswered())
            mTextView.setText(mQuestionBank[mQuestionIndex].getTextResId());
    }

    private int getQuestion(){
        return mQuestionBank[mQuestionIndex].getTextResId();
    }

    public static void setCheated(boolean cheated) {
        MainActivity.cheated = cheated;
    }
}
