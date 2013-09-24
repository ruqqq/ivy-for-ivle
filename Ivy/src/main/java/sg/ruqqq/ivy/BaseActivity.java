package sg.ruqqq.ivy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Timer;
import java.util.TimerTask;

import sg.ruqqq.ivy.util.IVLE;
import sg.ruqqq.ivy.util.Prefs_;

/**
 * Created by ruqqq on 24/9/13.
 */
public abstract class BaseActivity extends FragmentActivity {
    public static String TAG = "BaseActivity";

    protected Prefs_ prefs;

    protected IvyApp.Login login;

    protected Gson gson = new Gson();

    protected boolean isViewLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new Prefs_(this);

        login = ((IvyApp) getApplication()).getLogin();

        checkSession();
    }

    protected void checkSession() {
        // if token expiring in more than 10 mins, don't need to do session check
        if (login.expiry != 0 && login.lastValidCheck != 0 && login.expiry - login.lastValidCheck > 1000 * 60 * 10) {
            waitForViewLoad();
            return;
        }

        if (login.token.equals("")) {
            LoginActivity_.intent(this).startForResult(1337);
        } else {
            // Do not need to do session check if not connected to internet
            if (!IvyApp.hasInternet(this)) {
                waitForViewLoad();
                return;
            }

            Log.d(TAG, "Logged in with token: " + login.token);
            Ion.with(this, IVLE.Login.VALIDATE_URL(login.token))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String s) {
                        IVLE.Login.ValidateResp res = gson.fromJson(s, IVLE.Login.ValidateResp.class);
                        if (!res.isSuccess()) {
                            ((IvyApp) getApplication()).clearLogin();
                            LoginActivity_.intent(BaseActivity.this).startForResult(1337);
                        } else {
                            prefs.edit().lapiToken().put(res.getToken()).lapiToken_Expiry().put(res.getValidTill()).apply();
                            login.lastValidCheck = System.currentTimeMillis();
                            ((IvyApp) getApplication()).reloadLoginFromPref();
                            getUserDetails();
                            //Toast.makeText(BaseActivity.this, "Expiry: " + res.getValidTill(), Toast.LENGTH_LONG).show();
                            waitForViewLoad();
                        }
                    }
                });
        }
    }

    private void getUserDetails() {
        // get user id if not cached
        if (login.id.equals("")) {
            Ion.with(this, IVLE.Login.USERID_GET_URL(login.token))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String s) {
                            String s2 = gson.fromJson(s, String.class);
                            if (e == null && s2 != null) {
                                prefs.edit().lapiUserID().put(s2).apply();
                            }
                        }
                    });
        }

        // get user name if not cached
        if (login.name.equals("")) {
            Ion.with(this, IVLE.Login.USERNAME_GET_URL(login.token))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String s) {
                            String s2 = gson.fromJson(s, String.class);
                            if (e == null && s2 != null) {
                                prefs.edit().lapiUserName().put(s2).apply();
                            }
                        }
                    });
        }

        // get user email if not cached
        if (login.email.equals("")) {
            Ion.with(this, IVLE.Login.USEREMAIL_GET_URL(login.token))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String s) {
                            String s2 = gson.fromJson(s, String.class);
                            if (e == null && s2 != null) {
                                prefs.edit().lapiUserEmail().put(s2).apply();
                            }
                        }
                    });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1337 && resultCode == 1) {
            ((IvyApp) getApplication()).reloadLoginFromPref();
            checkSession();
        }
    }

    protected void waitForViewLoad() {
        // if view is not loaded, wait for it to load
        if (!isViewLoaded) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waitForViewLoad();
                        }
                    });
                }
            }, 100);
            return;
        } else {
            onLoggedIn();
        }
    }

    protected abstract void onLoggedIn();
}
