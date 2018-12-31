package shuyun.errorcatcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Catch global errors or exceptions
 * @author shuyun
 * create at 2017/9/1 0018 17:29
 * change at 2018/7/18 0018 17:29
*/
public class CatcherHandler implements Thread.UncaughtExceptionHandler {

    private static CatcherHandler catcherHandler;

    private Thread.UncaughtExceptionHandler handler;
    private ArrayList<Activity> listOfActivities;
    private Class errorActivityClass;
    private Context context;
    private ActionCallback callback;
    private int delayTimeBeforeFinish = 300;

    /**
     * Action what happens before closing all activities
     * @param action
     * @return
     */
    public CatcherHandler before(ActionCallback action){
        this.callback = action;
        return this;
    }

    private CatcherHandler(Context context) {
        this.context = context;
        this.handler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static CatcherHandler getInstance(Context context){
        if(null == catcherHandler) {
            synchronized (CatcherHandler.class) {
                if (null == catcherHandler) {
                    catcherHandler = new CatcherHandler(context);
                }
            }
        }
        return catcherHandler;
    }

    /**
     * add Activty that alive to finish them after error being catching
     * @param activiy
     * @return
     */
    public CatcherHandler addActivity(Activity activiy) {
        if (null == listOfActivities) {
            listOfActivities = new ArrayList<>();
        }
        listOfActivities.add(activiy);
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
     * @param millistime default 300ms
     * @return
     */
    public CatcherHandler setDelayTimeBeforeFinish(int millistime) {
        this.delayTimeBeforeFinish = millistime;
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
            if(null != callback) {
                callback.execute(e);
            }
            try {
                Thread.sleep(delayTimeBeforeFinish);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (null != listOfActivities) {
                for (Activity activity : listOfActivities) {
                    if (null != activity) {
                        activity.finish();
                    }
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private boolean process(Throwable throwable) {
        if(null == throwable) {
            return false;
        }
        if(null != errorActivityClass){
            Intent intent = new Intent(context, errorActivityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return true;
    }

}
