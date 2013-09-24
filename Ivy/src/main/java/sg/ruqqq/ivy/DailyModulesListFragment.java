package sg.ruqqq.ivy;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sg.ruqqq.ivy.data.TimetableStudent;
import sg.ruqqq.ivy.view.ModuleTimetableView;
import sg.ruqqq.ivy.view.ModuleTimetableView_;

/**
 * Created by ruqqq on 23/9/13.
 */
@EFragment(R.layout.fragment_daily_modules_list)
public class DailyModulesListFragment extends Fragment {
    public static String TAG = "DailyModulesListFragment";

    @ViewById
    ListView listView;

    @FragmentArg
    ArrayList<TimetableStudent> timetableList;

    ModuleTimetableListAdapter listAdapter;

    @AfterViews
    void calledAfterViewInjection() {
        Collections.sort(timetableList, new Comparator<TimetableStudent>() {
            @Override
            public int compare(TimetableStudent o1, TimetableStudent o2) {
                return Integer.parseInt(o1.getStartTime()) - Integer.parseInt(o2.getStartTime());
            }
        });

        listAdapter = new ModuleTimetableListAdapter(timetableList);
        listView.setAdapter(listAdapter);
    }

    @ItemClick
    void listViewItemClicked(TimetableStudent timetable) {

    }

    private class ModuleTimetableListAdapter extends BaseAdapter {
        List<TimetableStudent> timetableList;

        ModuleTimetableListAdapter(ArrayList<TimetableStudent> timetableList) {
            this.timetableList = timetableList;
        }

        @Override
        public int getCount() {
            return timetableList.size();
        }

        @Override
        public Object getItem(int i) {
            return timetableList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return timetableList.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ModuleTimetableView moduleTimetableView;
            if (view == null)
                moduleTimetableView = ModuleTimetableView_.build(getActivity());
            else
                moduleTimetableView = (ModuleTimetableView) view;

            moduleTimetableView.bind(timetableList.get(i));
            return moduleTimetableView;
        }
    }
}
