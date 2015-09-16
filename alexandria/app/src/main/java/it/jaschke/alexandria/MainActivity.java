package it.jaschke.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


/**
 * Created by Suren Grigoryan on 9/14/15.
 */
public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    private static final String TAG = MainActivity.class.getSimpleName();
    private FloatingActionButton mFab;
    private MessageReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main, new MainFragment())
                    .commit();
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookFragment();
            }
        });
        final ActionBar actionBar = getSupportActionBar();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
            mFab.hide();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged");
                if (actionBar != null) {
                    boolean homeAsUpEnabled;

                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        homeAsUpEnabled = true;
                        mFab.hide();
                    } else {
                        homeAsUpEnabled = false;
                        setTitle(getString(R.string.app_name));
                        mFab.show();
                    }
                    actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
                }
            }
        });

        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(MESSAGE_KEY)!=null){
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void addBookFragment() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_main, new AddBook())
                .addToBackStack("BookList")
                .commit();
    }
}
