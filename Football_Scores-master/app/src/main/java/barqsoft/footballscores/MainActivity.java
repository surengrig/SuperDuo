package barqsoft.footballscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suren Grigoryan on 9/3/15.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int DAYS_COUNT = 5;
    private static final int SELECTED_DAY = DAYS_COUNT / 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        DaysPagerAdapter adapter = new DaysPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(SELECTED_DAY);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class DaysPagerAdapter extends FragmentStatePagerAdapter {
        private final List<String> mTitles = new ArrayList<>();

        public DaysPagerAdapter(FragmentManager fm) {
            super(fm);
            for (int i = 0; i < DAYS_COUNT; i++) {
                mTitles.add(Utility.getDayName(getApplicationContext(), i - DAYS_COUNT / 2));
            }
        }

        @Override
        public Fragment getItem(int position) {
            DayScoresFragment fragment = new DayScoresFragment();
            fragment.setFragmentDate(Utility.getDate(position - DAYS_COUNT / 2));
            return fragment;
        }

        @Override
        public int getCount() {
            return DAYS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
