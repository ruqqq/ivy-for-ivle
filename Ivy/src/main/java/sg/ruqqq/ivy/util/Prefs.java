package sg.ruqqq.ivy.util;

import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by ruqqq on 23/9/13.
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Prefs {
    @DefaultString("")
    String lapiToken();

    @DefaultLong(0)
    long lapiToken_Expiry();

    @DefaultString("")
    String lapiUserName();

    @DefaultString("")
    String lapiUserID();

    @DefaultString("")
    String lapiUserEmail();
}
