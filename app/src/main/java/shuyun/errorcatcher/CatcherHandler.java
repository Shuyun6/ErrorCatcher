package shuyun.errorcatcher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Shuyun on 2017/9/1 0001.
 */
public class CatcherHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler handler;
    private ArrayList<Activity> listOfActivities;
    private Class errorActivityClass;
    private static CatcherHandler catcherHandler;
    private Context context;
    private Action action;
    private int delayTimeBeforeFinish = 1000;
    private boolean isRestartApp;
    private Class restartActivityClass;
    private int delayTimeOfRestart = 1000;

    public CatcherHandler setActionBeforeFinishActivities(Action action){
        this.action = action;
        return this;
    }

    interface Action{
        void action(Throwable e);
    }

    public CatcherHandler(Context context) {
        this.context = context;
        this.handler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static CatcherHandler getInstance(Context context){
        if(null == catcherHandler)
            catcherHandler = new CatcherHandler(context);
        return catcherHandler;
    }

    /**
     * set Activties that alive to finish them after error being catching
     * @param list
     * @return
     */
    public CatcherHandler setActivities(ArrayList<Activity> list) {
        this.listOfActivities = list;
        return this;
    }

    /**
     * set the activity that showing after error being catching
     * HAS default activity
     * @param activity
     * @return
     */
    public CatcherHandler setErrorActivity(Class activity) {
        this.errorActivityClass = activity;
        return this;
    }

    /**
     * set delay time that between error catching and activities finishing
     * @param millistime default 1000ms
     * @return
     */
    public CatcherHandler setDelayTimeBeforeFinish(int millistime) {
        this.delayTimeBeforeFinish = millistime;
        return this;
    }

    /**
     * restart app after finishing current activity
     * @param millistime delay time between finish and restart
     * @param activityClass the activity that need to be started
     * @param isRestart
     * @return
     */
    public CatcherHandler setRestart(int millistime, Class activityClass, boolean isRestart) {
        this.delayTimeOfRestart = millistime;
        this.restartActivityClass = activityClass;
        this.isRestartApp = isRestart;
        return this;
    }

    public void build(){
        Thread.setDefaultUncaughtExceptionHandler(catcherHandler);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!process(e) && handler != null) {
            handler.uncaughtException(t, e);
        }else{
            if(null != action)
                action.action(e);
            try {
                Thread.sleep(delayTimeBeforeFinish);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (null != listOfActivities)
                for (Activity activity : listOfActivities)
                    if (null != activity)
                        activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            if(isRestartApp)
                restartApp(context, restartActivityClass);
        }
    }

    private boolean process(Throwable throwable) {
        if(null == throwable)
            return false;
        if(null != errorActivityClass){
            Intent intent = new Intent(context, errorActivityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

    private void restartApp(Context context, Class activityClass) {
        if(null == activityClass)
            return;
        Intent intent = new Intent(context, activityClass);
        PendingIntent restartIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + delayTimeOfRestart,
                    restartIntent);
    }

}
