package com.example.geoquiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.model.Question;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeoQuizFragment extends Fragment {


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
    private Button cheat;
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


    public GeoQuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean[] answers = savedInstanceState.getBooleanArray(ANSWER_STATUS);
            for (int i = 0;i < 6;i++){
                mQuestionBank[i].setmAnswered(answers[i]);
            }
            mQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX);
            score = savedInstanceState.getInt(SCORE);
        }

    }

    public void end(View view) {
        view.findViewById(R.id.Layout1).setVisibility(View.GONE);
        view.findViewById(R.id.Layout2).setVisibility(View.GONE);
        view.findViewById(R.id.Layout3).setVisibility(View.GONE);
        view.findViewById(R.id.textView_question).setVisibility(View.GONE);
        view.findViewById(R.id.button_cheat).setVisibility(View.GONE);
        textViewEnd.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(QUESTION_INDEX,mQuestionIndex);
        boolean[] answers = new boolean[6];
        for (int i = 0;i < 6;i++){
            answers[i] = mQuestionBank[mQuestionIndex].ismAnswered();
        }
        outState.putBooleanArray(ANSWER_STATUS,answers);
        outState.putInt(SCORE,score);
    }


    private void checkAnswer(boolean userPressed) {
        if (mQuestionBank[mQuestionIndex].isAnswerTrue() == userPressed) {
            if (cheated){
                Toast.makeText(getContext(), "You Cheated!", Toast.LENGTH_SHORT).show();
                score -= 2;
                cheated = false;
            }else {
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);
                Toast toast = new Toast(getContext());
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0,0);
                toast.show();
                score++;
            }

        }else {
            if (cheated){
                Toast.makeText(getContext(), "You Got It Wrong Despite Cheating!!", Toast.LENGTH_SHORT).show();
                score -= 3;
                cheated = false;
            }else{
                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view2, null);
                Toast toast = new Toast(getContext());
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(android.view.Gravity.CENTER, 0,0);
                toast.show();
                score--;
            }
        }
    }


    private void updateQuestion() {
        if (!mQuestionBank[mQuestionIndex].ismAnswered())
            mTextView.setText(mQuestionBank[mQuestionIndex].getTextResId());
    }

    private int getQuestion(){
        return mQuestionBank[mQuestionIndex].getTextResId();
    }

//    public static void setCheated(boolean cheated) {
//        MainActivity.cheated = cheated;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_geo_quiz, container, false);
        textViewEnd = view.findViewById(R.id.textView_end);
        textViewEnd.setVisibility(View.GONE);
        textViewScore = view.findViewById(R.id.textView_score);
        textViewScore.setText("Score: "+ score);
        mTextView = view.findViewById(R.id.textView_question);
        updateQuestion();

        mButtonTrue = view.findViewById(R.id.button_true);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mQuestionIndex].setmAnswered(true);
                checkAnswer(true);
                mButtonFalse.setEnabled(false);
                mButtonTrue.setEnabled(false);
            }
        });

        mButtonFalse = view.findViewById(R.id.button_false);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mQuestionIndex].setmAnswered(true);
                checkAnswer(false);
                mButtonFalse.setEnabled(false);
                mButtonTrue.setEnabled(false);
            }
        });

        mButtonNext = view.findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = (++mQuestionIndex) % mQuestionBank.length;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex = (++mQuestionIndex) % mQuestionBank.length;
                    counter++;
                    if (counter == 6){
                        end(view);
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });

        mButtonPrevious = view.findViewById(R.id.button_previous);
        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = (--mQuestionIndex + 6) % mQuestionBank.length;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex = (--mQuestionIndex + 6) % mQuestionBank.length;
                    counter++;
                    if (counter == 6) {
                        end(view);
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });
        buttonFirst = view.findViewById(R.id.button_first);
        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = 0;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex++;
                    counter++;
                    if (counter == 6) {
                        end(view);
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);

            }
        });
        buttonLast = view.findViewById(R.id.button_last);
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScore.setText("Score: "+ score);
                mQuestionIndex = 5;
                while(mQuestionBank[mQuestionIndex].ismAnswered()){
                    mQuestionIndex--;
                    counter++;
                    if (counter == 6){
                        end(view);
                        break;
                    }
                }
                counter = 0;
                updateQuestion();
                mButtonFalse.setEnabled(true);
                mButtonTrue.setEnabled(true);
            }
        });
        cheat = view.findViewById(R.id.button_cheat);
        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheatActivity.class);
                intent.putExtra(QUESTION_RES_ID,getQuestion());
                intent.putExtra(QUESTION_ANSWER,mQuestionBank[mQuestionIndex].isAnswerTrue());
                startActivity(intent);
            }
        });
        return view;
    }

}
