package sg.ruqqq.ivy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import sg.ruqqq.ivy.util.Prefs_;

@EActivity(R.layout.activity_one_fragment)
public class LoginActivity extends FragmentActivity implements LoginFragment.LoginHandler {
    @Pref
    Prefs_ prefs;

    @FragmentById(R.id.fragment)
    LoginFragment fragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!prefs.lapiToken().get().equals("")) {
            Toast.makeText(this, "You're already logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }

        fragmentLogin = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragmentLogin == null)
            fragmentLogin = LoginFragment_.builder().build();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragmentLogin).commit();

        fragmentLogin.setLoginHandler(this);
    }

    @Override
    public void loggedIn(String token) {
        prefs.edit().lapiToken().put(token).apply();
        //Toast.makeText(this, "Logged in with token: " + token, Toast.LENGTH_SHORT).show();
        setResult(1);
        finish();
    }
}
