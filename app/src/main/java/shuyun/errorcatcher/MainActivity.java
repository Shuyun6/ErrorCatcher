package shuyun.errorcatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalApp app = (LocalApp) getApplication();
        app.addActivity(this);

        //start catching
        CatcherHandler.getInstance(getApplicationContext())
                .setActivities(app.getListOfActivities())
                .setErrorActivity(ErrorPagerActivity.class)
                .setDelayTime(1200)
                .build();

        Button bt_senderror = (Button) findViewById(R.id.bt_senderror);
        bt_senderror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //throw a null point exception without catching
                Log.e("", null);
            }
        });
    }
}
