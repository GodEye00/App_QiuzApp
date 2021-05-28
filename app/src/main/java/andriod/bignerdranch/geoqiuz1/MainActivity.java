package andriod.bignerdranch.geoqiuz1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity<mAnsweredToggle> extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATER = "Cheater";
    private static final String KEY_TOKEN = "TokenRemaining";
    private static final String KEY_ANSWERED = "Answered";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final int mQuestionLimit = 15;
    private boolean userPressedAnswer;
    public Button mTrueButton;
    public Button mFalseButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mRemainingTokenTextView;


    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_usa, false),
                    new Question(R.string.question_africa, true),
                    new Question(R.string.question_southafrica, false),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_egypt, false),
                    new Question(R.string.question_everest, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_korea, false),
                    new Question(R.string.question_russia, true),
                    new Question(R.string.question_australia2, true),
                    new Question(R.string.question_mideast2, true),
                    new Question(R.string.question_india, false),
                    new Question(R.string.question_africa2, false),
                    new Question(R.string.question_africa3, false),
                    new Question(R.string.question_mideast3, true),
                    new Question(R.string.question_brazil, false),
                    new Question(R.string.question_asia2, true),
                    new Question(R.string.question_japan, false),
            };

    private boolean[] mIsCheater = new boolean[mQuestionBank.length];

    private boolean[] mAnsweredToggle = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;
    private int mCurrentScore = 0;
    private int mRemainingToken = 3;
    private int mAnswers = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCurrentScore = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBooleanArray(KEY_CHEATER);
            mRemainingToken = savedInstanceState.getInt(KEY_TOKEN, 3);
            mAnswers = savedInstanceState.getInt(KEY_INDEX,0);
            mAnsweredToggle = savedInstanceState.getBooleanArray(KEY_ANSWERED);

        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mRemainingTokenTextView = (TextView) findViewById(R.id.token_text_view);
        showCheatToken();


        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        setCheatInvis();
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               commWithCheatActivity();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mAnsweredToggle[mCurrentIndex] = checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnsweredToggle[mCurrentIndex] = checkAnswer(false);           }
        });


        updateQuestion();
        }


















        private void showCheatToken () {

            if (mRemainingToken == 3) {
                mRemainingTokenTextView.setText("You have " + mRemainingToken + " cheat tokens");
            }
             else if (mRemainingToken == 2) {
                mRemainingTokenTextView.setText("Remaining Cheat Tokens: "+mRemainingToken);
            }

            else if (mRemainingToken == 1) {
                mRemainingTokenTextView.setText("Remaining Cheat Tokens: ONLY "+mRemainingToken);
            }

            else  if (mRemainingToken == 0) {
                mRemainingTokenTextView.setVisibility(View.INVISIBLE);


            }
        }

        private void setCheatInvis () {
        if (mRemainingToken == 0) {
            mCheatButton.setVisibility(View.INVISIBLE);
        } else {return;}
        }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            if (requestCode == REQUEST_CODE_CHEAT) {
                if (data == null) {
                    return;
                }

                mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);

                mRemainingToken--;
                mRemainingTokenTextView.setText("Remaining Cheat Tokens: ONLY "+mRemainingToken);

                if (mRemainingToken == 0) {
                    mCheatButton.setVisibility(View.INVISIBLE);
                    mRemainingTokenTextView.setVisibility(View.INVISIBLE);
                }

            }
        }


        @Override
        public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

        if (mAnsweredToggle[mCurrentIndex]) {
            toggleButtonTo(false);
        } else {
            toggleButtonTo(true);
        }
        if (mIsCheater[mCurrentIndex]) {
            mCheatButton.setEnabled(false);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_INDEX, mCurrentScore);
        savedInstanceState.putBooleanArray(KEY_CHEATER, mIsCheater);
        savedInstanceState.putInt(KEY_TOKEN, mRemainingToken);
        savedInstanceState.putInt(KEY_INDEX, mAnswers);
        savedInstanceState.putBooleanArray(KEY_ANSWERED, mAnsweredToggle);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void prevQuestion() {
        if (mCurrentIndex != 0 ) {
            mCurrentIndex = mCurrentIndex - 1;

            if (mAnsweredToggle[mCurrentIndex]) {
                toggleButtonTo(false);
            } else {
                toggleButtonTo(true);
            }
        }

        if (mCurrentIndex != mQuestionBank.length -1)
        {
            mNextButton.setEnabled(true);
        }
        if (mIsCheater[mCurrentIndex]) {
            mCheatButton.setEnabled(false);
        }

        updateQuestion();
    }


    private void nextQuestion() {
        mCurrentIndex = ( mCurrentIndex + 1 ) % (mQuestionBank.length);
        mCheatButton.setEnabled(true);

        if (mAnsweredToggle[mCurrentIndex]) {
            toggleButtonTo(false);

        } else {
            toggleButtonTo(true);
        }


        if (mCurrentIndex == 0) {
            mCurrentScore = 0; mAnswers = 0; mRemainingToken = 3;
            for (int i=0; i<=mQuestionBank.length-1; i++) {
            mAnsweredToggle[i] = false;
        }
        toggleButtonTo(true);
        CharSequence text = "You have Started Again";
            Toast.makeText(this,text,Toast.LENGTH_LONG).show();

            mCheatButton.setVisibility(View.VISIBLE);
            mRemainingTokenTextView.setVisibility(View.VISIBLE);
        }

        if ((mCurrentIndex == mQuestionBank.length -1) && (mAnswers != mQuestionBank.length)) {
            mNextButton.setEnabled(false);
        }

        updateQuestion();
    }


    private void updateQuestion () {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private boolean checkAnswer(boolean userPressedTrue) {

        toggleButtonTo(false);
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
         boolean mAnswered = false;
         int cheated_Score = 0;

        int messageResId = 0;

        if (mIsCheater[mCurrentIndex]) {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_cheater;
                mCurrentScore += 1;
                Log.d(TAG, "Current Score is " + mCurrentScore);
            } else {
                messageResId = R.string.wrong_cheater;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

            messageResId = R.string.judgment_toast;
            mAnswers += 1;
            cheated_Score += 1;
            mAnswered = true;

        } else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mCurrentScore += 1;
                Log.d(TAG, "Current Score is " + mCurrentScore);
                mAnswers += 1;
                mAnswered = true;

            } else {
                messageResId = R.string.incorrect_toast;
                mAnswers += 1;
                mAnswered = true;

            }

        }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();


        if ((mCurrentIndex == mQuestionBank.length -1) && (mAnswers != mQuestionBank.length)) {
            mNextButton.setEnabled(false);
            unfinished();
        }
        else if ((mCurrentIndex != mQuestionBank.length -1) && (mAnswers == mQuestionBank.length)) {
            showScore();
            if (mCurrentScore > mQuestionLimit) {
                showCongrats();
            }
        }
        else if ((mCurrentIndex == mQuestionBank.length -1) && (mAnswers == mQuestionBank.length)) {
            showScore();
            if (mCurrentScore > mQuestionLimit) {
                showCongrats();
            }
            mNextButton.setEnabled(true);
        }


    return mAnswered;
    }

    private void showScore() {
        String text1 = "You have answered all Your Questions.";
        String text2 = "Please wait for your score";
        String text3 = "You had "+mCurrentScore+"/"+mQuestionBank.length;
        Toast.makeText(this,text1,Toast.LENGTH_LONG).show();
        Toast.makeText(this,text2,Toast.LENGTH_LONG).show();
        Toast.makeText(this,text3,Toast.LENGTH_LONG).show();
    }

    private void showCongrats() {
        String text = "Congratulations!!! You passed the past score, you are a Genius!!!!!!!";
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();

    }


    private void unfinished() {
        int remainingQuest = mQuestionBank.length - mAnswers;
        String text = "You have "+remainingQuest+" more questions to answer!";
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }


    private void toggleButtonTo(boolean toggleTo) {
        if (toggleTo == true) {mTrueButton.setEnabled(true); mFalseButton.setEnabled(true); mCheatButton.setEnabled(true);}
        else {
            mTrueButton.setEnabled(false); mFalseButton.setEnabled(false); mCheatButton.setEnabled(false);
        }

    }

    private void commWithCheatActivity() {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
    }


}


