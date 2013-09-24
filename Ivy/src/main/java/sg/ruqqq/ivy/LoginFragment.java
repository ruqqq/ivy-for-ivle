package sg.ruqqq.ivy;

import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import sg.ruqqq.ivy.util.IVLE;

/**
 * Created by ruqqq on 23/9/13.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    public static String TAG = "LoginFragment";

    @ViewById
    ProgressBar progressBar;

    @ViewById
    WebView webView;

    LoginHandler loginHandler;

    static interface LoginHandler {
        void loggedIn(String token);
    }

    void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @AfterViews
    void calledAfterViewInjection() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    if (progressBar.getVisibility() == View.INVISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                } else {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.INVISIBLE);

                    if (view.getUrl() != null && view.getUrl().contains("https://ivle.nus.edu.sg/?token=")) {
                        String token = view.getUrl().replace("https://ivle.nus.edu.sg/?token=", "");

                        if (loginHandler != null)
                            loginHandler.loggedIn(token);
                    }
                }

                progressBar.setProgress(progress);
            }
        });
        webView.loadUrl(IVLE.LOGIN_URL);
    }
}
