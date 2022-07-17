package com.example.geoquiz.model;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswered = false;


    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean ismAnswered() {
        return mAnswered;
    }

    public void setmAnswered(boolean mAnswered) {
        this.mAnswered = mAnswered;
    }

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }
}
