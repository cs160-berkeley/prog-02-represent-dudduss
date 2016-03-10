package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

/**
 * Created by sampathduddu on 3/9/16.
 */
public class TwitterLoginActivity extends AppCompatActivity {


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "40KO6KoOri8alJhkg3zMfgCJQ";
    private static final String TWITTER_SECRET = "uMMB8u3ZsUeFCTQyxXWYMhWcSND1WvnPWPRRNSIwmiz2tQSShu";

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();

        if (authToken != null) {

            Intent i = new Intent(TwitterLoginActivity.this, MainActivity.class);
            startActivity(i);

        }
//        String token = authToken.token;
//        String secret = authToken.secret;
//
//        if (token )

//        authenticate();

        setContentView(R.layout.twitter_login);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTwitter);
        toolbar.setTitle("  Twitter Login");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        setSupportActionBar(toolbar);


        loginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(com.twitter.sdk.android.core.Result<TwitterSession> result) {



                Intent i = new Intent(TwitterLoginActivity.this, MainActivity.class);
                startActivity(i);

            }

            @Override
            public void failure(TwitterException e) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
