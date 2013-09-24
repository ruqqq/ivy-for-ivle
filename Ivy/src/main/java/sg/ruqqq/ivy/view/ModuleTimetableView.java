package sg.ruqqq.ivy.view;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import sg.ruqqq.ivy.R;
import sg.ruqqq.ivy.data.TimetableStudent;

/**
 * Created by ruqqq on 23/9/13.
 */
@EViewGroup(R.layout.module_timetable_list_item)
public class ModuleTimetableView extends RelativeLayout {
    @ViewById
    TextView module;

    @ViewById
    TextView type;

    @ViewById
    TextView venue;

    @ViewById
    TextView time;

    boolean venueAndTimeVisibility = true;

    public ModuleTimetableView(Context context) {
        super(context);
    }

    public void setVenueAndTimeVisibility(boolean visibility) {
        venueAndTimeVisibility = visibility;
    }

    public void bind(TimetableStudent timetable) {
        module.setText(timetable.getModuleCode());
        type.setText(timetable.getLessonType() + " (" +  timetable.getClassNo() + ")");
        venue.setText(timetable.getVenue());

        // formatting OCD
        String startTime = timetable.getStartTime();
        if (startTime.length() == 3)
            startTime = "0" + startTime;
        String endTime = timetable.getEndTime();
        if (endTime.length() == 3)
            endTime = "0" + endTime;

        time.setText(startTime + "hrs - " + endTime + "hrs");

        if (!venueAndTimeVisibility) {
            venue.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        } else {
            venue.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        }
    }
}
