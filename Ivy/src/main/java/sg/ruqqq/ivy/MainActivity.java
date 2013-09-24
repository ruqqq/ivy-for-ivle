package sg.ruqqq.ivy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.viewpagerindicator.TabPageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import sg.ruqqq.ivy.data.DataHandler;
import sg.ruqqq.ivy.data.TimetableStudent;
import sg.ruqqq.ivy.util.IVLE;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    static String TAG = "IVY/MainActivity";

    @ViewById
    TabPageIndicator indicator;

    @ViewById
    ViewPager viewPager;

    @FragmentById(R.id.fragment)
    DailyModulesListFragment fragmentDailyModulesList;

    @InstanceState
    ArrayList<ArrayList<TimetableStudent>> timetableList;

    DailyPagerAdapter pagerAdapter;

    @InstanceState
    boolean isLoaded;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @AfterViews
    void calledAfterViewInjection() {
        pagerAdapter = new DailyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        isViewLoaded = true;
    }

    @Override
    protected void onLoggedIn() {
        if (!isLoaded) {
            TimetableStudent.GetTimetable(this, login.token, new DataHandler() {
                @Override
                public void onLoad(Object object) {
                    timetableList = (ArrayList) object;

                    isLoaded = true;
                    pagerAdapter = new DailyPagerAdapter(getSupportFragmentManager());
                    viewPager.setAdapter(pagerAdapter);
                    indicator.notifyDataSetChanged();

                    // set current page to today
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    dayOfWeek--;
                    if (dayOfWeek > 5) dayOfWeek = 0;
                    indicator.setCurrentItem(dayOfWeek);
                }

                // TODO: Better error handling, perhaps AppMsg?
                @Override
                public void onError(Exception e, String comments) {
                    Toast.makeText(MainActivity.this, "Error getting timetable. Try restarting application. (" + comments + ")", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // TODO: Implement logout function
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    String[] TITLES = {"ALL", "M", "T", "W", "T", "F"};

    class DailyPagerAdapter extends FragmentPagerAdapter {
        public DailyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (timetableList.get(i) == null) return null;

            Fragment fragment;
            if (i == 0)
                fragment = AllModulesListFragment_.builder().timetableList(timetableList.get(0)).build();
            else
                fragment = DailyModulesListFragment_.builder().timetableList(timetableList.get(i)).build();

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position].toUpperCase();
        }

        @Override
        public int getCount() {
            return isLoaded ? TITLES.length : 0;
        }
    }
}
