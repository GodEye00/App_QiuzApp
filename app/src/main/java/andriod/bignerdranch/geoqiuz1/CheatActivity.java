package andriod.bignerdranch.geoqiuz1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "android.bignerdranch.geoqiuz1.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.bignerdranch.geoqiuz1.answer_shown";
    private static final String KEY_SHOWN = "Shown";
    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;



    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_SHOWN, false);
        }


        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);


        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswer();

                showAnimation();

            }
        });

                if (mIsAnswerShown)
        {
            showAnswer();
        }

    }










    private void showAnswer () {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);

        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        mIsAnswerShown = true;
        setAnswerShowResult(mIsAnswerShown);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CheatActivity", "onPause() called");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("CheatActivity", "onResume() called");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       outState.putBoolean(KEY_SHOWN, mIsAnswerShown);

    }


    private void setAnswerShowResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    private void showAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton,
                    cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }

    }

}