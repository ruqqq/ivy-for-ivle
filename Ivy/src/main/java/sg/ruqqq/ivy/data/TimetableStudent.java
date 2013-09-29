package sg.ruqqq.ivy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.util.ArrayList;

import sg.ruqqq.ivy.IvyApp;
import sg.ruqqq.ivy.util.IVLE;

/**
 * Created by ruqqq on 24/9/13.
 */
public class TimetableStudent implements Parcelable {
    public static String TAG = "IVY/TimetableStudent";

    String AcadYear;
    String Semester;
    String StartTime;
    String EndTime;
    String ModuleCode;
    String ClassNo;
    String LessonType;
    String Venue;
    int DayCode;
    String DayText;
    String WeekCode;
    String WeekText;

    public static class Outer {
        TimetableStudent[] Results;

        String Comments;

        public TimetableStudent[] getResults() {
            return Results;
        }

        public String getComments() {
            return Comments;
        }
    }

    public TimetableStudent(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TimetableStudent createFromParcel(Parcel in) {
            return new TimetableStudent(in);
        }

        public TimetableStudent[] newArray(int size) {
            return new TimetableStudent[size];
        }
    };

    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.AcadYear);
        parcel.writeString(this.Semester);
        parcel.writeString(this.StartTime);
        parcel.writeString(this.EndTime);
        parcel.writeString(this.ModuleCode);
        parcel.writeString(this.ClassNo);
        parcel.writeString(this.LessonType);
        parcel.writeString(this.Venue);
        parcel.writeInt(this.DayCode);
        parcel.writeString(this.DayText);
        parcel.writeString(this.WeekCode);
        parcel.writeString(this.WeekText);
    }

    public void readFromParcel(Parcel in) {
        this.AcadYear = in.readString();
        this.Semester = in.readString();
        this.StartTime = in.readString();
        this.EndTime = in.readString();
        this.ModuleCode = in.readString();
        this.ClassNo = in.readString();
        this.LessonType = in.readString();
        this.Venue = in.readString();
        this.DayCode = in.readInt();
        this.DayText = in.readString();
        this.WeekCode = in.readString();
        this.WeekText = in.readString();
    }

    public String getAcadYear() {
        return AcadYear;
    }

    public String getSemester() {
        return Semester;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getModuleCode() {
        return ModuleCode;
    }

    public String getClassNo() {
        return ClassNo;
    }

    public String getLessonType() {
        return LessonType;
    }

    public String getVenue() {
        return Venue;
    }

    public int getDayCode() {
        return DayCode;
    }

    public String getDayText() {
        return DayText;
    }

    public String getWeekCode() {
        return WeekCode;
    }

    public String getWeekText() {
        return WeekText;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "AcadYear='" + AcadYear + '\'' +
                ", Semester='" + Semester + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", ModuleCode='" + ModuleCode + '\'' +
                ", ClassNo='" + ClassNo + '\'' +
                ", LessonType='" + LessonType + '\'' +
                ", Venue='" + Venue + '\'' +
                ", DayCode='" + DayCode + '\'' +
                ", DayText='" + DayText + '\'' +
                ", WeekCode='" + WeekCode + '\'' +
                ", WeekText='" + WeekText + '\'' +
                '}';
    }

    public static void GetTimetable(final Context context, final String token, final DataHandler handler) {
        final String url = IVLE.MyOrganizer.TIMETABLE_STUDENT_URL(token, IvyApp.getAcadYear(), IvyApp.getAcadSemester());

        ArrayList<ArrayList<TimetableStudent>> timetableList = null;
        if (!IvyApp.hasInternet(context))
            timetableList = GetTimetableFromCache(context, url);

        if (timetableList != null) {
            //Toast.makeText(context, "Loading data from cache", Toast.LENGTH_SHORT).show();
            handler.onLoad(timetableList);
        } else if (IvyApp.hasInternet(context)) {
            GetTimetableFromServer(context, url, handler);
        } else {
            handler.onError(null, "No Internet");
        }
    }

    private static ArrayList<ArrayList<TimetableStudent>> GetTimetableFromCache(final Context context, final String url) {
        final SharedPreferences prefs = DataPrefs.get(context);
        final Gson gson = new Gson();

        String cached = prefs.getString(DataPrefs.DATA + url, null);
        ArrayList<ArrayList<TimetableStudent>> timetableList = null;
        if (cached != null) {
            try {
                Type type = new TypeToken<ArrayList<ArrayList<TimetableStudent>>>(){}.getType();
                timetableList = gson.fromJson(cached, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return timetableList;
    }

    private static void GetTimetableFromServer(final Context context, final String url, final DataHandler handler) {
        final SharedPreferences prefs = DataPrefs.get(context);

        //Toast.makeText(context, "Loading data from internet", Toast.LENGTH_SHORT).show();

        Ion.with(context, url)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String s) {
                    Gson gson = new Gson();
                    ArrayList<ArrayList<TimetableStudent>> timetableList = new ArrayList<ArrayList<TimetableStudent>>();

                    TimetableStudent.Outer outer = gson.fromJson(s, TimetableStudent.Outer.class);
                    Log.d(TAG, s);

                    if (e != null || outer.getResults().length == 0) {
                        timetableList = GetTimetableFromCache(context, url);
                        if (timetableList != null) {
                            handler.onLoad(timetableList);
                        } else {
                            handler.onError(e, outer == null ? "" : outer.getComments());
                        }

                        return;
                    }

                    ArrayList<String> moduleCodes = new ArrayList<String>();
                    for (int i = 0; i < 6; i++) {
                        timetableList.add(new ArrayList<TimetableStudent>());
                    }

                    for (TimetableStudent t : outer.getResults()) {
                        Log.d(TAG, t.toString());
                        if (t.getDayCode() > 5) continue;

                        if (moduleCodes.indexOf(t.getModuleCode()) < 0) {
                            moduleCodes.add(t.getModuleCode());
                            timetableList.get(0).add(t);
                        }

                        timetableList.get(t.getDayCode()).add(t);
                    }

                    // cache data
                    prefs
                        .edit()
                        .putString(DataPrefs.DATA + url, gson.toJson(timetableList))
                        .putLong(DataPrefs.LAST_FETCH + url, System.currentTimeMillis())
                        .commit();

                    handler.onLoad(timetableList);
                }
            });
    }
}
