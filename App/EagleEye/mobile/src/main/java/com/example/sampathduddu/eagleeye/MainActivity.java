package com.example.sampathduddu.eagleeye;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  Eagle Eye");
        toolbar.setLogo(R.drawable.eaglelarge);
//        toolbar.setLogo("eagle");

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final EditText zipcode = (EditText) findViewById(R.id.zipcode);

        zipcode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    String zip = zipcode.getText().toString();
                    Intent myintent = new Intent(MainActivity.this, CongressionalActivity.class);

                    myintent.putExtra("zip", zip);
                    startActivity(myintent);

                    Intent sendIntent = new Intent(MainActivity.this, PhoneToWatchService.class);
                    sendIntent.putExtra("zip", zip);
                    startService(sendIntent);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCongressional(View view) {

        if (view.getId() == R.id.buttonGetLocation) {

            Intent i = new Intent(MainActivity.this, CongressionalActivity.class);
            i.putExtra("zip", "94720");
            startActivity(i);

            Intent sendIntent = new Intent(MainActivity.this, PhoneToWatchService.class);
            sendIntent.putExtra("zip", "94720");
            startService(sendIntent);

        }
    }
}
