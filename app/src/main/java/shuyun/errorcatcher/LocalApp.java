package shuyun.errorcatcher;

import android.app.Activity;
import android.app.Application;
import android.os.Looper;

import java.util.ArrayList;

/**
 * Created by Shuyun on 2017/9/1 0001.
 */
public class LocalApp extends Application {

    private ArrayList<Activity> listOfActivities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addActivity(Activity activity){
        listOfActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        listOfActivities.remove(activity);
    }

    public ArrayList<Activity> getListOfActivities() {
        return listOfActivities;
    }

}
