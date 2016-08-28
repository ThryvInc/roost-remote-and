package com.rndapp.roostremote.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.api_calls.GetPlacesCall;
import com.rndapp.roostremote.api_calls.LoginCall;
import com.rndapp.roostremote.models.Place;
import com.rndapp.roostremote.models.User;

import java.util.List;

/**
 * Created by ell on 1/9/16.
 */
public class LoginActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private CheckBox mRememberMe;
    private RelativeLayout mSignInRelativeLayout;
    private TextView mSignInTextView;
    private ProgressBar mLoginProgressBar;
    private ImageView mBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupWindowAnimations();

        queue = Volley.newRequestQueue(this);

        mUsernameView = (EditText) findViewById(R.id.et_username);
        mPasswordView = (EditText) findViewById(R.id.et_password);

        mUsernameView.setText(getSharedPreferences("login", Activity.MODE_PRIVATE).getString("username", ""));
        mPasswordView.setText(getSharedPreferences("login", Activity.MODE_PRIVATE).getString("password", ""));

        mRememberMe = (CheckBox) findViewById(R.id.cb_remember);
        mRememberMe.setChecked(true);

        mLoginProgressBar = (ProgressBar) findViewById(R.id.pb_login);

        mBackgroundImageView = (ImageView)findViewById(R.id.iv_login_bg);
        Glide.with(this).load(R.drawable.manhattan2).centerCrop().into(mBackgroundImageView);

        mSignInRelativeLayout = (RelativeLayout) findViewById(R.id.rl_sign_in);
        mSignInRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        mSignInTextView = (TextView) findViewById(R.id.tv_sign_in);
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setEnterTransition(fade);
    }

    private void attemptLogin(){
        mLoginProgressBar.setVisibility(View.VISIBLE);
        mSignInTextView.setVisibility(View.GONE);

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (mRememberMe.isChecked()){
            getSharedPreferences("login", Activity.MODE_PRIVATE).edit()
                .putString("username", username)
                .putString("password", password)
                .apply();
        }

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else {
            mPasswordView.setError(null);
        }

        if (TextUtils.isEmpty(username)){
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            mLoginProgressBar.setVisibility(View.GONE);
            mSignInTextView.setVisibility(View.VISIBLE);
        }else {
            animateSigningIn();

            LoginCall.addRequestToQueue(this, username, password, queue, new LoginCall.UserListener() {
                @Override
                public void onUserParsed(User user) {
                    GetPlacesCall.addRequestToQueue(LoginActivity.this, queue, new GetPlacesCall.PlacesListener() {
                        @Override
                        public void onPlacesParsed(List<Place> places) {
                            startNextActivity(places);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            animateBackToSignIn();
                            mLoginProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    animateBackToSignIn();
                    mLoginProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void animateSigningIn(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int width = (int) (displayMetrics.widthPixels - 32 * displayMetrics.density);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int density = (int) getResources().getDisplayMetrics().density;
                mSignInRelativeLayout.getLayoutParams().width = interpolatedTime == 1 ?
                        RelativeLayout.LayoutParams.WRAP_CONTENT :
                        (int)(width + (44 * density - width) * interpolatedTime);
                mSignInRelativeLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(250);
        mSignInRelativeLayout.startAnimation(animation);
    }

    private void animateBackToSignIn(){
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int width = (int) (displayMetrics.widthPixels - 32 * displayMetrics.density);
                mSignInRelativeLayout.getLayoutParams().width = interpolatedTime == 1 ?
                        RelativeLayout.LayoutParams.MATCH_PARENT :
                        (int)(44 * displayMetrics.density + (width - 44 * displayMetrics.density) * interpolatedTime);
                mSignInRelativeLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(250);
        mSignInRelativeLayout.startAnimation(animation);

        mSignInTextView.setVisibility(View.VISIBLE);
    }

    private void startNextActivity(final List<Place> places){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int finalRadius = Math.max(displayMetrics.heightPixels, displayMetrics.widthPixels);
        Animator animator = ViewAnimationUtils.createCircularReveal(findViewById(R.id.v_reveal),
                displayMetrics.widthPixels / 2,
                (int) (displayMetrics.heightPixels - 38 * displayMetrics.density),
                0,
                finalRadius);
        findViewById(R.id.v_reveal).setVisibility(View.VISIBLE);
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(LoginActivity.this, PlaceActivity.class);
                intent.putExtra(PlaceActivity.PLACE_KEY, places.get(0));
                startActivity(intent);
                LoginActivity.this.finish();
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();
    }
}
