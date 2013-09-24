package sg.ruqqq.ivy.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruqqq on 24/9/13.
 */
public final class DataPrefs {
    public static String DATA = "data_";
    public static String LAST_FETCH = "last_fetch_";

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences("CACHED_DATA", 0);
    }
}
