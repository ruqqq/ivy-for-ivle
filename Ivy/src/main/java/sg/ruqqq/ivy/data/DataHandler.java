package sg.ruqqq.ivy.data;

/**
 * Created by ruqqq on 24/9/13.
 */
public interface DataHandler {
    void onLoad(Object object);
    void onError(Exception e, String comments);
}
