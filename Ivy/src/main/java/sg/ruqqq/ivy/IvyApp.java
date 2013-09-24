package sg.ruqqq.ivy;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import sg.ruqqq.ivy.util.Prefs_;

/**
 * Created by ruqqq on 24/9/13.
 */
@EApplication
public class IvyApp extends Application {
    @Pref
    Prefs_ prefs;

    public static class Login {
        String token;
        String id;
        String name;
        String email;
        long expiry;
        long lastValidCheck;
    }

    private Login login = new Login();

    @Override
    public void onCreate() {
        super.onCreate();
        reloadLoginFromPref();
    }

    public Login getLogin() {
        return login;
    }

    public Login reloadLoginFromPref() {
        login.token = prefs.lapiToken().get();
        login.id = prefs.lapiUserID().get();
        login.name = prefs.lapiUserName().get();
        login.email = prefs.lapiUserEmail().get();
        login.expiry = prefs.lapiToken_Expiry().get();

        return login;
    }

    public void clearLogin() {
        prefs.edit().lapiUserID().put("").lapiUserName().put("").lapiUserEmail().put("").lapiToken().put("").lapiToken_Expiry().put(0).apply();
        reloadLoginFromPref();
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
