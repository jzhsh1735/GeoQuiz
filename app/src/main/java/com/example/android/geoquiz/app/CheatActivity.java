package com.example.android.geoquiz.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_TRUE = "com.example.android.geoquiz.app.answer_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.android.geoquiz.app.answer_shown";
    private static final String CHEAT_KEY = "cheat";

    private boolean mAnswerTrue;
    private boolean mAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context context, boolean answerTrue) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_TRUE, answerTrue);
        return intent;
    }

    private void setAnswerShownResult(boolean answerShown) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER_SHOWN, answerShown);
        setResult(RESULT_OK, intent);
    }

    public static boolean isAnswerShown(Intent intent) {
        return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_TRUE, false);
        mAnswerShown = false;

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        if (mAnswerTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        mAnswerTextView.setVisibility(View.INVISIBLE);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerShown = true;
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator animator = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();
                } else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(CHEAT_KEY, false);
            if (mAnswerShown) {
                setAnswerShownResult(true);
                mAnswerTextView.setVisibility(View.VISIBLE);
                mShowAnswerButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CHEAT_KEY, mAnswerShown);
    }
}
